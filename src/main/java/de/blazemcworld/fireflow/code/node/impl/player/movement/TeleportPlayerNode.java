package de.blazemcworld.fireflow.code.node.impl.player.movement;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.PlayerType;
import de.blazemcworld.fireflow.code.type.PositionType;
import de.blazemcworld.fireflow.code.type.SignalType;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import org.bukkit.Location;
import org.bukkit.Material;

public class TeleportPlayerNode extends Node {

    public TeleportPlayerNode() {
        super("teleport_player", "Teleport Player", "Teleports the player to a position", Material.ENDER_PEARL);

        Input<Void> signal = new Input<>("signal", "Signal", SignalType.INSTANCE);
        Input<PlayerValue> player = new Input<>("player", "Player", PlayerType.INSTANCE);
        Input<Location> position = new Input<>("position", "Position", PositionType.INSTANCE);
        Output<Void> next = new Output<>("next", "Next", SignalType.INSTANCE);

        signal.onSignal((ctx) -> {
            player.getValue(ctx).tryUse(ctx, p -> {
                Location loc = position.getValue(ctx);
                loc.setWorld(ctx.evaluator.world);
                p.teleport(loc);
            });
            ctx.sendSignal(next);
        });
    }

    @Override
    public Node copy() {
        return new TeleportPlayerNode();
    }

}
