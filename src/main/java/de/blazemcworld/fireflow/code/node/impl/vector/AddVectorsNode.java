package de.blazemcworld.fireflow.code.node.impl.vector;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.VectorType;
import org.bukkit.Material;
import org.bukkit.util.Vector;

public class AddVectorsNode extends Node {

    public AddVectorsNode() {
        super("add_vectors", "Add Vectors", "Adds together each axis independently.", Material.ANVIL);

        Input<Vector> first = new Input<>("first", "First", VectorType.INSTANCE);
        Input<Vector> second = new Input<>("second", "Second", VectorType.INSTANCE);
        Output<Vector> result = new Output<>("result", "Result", VectorType.INSTANCE);

    	result.valueFrom(ctx -> first.getValue(ctx).add(second.getValue(ctx)));
    }

    @Override
    public Node copy() {
        return new AddVectorsNode();
    }
}
