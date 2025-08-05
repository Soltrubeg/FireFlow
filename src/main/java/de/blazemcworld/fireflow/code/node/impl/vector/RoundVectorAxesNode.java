package de.blazemcworld.fireflow.code.node.impl.vector;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.StringType;
import de.blazemcworld.fireflow.code.type.VectorType;
import org.bukkit.Material;
import org.bukkit.util.Vector;

public class RoundVectorAxesNode extends Node {

    public RoundVectorAxesNode() {
        super("round_vector_axes", "Round Vector Axes", "Rounds each axis of a vector", Material.CLAY_BALL);

        Input<Vector> vector = new Input<>("vector", "Vector", VectorType.INSTANCE);
        Input<String> mode = new Input<>("mode", "Mode", StringType.INSTANCE).options("Round", "Floor", "Ceil");
        Output<Vector> rounded = new Output<>("rounded", "Result", VectorType.INSTANCE);

        rounded.valueFrom(ctx -> {
            Vector v = vector.getValue(ctx);
            switch (mode.getValue(ctx)) {
                case "Round" -> {
                    return new Vector(
                        Math.round(v.getX()),
                        Math.round(v.getY()),
                        Math.round(v.getZ())
                    );
                }
                case "Floor" -> {
                    return new Vector(
                        Math.floor(v.getX()),
                        Math.floor(v.getY()),
                        Math.floor(v.getZ())
                    );
                }
                case "Ceil" -> {
                    return new Vector(
                        Math.ceil(v.getX()),
                        Math.ceil(v.getY()),
                        Math.ceil(v.getZ())
                    );
                }
            }
            return v;
        });
    }

    @Override
    public Node copy() {
        return new RoundVectorAxesNode();
    }

}

