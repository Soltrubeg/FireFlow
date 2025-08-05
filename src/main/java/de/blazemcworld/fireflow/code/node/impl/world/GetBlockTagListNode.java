package de.blazemcworld.fireflow.code.node.impl.world;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.ListType;
import de.blazemcworld.fireflow.code.type.StringType;
import de.blazemcworld.fireflow.code.type.VectorType;
import de.blazemcworld.fireflow.code.value.ListValue;
import net.minecraft.world.level.block.state.properties.Property;
import org.bukkit.Material;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class GetBlockTagListNode extends Node {

    public GetBlockTagListNode() {
        super("get_block_tag_list", "Get Block Tag List", "Gets the list of all tag names of a block.", Material.ACACIA_STAIRS);

        Input<Vector> position = new Input<>("position", "Position", VectorType.INSTANCE);
        Output<ListValue<String>> list = new Output<>("list", "List", ListType.of(StringType.INSTANCE));

        list.valueFrom((ctx) -> {
            CraftBlockState state = (CraftBlockState) ctx.evaluator.world.getBlockState(position.getValue(ctx).toLocation(ctx.evaluator.world));
            List<String> contents = new ArrayList<>();
            for (Property<?> property : state.getHandle().getProperties()) {
                contents.add(property.getName());
            }
            return new ListValue<>(StringType.INSTANCE, contents);
        });
    }

    @Override
    public Node copy() {
        return new GetBlockTagListNode();
    }
}