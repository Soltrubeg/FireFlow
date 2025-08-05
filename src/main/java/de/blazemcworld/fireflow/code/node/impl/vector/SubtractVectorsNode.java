package de.blazemcworld.fireflow.code.node.impl.vector;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.VectorType;
import org.bukkit.Material;
import org.bukkit.util.Vector;

public class SubtractVectorsNode extends Node {

    public SubtractVectorsNode() {
        super("subtract_vectors", "Subtract Vectors", "Subtracts the second vector from the first.", Material.ANVIL);

        Input<Vector> first = new Input<>("first", "First", VectorType.INSTANCE);
        Input<Vector> second = new Input<>("second", "Second", VectorType.INSTANCE);
        Output<Vector> result = new Output<>("result", "Result", VectorType.INSTANCE);

    	result.valueFrom(ctx -> first.getValue(ctx).subtract(second.getValue(ctx)));
    }

    @Override
    public Node copy() {
        return new SubtractVectorsNode();
    }
}
