package de.blazemcworld.fireflow.code.node.impl.player.movement;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.ConditionType;
import de.blazemcworld.fireflow.code.type.PlayerType;
import de.blazemcworld.fireflow.code.type.SignalType;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import org.bukkit.Material;

public class SetPlayerFlyingNode extends Node {
    public SetPlayerFlyingNode() {
        super("set_player_flying", "Set Player Flying", "Sets the flying state of the player", Material.FEATHER);
        Input<Void> signal = new Input<>("signal", "Signal", SignalType.INSTANCE);
        Input<PlayerValue> player = new Input<>("player", "Player", PlayerType.INSTANCE);
        Input<Boolean> flying = new Input<>("flying", "Flying", ConditionType.INSTANCE);
        Output<Void> next = new Output<>("next", "Next", SignalType.INSTANCE);
        signal.onSignal((ctx) -> {
            player.getValue(ctx).tryUse(ctx, p -> {
                p.setFlying(flying.getValue(ctx));
            });
            ctx.sendSignal(next);
        });
    }

    @Override
    public Node copy() {
        return new SetPlayerFlyingNode();
    }
}
