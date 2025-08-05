package de.blazemcworld.fireflow.code.node;

import de.blazemcworld.fireflow.FireFlow;
import de.blazemcworld.fireflow.code.CodeThread;
import de.blazemcworld.fireflow.code.node.option.InputOptions;
import de.blazemcworld.fireflow.code.type.ListType;
import de.blazemcworld.fireflow.code.type.StringType;
import de.blazemcworld.fireflow.code.type.WireType;
import de.blazemcworld.fireflow.code.value.ListValue;
import de.blazemcworld.fireflow.code.widget.NodeWidget;
import org.bukkit.Material;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Node {

    public final String id;
    public final Material icon;
    public final String name;
    public final String description;
    public List<Input<?>> inputs = new ArrayList<>();
    public List<Varargs<?>> varargs = new ArrayList<>();
    public List<Output<?>> outputs = new ArrayList<>();
    public WeakReference<NodeWidget> originWidget; // Only available in the CodeEvaluator nodes, reference to the widget that created this node
    public String evalUUID = UUID.randomUUID().toString();
    public int evalRevision = 0;

    protected Node(String id, String name, String description, Material icon) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.icon = icon;
    }

    public abstract Node copy();

    public List<WireType<?>> getTypes() {
        return null;
    }

    public int getTypeCount() {
        return 0;
    }

    public boolean acceptsType(WireType<?> type, int index) {
        return false;
    }

    public Node copyWithTypes(List<WireType<?>> types) {
        return copy();
    }

    public class Input<T> {
        public final String id;
        public final String name;
        public final WireType<T> type;
        public String inset;
        public Output<?> connected;
        public final Varargs<T> varargsParent;
        private Consumer<CodeThread> logic;
        public InputOptions options;

        private static final Pattern inlineFormat = Pattern.compile("\\{\\w*(:\\w+)*}");

        public Input(String id, String name, WireType<T> type, Varargs<T> varargsParent) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.varargsParent = varargsParent;
            inputs.add(this);
        }

        public Input(String id, String name, WireType<T> type) {
            this(id, name, type, null);
        }

        @SuppressWarnings("unchecked")
        public T getValue(CodeThread ctx) {
            ctx.evaluator.syncRevision(Node.this);
            if (connected != null) {
                ctx.notifyDebug(connected);
                Object out = connected.computeNow(ctx);

                if (inset != null && type.canConvert(StringType.INSTANCE)) {
                    Matcher m = inlineFormat.matcher(inset);
                    StringBuilder builder = new StringBuilder();
                    while (m.find()) {
                        m.appendReplacement(builder, connected.type.stringify(out, m.group().substring(1, m.group().length() - 1)));
                    }
                    m.appendTail(builder);
                    T value = type.parseInset(builder.toString());
                    if (value != null) return value;
                    return type.defaultValue();
                }

                if (connected.type == type) return (T) out;
                return type.convert(connected.type, out);
            }
            if (inset != null) {
                T value = type.parseInset(inset);
                if (value != null) return value;
            }
            return type.defaultValue();
        }

        public Input<T> options(String... options) {
            return options(new InputOptions.Choice(Arrays.asList(options), this));
        }

        public Input<T> options(InputOptions options) {
            this.options = options;
            setInset(options.fallback());
            return this;
        }

        public void onSignal(Consumer<CodeThread> logic) {
            this.logic = logic;
        }

        private void computeNow(CodeThread ctx) {
            if (logic == null) return;
            logic.accept(ctx);
        }

        public Node getNode() {
            return Node.this;
        }

        public void setInset(String value) {
            inset = value;
            if (!type.canConvert(StringType.INSTANCE) || (inset != null && !inlineFormat.matcher(inset).find())) connected = null;
            if (varargsParent != null) varargsParent.update();
            if (inset == null && options != null) inset = options.fallback();
        }

        public void connect(Output<?> output) {
            if (output == null) {
                connected = null;
            } else if (canUnderstand(output.type)) {
                connected = output;
            } else {
                FireFlow.logger.warning("Called input.connect() with invalid wire type!");
                Thread.dumpStack();
            }
            if (connected != null && inset != null && (!type.canConvert(StringType.INSTANCE) || !inlineFormat.matcher(inset).find())) inset = null;
            if (varargsParent != null) varargsParent.update();
            if (connected == null && options != null) inset = options.fallback();
        }

        public boolean canUnderstand(WireType<?> other) {
            if (other == type || type.canConvert(other)) return true;

            if (varargsParent != null && other instanceof ListType<?> l) {
                return l.elementType == type || type.canConvert(l.elementType);
            }
            return false;
        }
    }

    public class Output<T> {
        public final String id;
        public final String name;
        public final WireType<T> type;
        public Input<?> connected;
        private Function<CodeThread, T> logic;

        public Output(String id, String name, WireType<T> type) {
            this.id = id;
            this.type = type;
            this.name = name;
            outputs.add(this);
        }

        public void valueFrom(Function<CodeThread, T> logic) {
            this.logic = logic;
        }

        public void sendSignalImmediately(CodeThread ctx) {
            if (connected == null) return;
            connected.computeNow(ctx);
        }

        public T computeNow(CodeThread ctx) {
            return logic.apply(ctx);
        }

        public Node getNode() {
            return Node.this;
        }

        public void valueFromScope() {
            logic = (ctx) -> ctx.getScopeValue(this);
        }
    }

    public class Varargs<T> {
        public final String id;
        public final String name;
        public final WireType<T> type;
        public List<Input<T>> children = new ArrayList<>();
        public boolean ignoreUpdates = false;

        public Varargs(String id, String name, WireType<T> type) {
            this.id = id;
            this.name = name;
            this.type = type;
            varargs.add(this);
            addInput(UUID.randomUUID().toString());
        }

        @SuppressWarnings("unchecked")
        public List<T> getVarargs(CodeThread ctx) {
            ctx.evaluator.syncRevision(Node.this);
            List<T> list = new ArrayList<>();
            for (Input<T> input : children) {
                if (input.inset == null && input.connected == null) continue;
                if (input.connected != null && input.type != input.connected.type && input.connected.type instanceof ListType<?> l && (input.type.canConvert(l.elementType) || l.elementType == input.type)) {
                    ListValue<?> spread = (ListValue<?>) input.connected.computeNow(ctx);
                    for (int i = 0; i < spread.size(); i++) {
                        if (spread.type == input.type) {
                            list.add((T) spread.get(i));
                        } else {
                            list.add(input.type.convert(spread.type, spread.get(i)));
                        }
                    }
                    continue;
                }
                list.add(input.getValue(ctx));
            }
            return list;
        }

        public void update() {
            if (ignoreUpdates) return;
            List<Input<T>> used = new ArrayList<>();
            for (Input<T> input : children) {
                if (input.inset != null || input.connected != null) {
                    used.add(input);
                }
            }

            if (used.size() == children.size()) {
                addInput(UUID.randomUUID().toString());
                return;
            }

            for (Input<T> input : new ArrayList<>(children)) {
                if (used.contains(input)) continue;
                if (input != children.getLast()) {
                    inputs.remove(input);
                    children.remove(input);
                }
            }

            if (!used.contains(children.getLast())) return;
            addInput(UUID.randomUUID().toString());
        }

        public void addInput(String uuid) {
            Input<T> input = new Input<>(uuid, name, type, this);
            children.add(input);
            inputs.remove(input);
            for (int i = inputs.size() - 1; i >= 0; i--) {
                if (inputs.get(i).varargsParent == this) {
                    inputs.add(i + 1, input);
                    return;
                }
            }
            inputs.add(input);
        }
    }
}
