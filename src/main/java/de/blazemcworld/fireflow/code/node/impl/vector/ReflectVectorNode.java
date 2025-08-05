package de.blazemcworld.fireflow.code.node.impl.vector;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.VectorType;
import org.bukkit.Material;
import org.bukkit.util.Vector;

public class ReflectVectorNode extends Node {

    public ReflectVectorNode() {
        super("reflect_vector", "Reflect Vector", "Reflects a vector, given a surface normal.", Material.GLASS_PANE);

        Input<Vector> vector = new Input<>("vector", "Vector", VectorType.INSTANCE);
        Input<Vector> normal = new Input<>("normal", "Normal", VectorType.INSTANCE);
        Output<Vector> reflected = new Output<>("reflected", "Reflected", VectorType.INSTANCE);

        reflected.valueFrom(ctx -> {
            Vector v = vector.getValue(ctx);
            Vector n = normal.getValue(ctx).normalize();
            return v.subtract(n.multiply(v.dot(n) * 2));
        });
    }

    @Override
    public Node copy() {
        return new ReflectVectorNode();
    }

}
