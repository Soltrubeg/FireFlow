package de.blazemcworld.fireflow.code.node.impl.entity;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.EntityType;
import de.blazemcworld.fireflow.code.type.ListType;
import de.blazemcworld.fireflow.code.value.EntityValue;
import de.blazemcworld.fireflow.code.value.ListValue;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EntityListNode extends Node {

    public EntityListNode() {
        super("entity_list", "Entity List", "Gets a list of all entities currently in the space, players are excluded", Material.LIGHT_BLUE_DYE);

        Output<ListValue<EntityValue>> entities = new Output<>("entities", "Entities", ListType.of(EntityType.INSTANCE));

        entities.valueFrom((ctx) -> {
            List<EntityValue> out = new ArrayList<>();
            for (Entity entity : ctx.evaluator.world.getEntities()) {
                if (entity instanceof Player) continue;
                out.add(new EntityValue(entity));
            }
            return new ListValue<>(EntityType.INSTANCE, out);
        });
    }

    @Override
    public Node copy() {
        return new EntityListNode();
    }
}
