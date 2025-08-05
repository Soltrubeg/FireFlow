package de.blazemcworld.fireflow.code;

import com.google.gson.JsonObject;
import de.blazemcworld.fireflow.code.node.EventNode;
import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.node.Node.Varargs;
import de.blazemcworld.fireflow.code.node.impl.event.meta.DebugEventNode;
import de.blazemcworld.fireflow.code.node.impl.event.meta.OnInitializeNode;
import de.blazemcworld.fireflow.code.node.impl.event.meta.OnPlayerJoinNode;
import de.blazemcworld.fireflow.code.node.impl.event.meta.OnPlayerLeaveNode;
import de.blazemcworld.fireflow.code.node.impl.function.FunctionCallNode;
import de.blazemcworld.fireflow.code.node.impl.function.FunctionDefinition;
import de.blazemcworld.fireflow.code.node.impl.function.FunctionInputsNode;
import de.blazemcworld.fireflow.code.node.impl.function.FunctionOutputsNode;
import de.blazemcworld.fireflow.code.node.impl.player.visual.SetPlayerSkinNode;
import de.blazemcworld.fireflow.code.widget.NodeWidget;
import de.blazemcworld.fireflow.code.widget.Widget;
import de.blazemcworld.fireflow.code.widget.WidgetVec;
import de.blazemcworld.fireflow.space.Space;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class CodeEvaluator {

    private static final long CPU_MAX = 20_000_000;
    private static final long CPU_BONUS_MAX = 5_000_000;
    private static final long CPU_BONUS_STEP = 100_000;

    public final Space space;
    private boolean stopped = false;
    public final VariableStore sessionVariables = new VariableStore();
    private Set<Node> nodes;
    private Set<EventNode> eventNodes;
    public final World world;
    private final Set<Runnable> tickTasks = new HashSet<>();
    private boolean initCalled = false;
    private int revision = 0; // Incremented after each live reload
    private long cpuUsage = 0;
    private long cpuBonusGranted = 0;

    public CodeEvaluator(Space space) {
        this.space = space;
        world = space.playWorld;

        Set<NodeWidget> nodes = new HashSet<>();
        for (Widget widget : space.editor.rootWidgets) {
            if (widget instanceof NodeWidget nodeWidget) {
                nodes.add(nodeWidget);
            }
        }

        this.nodes = copyNodes(nodes);
        updateEventNodes();
    }

    public void stop() {
        stopped = true;
    }

    public boolean isStopped() {
        return stopped;
    }

    private Set<Node> copyNodes(Set<NodeWidget> nodes) {
        HashMap<Node, Node> old2new = new HashMap<>();

        HashMap<String, FunctionDefinition> functions = new HashMap<>();

        for (FunctionDefinition old : space.editor.functions.values()) {
            FunctionDefinition copy = new FunctionDefinition(old.name, old.icon);
            for (Node.Output<?> input : old.inputsNode.outputs) {
                copy.addInput(input.id, input.type);
            }
            for (Node.Input<?> output : old.outputsNode.inputs) {
                copy.addOutput(output.id, output.type);
            }
            functions.put(old.name, copy);
        }

        for (NodeWidget nodeWidget : nodes) {
            Node node = nodeWidget.node;
            Node copy = null;

            if (node instanceof FunctionCallNode call) {
                copy = new FunctionCallNode(functions.get(call.function.name));
            }

            if (node instanceof FunctionInputsNode inputsNode) {
                copy = functions.get(inputsNode.function.name).inputsNode;
            }

            if (node instanceof FunctionOutputsNode outputsNode) {
                copy = functions.get(outputsNode.function.name).outputsNode;
            }

            if (copy == null) copy = node.copy();
            
            for (Varargs<?> base : node.varargs) {
                for (Varargs<?> next : copy.varargs) {
                    if (!base.id.equals(next.id)) continue;
                    next.ignoreUpdates = true;
                    copy.inputs.removeAll(next.children);
                    next.children.clear();

                    for (Node.Input<?> input : base.children) {
                        next.addInput(input.id);
                    }
                }
            }

            old2new.put(node, copy);
            copy.originWidget = new WeakReference<>(nodeWidget);
            copy.evalUUID = node.evalUUID;
            copy.evalRevision = revision;
        }

        for (NodeWidget oldWidget : nodes) {
            Node old = oldWidget.node;
            Node copy = old2new.get(old);
            for (int i = 0; i < copy.inputs.size(); i++) {
                Node.Input<?> newInput = copy.inputs.get(i);
                Node.Output<?> oldTarget = old.inputs.get(i).connected;
                if (oldTarget == null) continue;
                Node.Output<?> newTarget = old2new.get(oldTarget.getNode()).outputs.get(oldTarget.getNode().outputs.indexOf(oldTarget));
                if (newTarget == null) continue;
                newInput.connect(newTarget);
            }

            for (int i = 0; i < copy.outputs.size(); i++) {
                Node.Output<?> newOutput = copy.outputs.get(i);
                Node.Input<?> oldTarget = old.outputs.get(i).connected;
                if (oldTarget == null) continue;
                newOutput.connected = old2new.get(oldTarget.getNode()).inputs.get(oldTarget.getNode().inputs.indexOf(oldTarget));
            }

            for (int i = 0; i < copy.inputs.size(); i++) {
                Node.Input<?> newInput = copy.inputs.get(i);
                Node.Input<?> oldInput = old.inputs.get(i);
                if (oldInput.inset == null) continue;
                newInput.setInset(oldInput.inset);
            }
        }

        return new HashSet<>(old2new.values());
    }

    public void tick() {
        if (stopped) return;
        cpuUsage = 0;
        cpuBonusGranted = 0;
        Set<Runnable> tasks;
        synchronized (tickTasks) {
            tasks = new HashSet<>(tickTasks);
            tickTasks.clear();
        }
        for (Runnable task : tasks) task.run();
    }

    public void exitPlay(Player player) {
        EventContext ctx = new EventContext(this);
        ctx.customEvent = new OnPlayerLeaveNode.LeaveEvent(player);
        emitEvent(ctx);

        SetPlayerSkinNode.reset(player);
    }

    public void nextTick(Runnable r) {
        synchronized (tickTasks) {
            tickTasks.add(r);
        }
    }

    private void ensureInit() {
        if (!initCalled) {
            initCalled = true;

            EventContext ctx = new EventContext(this);
            ctx.customEvent = OnInitializeNode.EVENT;
            emitEvent(ctx);
        }
    }

    public void emitEvent(EventContext ctx) {
        for (EventNode node : eventNodes) {
            node.handleEvent(ctx);
        }
    }

    public void onJoin(Player player) {
        EventContext ctx = new EventContext(this);
        ctx.customEvent = new OnPlayerJoinNode.JoinEvent(player);
        emitEvent(ctx);
    }

    public void triggerDebug(String id, EditOrigin origin) {
        ensureInit();
        EventContext ctx = new EventContext(this);
        DebugEventNode.DebugEvent event = new DebugEventNode.DebugEvent(id);
        ctx.customEvent = new DebugEventNode.DebugEvent(id);
        emitEvent(ctx);
        if (!event.found) origin.sendError("No debug event with id '" + id + "' found.");
    }

    public void visualizeDebug(Node node) {
        space.editor.nextTick(() -> {
            NodeWidget widget = node.originWidget.get();
            if (widget == null) return;

            WidgetVec pos = widget.pos();
            space.editor.world.spawnParticle(Particle.DUST.builder().color(0xFFFF00).particle(), pos.loc(), 1);

            JsonObject json = new JsonObject();
            json.addProperty("type", "debug");
            json.addProperty("x", pos.x());
            json.addProperty("y", pos.y());
            space.editor.webBroadcast(json);
        });
    }

    public void liveReload() {
        nextTick(() -> {
            revision++;
            Set<NodeWidget> widgets = new HashSet<>();
            for (Widget widget : space.editor.rootWidgets) {
                if (widget instanceof NodeWidget nodeWidget) {
                    widgets.add(nodeWidget);
                }
            }
            this.nodes = copyNodes(widgets);
            updateEventNodes();
        });
    }

    private void updateEventNodes() {
        Set<EventNode> updated = new HashSet<>();
        for (Node node : nodes) {
            if (node instanceof EventNode en) updated.add(en);
        }
        eventNodes = updated;
    }

    public void syncRevision(Node old) {
        if (old.evalRevision == revision) return;

        for (Node current : nodes) {
            if (current.originWidget.get() != old.originWidget.get()) continue;
            if (old.originWidget.get() == null) continue;

            for (Node.Varargs<?> oldVarargs : old.varargs) {
                oldVarargs.ignoreUpdates = true;

                for (Node.Varargs<?> currentVarargs : current.varargs) {
                    if (!oldVarargs.id.equals(currentVarargs.id)) continue;
                    old.inputs.removeAll(oldVarargs.children);
                    oldVarargs.children.clear();

                    for (Node.Input<?> input : currentVarargs.children) {
                        oldVarargs.addInput(input.id);
                    }
                }
            }

            for (Node.Input<?> oldInput : old.inputs) {
                for (Node.Input<?> currentInput : current.inputs) {
                    if (!oldInput.id.equals(currentInput.id)) continue;

                    oldInput.connected = currentInput.connected;
                    oldInput.inset = currentInput.inset;
                    break;
                }
            }

            for (Node.Output<?> oldOutput : old.outputs) {
                for (Node.Output<?> currentOutput : current.outputs) {
                    if (!oldOutput.id.equals(currentOutput.id)) continue;

                    oldOutput.connected = currentOutput.connected;
                    break;
                }
            }

            for (Node.Varargs<?> oldVarargs : old.varargs) {
                oldVarargs.ignoreUpdates = false;
            }
            break;
        }

        old.evalRevision = revision;
    }

    public int cpuPercentage() {
        return Math.clamp(cpuUsage * 100 / CPU_MAX, 0, 100);
    }

    public void cpuUsed(long usage) {
        cpuUsage += usage;
    }

    public boolean shouldWait() {
        return cpuUsage > CPU_MAX + cpuBonusGranted;
    }

    public void grantCpuBonus() {
        cpuBonusGranted = Math.min(Math.max(0, cpuUsage - CPU_MAX + CPU_BONUS_STEP), CPU_BONUS_MAX);
    }
}
