package de.blazemcworld.fireflow.code.node.impl.vector;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.NumberType;
import de.blazemcworld.fireflow.code.type.VectorType;
import org.bukkit.Material;
import org.bukkit.util.Vector;

public class UnpackVectorNode extends Node {
    public UnpackVectorNode() {
        super("unpack_vector", "Unpack Vector", "Unpacks a vector into its components", Material.IRON_INGOT);

        Input<Vector> vector = new Input<>("vector", "Vector", VectorType.INSTANCE);
        Output<Double> x = new Output<>("x", "X", NumberType.INSTANCE);
        Output<Double> y = new Output<>("y", "Y", NumberType.INSTANCE);
        Output<Double> z = new Output<>("z", "Z", NumberType.INSTANCE);

        x.valueFrom(ctx -> vector.getValue(ctx).getX());
        y.valueFrom(ctx -> vector.getValue(ctx).getY());
        z.valueFrom(ctx -> vector.getValue(ctx).getZ());
    }

    @Override
    public Node copy() {
        return new UnpackVectorNode();
    }
}
