package de.blazemcworld.fireflow.code.node.impl.entity;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.EntityType;
import de.blazemcworld.fireflow.code.type.PositionType;
import de.blazemcworld.fireflow.code.type.SignalType;
import de.blazemcworld.fireflow.code.value.EntityValue;
import org.bukkit.Location;
import org.bukkit.Material;

public class TeleportEntityNode extends Node {

    public TeleportEntityNode() {
        super("teleport_entity", "Teleport Entity", "Teleports an entity to a position", Material.ENDER_PEARL);

        Input<Void> signal = new Input<>("signal", "Signal", SignalType.INSTANCE);
        Input<EntityValue> entity = new Input<>("entity", "Entity", EntityType.INSTANCE);
        Input<Location> position = new Input<>("position", "Position", PositionType.INSTANCE);
        Output<Void> next = new Output<>("next", "Next", SignalType.INSTANCE);

        signal.onSignal((ctx) -> {
            entity.getValue(ctx).use(ctx, e -> {
                Location loc = position.getValue(ctx);
                loc.setWorld(ctx.evaluator.world);
                e.teleport(loc);
            });
            ctx.sendSignal(next);
        });
    }

    @Override
    public Node copy() {
        return new TeleportEntityNode();
    }
}