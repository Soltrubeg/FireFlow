package de.blazemcworld.fireflow.code.node.impl.entity;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.EntityType;
import de.blazemcworld.fireflow.code.type.PositionType;
import de.blazemcworld.fireflow.code.type.SignalType;
import de.blazemcworld.fireflow.code.type.StringType;
import de.blazemcworld.fireflow.code.value.EntityValue;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.entity.Entity;

public class SpawnEntityNode extends Node {

    public SpawnEntityNode() {
        super("spawn_entity", "Spawn Entity", "Spawns a new entity at the given coordinates.", Material.DARK_OAK_SAPLING);

        Input<Void> signal = new Input<>("signal", "Signal", SignalType.INSTANCE);
        Input<Location> position = new Input<>("position", "Position", PositionType.INSTANCE);
        Input<String> type = new Input<>("type", "Type", StringType.INSTANCE);

        Output<Void> next = new Output<>("next", "Next", SignalType.INSTANCE);
        Output<EntityValue> entity = new Output<>("entity", "Entity", EntityType.INSTANCE);
        entity.valueFromScope();

        signal.onSignal((ctx) -> {
            org.bukkit.entity.EntityType entityType = Registry.ENTITY_TYPE.get(NamespacedKey.minecraft(type.getValue(ctx)));
            if (entityType != null && entityType.isSpawnable()) {
                Entity spawned = ctx.evaluator.world.spawnEntity(position.getValue(ctx), entityType);
                ctx.setScopeValue(entity, new EntityValue(spawned));
            }

            ctx.sendSignal(next);
        });
    }

    @Override
    public Node copy() {
        return new SpawnEntityNode();
    }
}
