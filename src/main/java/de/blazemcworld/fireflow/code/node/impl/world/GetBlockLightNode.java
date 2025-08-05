package de.blazemcworld.fireflow.code.node.impl.world;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.NumberType;
import de.blazemcworld.fireflow.code.type.VectorType;
import org.bukkit.Material;
import org.bukkit.util.Vector;

public class GetBlockLightNode extends Node {

    public GetBlockLightNode() {
        super("get_block_light", "Get Block Light", "Gets the light level of a block", Material.EYE_ARMOR_TRIM_SMITHING_TEMPLATE);

        Input<Vector> position = new Input<>("position", "Position", VectorType.INSTANCE);
        Output<Double> light = new Output<>("light", "Light", NumberType.INSTANCE);
        Output<Double> blockLight = new Output<>("block_light", "Block Light", NumberType.INSTANCE);
        Output<Double> skyLight = new Output<>("sky_light", "Sky Light", NumberType.INSTANCE);

        light.valueFrom((ctx) -> (double) position.getValue(ctx).toLocation(ctx.evaluator.world).getBlock().getLightLevel());
        blockLight.valueFrom((ctx) -> (double) position.getValue(ctx).toLocation(ctx.evaluator.world).getBlock().getLightFromBlocks());
        skyLight.valueFrom((ctx) -> (double) position.getValue(ctx).toLocation(ctx.evaluator.world).getBlock().getLightFromSky());
    }

    @Override
    public Node copy() {
        return new GetBlockLightNode();
    }
}
