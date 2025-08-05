package de.blazemcworld.fireflow.code;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class EventContext {
    public final @Nullable Event event;
    public final CodeEvaluator evaluator;
    private boolean allowCancel = false;
    public Object customEvent;

    public EventContext(CodeEvaluator evaluator, @Nullable Event event) {
        this.event = event;
        this.evaluator = evaluator;
    }
    public EventContext(CodeEvaluator evaluator) {
        this.event = null;
        this.evaluator = evaluator;
    }

    public EventContext allowCancel() {
        allowCancel = true;
        return this;
    }

    public CodeThread newCodeThread() {
        return new CodeThread(evaluator, this);
    }

    public void setCancelled(boolean value) {
        if (!allowCancel) return;
        if (event instanceof Cancellable c) c.setCancelled(value);
    }
}
