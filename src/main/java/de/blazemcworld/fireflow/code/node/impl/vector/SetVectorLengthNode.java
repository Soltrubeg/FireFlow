package de.blazemcworld.fireflow.code.node.impl.vector;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.NumberType;
import de.blazemcworld.fireflow.code.type.VectorType;
import org.bukkit.Material;
import org.bukkit.util.Vector;

public class SetVectorLengthNode extends Node {
    public SetVectorLengthNode() {
        super("set_vector_length", "Set Vector Length", "Changes the length of a vector", Material.SHEARS);
        Input<Vector> vector = new Input<>("vector", "Vector", VectorType.INSTANCE);
        Input<Double> length = new Input<>("length", "Length", NumberType.INSTANCE);
        Output<Vector> scaled = new Output<>("scaled", "Scaled", VectorType.INSTANCE);

        scaled.valueFrom(ctx -> {
            Vector v = vector.getValue(ctx);
            double l = length.getValue(ctx);
            return v.normalize().multiply(l);
        });
    }

    @Override
    public Node copy() {
        return new SetVectorLengthNode();
    }
}
