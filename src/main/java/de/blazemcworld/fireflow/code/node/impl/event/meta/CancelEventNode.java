package de.blazemcworld.fireflow.code.node.impl.event.meta;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.ConditionType;
import de.blazemcworld.fireflow.code.type.SignalType;
import org.bukkit.Material;

public class CancelEventNode extends Node {

    public CancelEventNode() {
        super("cancel_event", "Cancel Event", "Enables or disables the default behaviour of an event.", Material.BARRIER);

        Input<Void> signal = new Input<>("signal", "Signal", SignalType.INSTANCE);
        Input<Boolean> cancel = new Input<>("cancel", "Cancel", ConditionType.INSTANCE);
        Output<Void> next = new Output<>("next", "Next", SignalType.INSTANCE);
        signal.onSignal((ctx) -> {
            ctx.context.setCancelled(cancel.getValue(ctx));
            ctx.sendSignal(next);
        });
    }

    @Override
    public Node copy() {
        return new CancelEventNode();
    }
}
