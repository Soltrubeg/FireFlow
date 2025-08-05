package de.blazemcworld.fireflow.code.node.impl.entity;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.EntityType;
import de.blazemcworld.fireflow.code.type.StringType;
import de.blazemcworld.fireflow.code.value.EntityValue;
import org.bukkit.Material;

public class EntityTypeNode extends Node {

    public EntityTypeNode() {
        super("entity_type", "Entity Type", "Returns the type, given an entity, or 'invalid' if the entity could not be found.", Material.SPYGLASS);

        Input<EntityValue> entity = new Input<>("entity", "Entity", EntityType.INSTANCE);
        Output<String> type = new Output<>("type", "Type", StringType.INSTANCE);

        type.valueFrom((ctx) -> entity.getValue(ctx).apply(ctx, e -> e.getType().key().value(), "invalid"));
    }

    @Override
    public Node copy() {
        return new EntityTypeNode();
    }
}
