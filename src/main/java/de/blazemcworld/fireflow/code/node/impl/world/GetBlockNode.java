package de.blazemcworld.fireflow.code.node.impl.world;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.StringType;
import de.blazemcworld.fireflow.code.type.VectorType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;

public class GetBlockNode extends Node {
    public GetBlockNode() {
        super("get_block", "Get Block", "Gets the block at a position", Material.ENDER_EYE);

        Input<Vector> position = new Input<>("position", "Position", VectorType.INSTANCE);
        Output<String> block = new Output<>("block", "Block", StringType.INSTANCE);

        block.valueFrom((ctx) -> {
            Vector pos = position.getValue(ctx);
            return new Location(ctx.evaluator.world, pos.getX(), pos.getY(), pos.getZ()).getBlock().getType().key().value();
        });
    }

    @Override
    public Node copy() {
        return new GetBlockNode();
    }
}