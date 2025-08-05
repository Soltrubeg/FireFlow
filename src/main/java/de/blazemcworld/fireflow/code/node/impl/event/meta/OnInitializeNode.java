package de.blazemcworld.fireflow.code.node.impl.event.meta;

import de.blazemcworld.fireflow.code.CodeThread;
import de.blazemcworld.fireflow.code.EventContext;
import de.blazemcworld.fireflow.code.node.EventNode;
import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.SignalType;
import org.bukkit.Material;

public class OnInitializeNode extends Node implements EventNode {

    private final Output<Void> signal;

    public OnInitializeNode() {
        super("on_initialize", "On Initialize", "Emits a signal when the space loads or reloads.", Material.CHERRY_SAPLING);

        signal = new Output<>("signal", "Signal", SignalType.INSTANCE);
    }

    @Override
    public Node copy() {
        return new OnInitializeNode();
    }

    @Override
    public void handleEvent(EventContext context) {
        if (context.customEvent != EVENT) return;

        CodeThread thread = context.newCodeThread();
        thread.sendSignal(signal);
        thread.clearQueue();
    }

    public static final Object EVENT = new Object();
}
