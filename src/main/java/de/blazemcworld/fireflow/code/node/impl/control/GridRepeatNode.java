package de.blazemcworld.fireflow.code.node.impl.control;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.SignalType;
import de.blazemcworld.fireflow.code.type.StringType;
import de.blazemcworld.fireflow.code.type.VectorType;
import net.minecraft.util.Mth;
import org.bukkit.Material;
import org.bukkit.util.Vector;

public class GridRepeatNode extends Node {

    public GridRepeatNode() {
        super("grid_repeat", "Grid Repeat", "Emits a signal for each block position in a region. If relative end is enabled, the end is an offset from the start.", Material.DARK_PRISMARINE);

        Input<Void> signal = new Input<>("signal", "Signal", SignalType.INSTANCE);
        Input<Vector> start = new Input<>("start", "Start", VectorType.INSTANCE);
        Input<Vector> end = new Input<>("end", "End", VectorType.INSTANCE);
        Input<String> mode = new Input<>("mode", "Mode", StringType.INSTANCE).options("absolute_end", "relative_end");
        Output<Void> repeat = new Output<>("repeat", "Repeat", SignalType.INSTANCE);
        Output<Vector> current = new Output<>("current", "Current", VectorType.INSTANCE);
        Output<Void> next = new Output<>("next", "Next", SignalType.INSTANCE);
        current.valueFromScope();

        signal.onSignal((ctx) -> {
            Vector startValue = start.getValue(ctx);
            Vector endValue = mode.getValue(ctx).equals("relative_end") ? startValue.add(end.getValue(ctx)) : end.getValue(ctx);

            int minX = Mth.floor(Math.max(-512, Math.min(startValue.getX(), endValue.getX())));
            int minY = Mth.floor(Math.max(ctx.evaluator.world.getMinHeight(), Math.min(startValue.getY(), endValue.getY())));
            int minZ = Mth.floor(Math.max(-512, Math.min(startValue.getZ(), endValue.getZ())));
            int maxX = Mth.floor(Math.min(511, Math.max(startValue.getX(), endValue.getX())));
            int maxY = Mth.floor(Math.min(ctx.evaluator.world.getMaxHeight() - 1, Math.max(startValue.getY(), endValue.getY())));
            int maxZ = Mth.floor(Math.min(511, Math.max(startValue.getZ(), endValue.getZ())));

            Vector[] val = new Vector[]{new Vector(minX, minY, minZ)};
            ctx.setScopeValue(current, val[0]);

            ctx.submit(new Runnable() {
                @Override
                public void run() {
                    val[0] = new Vector(val[0].getX() + 1, val[0].getY(), val[0].getZ());

                    if (val[0].getX() > maxX) {
                        val[0] = new Vector(minX, val[0].getY() + 1, val[0].getZ());
                    }

                    if (val[0].getY() > maxY) {
                        val[0] = new Vector(minX, minY, val[0].getZ() + 1);
                    }

                    if (val[0].getZ() > maxZ) {
                        ctx.sendSignal(next);
                        return;
                    }

                    ctx.setScopeValue(current, val[0]);
                    ctx.submit(this);
                    ctx.sendSignal(repeat);
                }
            });
            ctx.sendSignal(repeat);
        });
    }

    @Override
    public Node copy() {
        return new GridRepeatNode();
    }

}
