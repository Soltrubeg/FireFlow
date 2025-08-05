package de.blazemcworld.fireflow.code.node.impl.vector;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.NumberType;
import de.blazemcworld.fireflow.code.type.VectorType;
import org.bukkit.Material;
import org.bukkit.util.Vector;

public class PackVectorNode extends Node {

    public PackVectorNode() {
        super("pack_vector", "Pack Vector", "Creates a new vector from its axes", Material.IRON_BLOCK);

        Input<Double> x = new Input<Double>("x", "X", NumberType.INSTANCE);
        Input<Double> y = new Input<Double>("y", "Y", NumberType.INSTANCE);
        Input<Double> z = new Input<Double>("z", "Z", NumberType.INSTANCE);
        Output<Vector> vector = new Output<>("vector", "Vector", VectorType.INSTANCE);

        vector.valueFrom(ctx -> new Vector(
                x.getValue(ctx),
                y.getValue(ctx),
                z.getValue(ctx)
        ));
    }

    @Override
    public Node copy() {
        return new PackVectorNode();
    }

}
