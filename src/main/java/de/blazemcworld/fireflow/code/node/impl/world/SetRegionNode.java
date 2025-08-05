package de.blazemcworld.fireflow.code.node.impl.world;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.ConditionType;
import de.blazemcworld.fireflow.code.type.SignalType;
import de.blazemcworld.fireflow.code.type.StringType;
import de.blazemcworld.fireflow.code.type.VectorType;
import net.minecraft.util.Mth;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class SetRegionNode extends Node {
    public SetRegionNode() {
        super("set_region", "Set Region", "Sets a region of blocks", Material.POLISHED_ANDESITE);
        Input<Void> signal = new Input<>("signal", "Signal", SignalType.INSTANCE);
        Input<Vector> corner1 = new Input<>("corner1", "Corner 1", VectorType.INSTANCE);
        Input<Vector> corner2 = new Input<>("corner2", "Corner 2", VectorType.INSTANCE);
        Input<String> block = new Input<>("block", "Block", StringType.INSTANCE);
        Input<Boolean> sendUpdate = new Input<>("send_update", "Send Update", ConditionType.INSTANCE);
        Output<Void> next = new Output<>("next", "Next", SignalType.INSTANCE);

        signal.onSignal((ctx) -> {
            Material material = Material.getMaterial(block.getValue(ctx));
            if (material != null && material.isBlock()) {
                boolean updates = sendUpdate.getValue(ctx);

                Vector corner1Value = corner1.getValue(ctx);
                Vector corner2Value = corner2.getValue(ctx);

                corner1Value = new Vector(
                        Math.floor(corner1Value.getX()),
                        Math.floor(corner1Value.getY()),
                        Math.floor(corner1Value.getZ())
                );
                corner2Value = new Vector(
                        Math.floor(corner2Value.getX()),
                        Math.floor(corner2Value.getY()),
                        Math.floor(corner2Value.getZ())
                );

                int minX = Mth.floor(Math.max(-512, Math.min(corner1Value.getX(), corner2Value.getX())));
                int minY = Mth.floor(Math.max(ctx.evaluator.world.getMinHeight(), Math.min(corner1Value.getY(), corner2Value.getY())));
                int minZ = Mth.floor(Math.max(-512, Math.min(corner1Value.getZ(), corner2Value.getZ())));
                int maxX = Mth.floor(Math.min(511, Math.max(corner1Value.getX(), corner2Value.getX())));
                int maxY = Mth.floor(Math.min(ctx.evaluator.world.getMaxHeight(), Math.max(corner1Value.getY(), corner2Value.getY())));
                int maxZ = Mth.floor(Math.min(511, Math.max(corner1Value.getZ(), corner2Value.getZ())));

                World world = ctx.evaluator.world;

                for (int x = minX; x <= maxX; x++) {
                    for (int y = minY; y <= maxY; y++) {
                        for (int z = minZ; z <= maxZ; z++) {
                            world.getBlockAt(x, y, z).setType(material, updates);
                        }
                    }
                }
            }
            ctx.sendSignal(next);
        });
    }

    @Override
    public Node copy() {
        return new SetRegionNode();
    }
}