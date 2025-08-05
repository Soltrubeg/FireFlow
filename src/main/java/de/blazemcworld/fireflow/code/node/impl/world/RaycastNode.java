package de.blazemcworld.fireflow.code.node.impl.world;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.ConditionType;
import de.blazemcworld.fireflow.code.type.StringType;
import de.blazemcworld.fireflow.code.type.VectorType;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class RaycastNode extends Node {

    public RaycastNode() {
        super("raycast", "Raycast", "Sends out a raycast, like a ray of light and returns if, where and what it hits.", Material.SPECTRAL_ARROW);

        Input<Vector> start = new Input<>("start", "Start", VectorType.INSTANCE);
        Input<Vector> end = new Input<>("end", "End", VectorType.INSTANCE);
        Input<Boolean> fluids = new Input<>("fluids", "Fluids", ConditionType.INSTANCE);
        Output<Vector> point = new Output<>("point", "Point", VectorType.INSTANCE);
        Output<String> block = new Output<>("block", "Block", StringType.INSTANCE);
        Output<Vector> side = new Output<>("side", "Side", VectorType.INSTANCE);

        point.valueFrom(ctx -> {
            Vector startVec = start.getValue(ctx);
            Vector endVec = end.getValue(ctx);
            RayTraceResult result = ctx.evaluator.world.rayTrace(
                    startVec.toLocation(ctx.evaluator.world), endVec.clone().subtract(startVec).normalize(),
                    512, fluids.getValue(ctx) ? FluidCollisionMode.ALWAYS : FluidCollisionMode.NEVER, false, 0, null
            );
            if (result == null) return endVec;
            return result.getHitPosition();
        });

        block.valueFrom(ctx -> {
            Vector startVec = start.getValue(ctx);
            Vector endVec = end.getValue(ctx);
            RayTraceResult result = ctx.evaluator.world.rayTraceBlocks(
                    startVec.toLocation(ctx.evaluator.world), endVec.clone().subtract(startVec).normalize(),
                    512, fluids.getValue(ctx) ? FluidCollisionMode.ALWAYS : FluidCollisionMode.NEVER, false
            );
            if (result == null) return Registry.MATERIAL.getKey(endVec.toLocation(ctx.evaluator.world).getBlock().getType()).getKey();;
            return Registry.MATERIAL.getKey(result.getHitBlock().getType()).getKey();
        });

        side.valueFrom(ctx -> {
            Vector startVec = start.getValue(ctx);
            Vector endVec = end.getValue(ctx);
            RayTraceResult result = ctx.evaluator.world.rayTraceBlocks(
                    startVec.toLocation(ctx.evaluator.world), endVec.clone().subtract(startVec).normalize(),
                    512, fluids.getValue(ctx) ? FluidCollisionMode.ALWAYS : FluidCollisionMode.NEVER, false
            );
            if (result == null || result.getHitBlockFace() == null) return new Vector();
            return result.getHitBlockFace().getDirection();
        });
    }

    @Override
    public Node copy() {
        return new RaycastNode();
    }
}
