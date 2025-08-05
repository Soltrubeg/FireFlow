package de.blazemcworld.fireflow.code;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.SignalType;

import java.util.Stack;

public class CodeThread {

    public final CodeEvaluator evaluator;
    private final Stack<Runnable> todo = new Stack<>();
    public final VariableStore threadVariables = new VariableStore();
    public FunctionScope functionScope = new FunctionScope(null, null);
    private boolean paused = false;
    public final EventContext context;
    private boolean isDebug = false;

    public CodeThread(CodeEvaluator evaluator, EventContext context) {
        this.evaluator = evaluator;
        this.context = context == null ? new EventContext(evaluator) : context;
    }

    @SuppressWarnings("unchecked")
    public <T> T getScopeValue(Node.Output<T> out) {
        Object v = functionScope.scopeStore.get(out.getNode().evalUUID + "_" + out.id);
        return v == null ? out.type.defaultValue() : (T) v;
    }

    public <T> void setScopeValue(Node.Output<T> out, T value) {
        functionScope.scopeStore.put(out.getNode().evalUUID + "_" + out.id, value);
    }

    public void sendSignal(Node.Output<Void> signal) {
        todo.add(() -> {
            notifyDebug(signal);
            evaluator.syncRevision(signal.getNode());
            signal.sendSignalImmediately(this);
        });
    }

    public void notifyDebug(Node.Output<?> out) {
        if (!isDebug) return;
        if (out.type == SignalType.INSTANCE) {
            Node.Input<?> input = out.connected;
            if (input == null) return;
            evaluator.visualizeDebug(input.getNode());
        } else {
            evaluator.visualizeDebug(out.getNode());
        }
    }

    public void submit(Runnable r) {
        todo.add(r);
    }

    public void clearQueue() {
        if (evaluator.isStopped()) return;
        long last = System.nanoTime();
        while (!todo.isEmpty() && !paused) {
            todo.pop().run();
            long now = System.nanoTime();
            evaluator.cpuUsed(now - last);
            last = now;
            if (evaluator.shouldWait()) {
                pause();
                evaluator.nextTick(this::resume);
                return;
            }
            if (evaluator.isStopped()) return;
        }
    }

    public CodeThread subThread() {
        CodeThread thread = new CodeThread(evaluator, context);
        thread.functionScope = functionScope.simpleCopy();
        thread.isDebug = isDebug;
        return thread;
    }

    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
        clearQueue();
    }

    public void markDebug() {
        isDebug = true;
    }

    public void handleEvent() {
        evaluator.grantCpuBonus();
        clearQueue();
    }
}
