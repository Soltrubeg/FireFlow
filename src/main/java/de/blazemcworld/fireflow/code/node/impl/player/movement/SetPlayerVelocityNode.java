package de.blazemcworld.fireflow.code.node.impl.player.movement;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.PlayerType;
import de.blazemcworld.fireflow.code.type.SignalType;
import de.blazemcworld.fireflow.code.type.VectorType;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import org.bukkit.Material;
import org.bukkit.util.Vector;

public class SetPlayerVelocityNode extends Node {
    public SetPlayerVelocityNode() {
        super("set_player_velocity", "Set Player Velocity", "Sets the velocity (motion) of the player", Material.ARROW);
        Input<Void> signal = new Input<>("signal", "Signal", SignalType.INSTANCE);
        Input<PlayerValue> player = new Input<>("player", "Player", PlayerType.INSTANCE);
        Input<Vector> velocity = new Input<>("velocity", "Velocity", VectorType.INSTANCE);
        Output<Void> next = new Output<>("next", "Next", SignalType.INSTANCE);
        signal.onSignal((ctx) -> {
            player.getValue(ctx).tryUse(ctx, p -> {
                p.setVelocity(velocity.getValue(ctx));
            });
            ctx.sendSignal(next);
        });
    }

    @Override
    public Node copy() {
        return new SetPlayerVelocityNode();
    }
}
