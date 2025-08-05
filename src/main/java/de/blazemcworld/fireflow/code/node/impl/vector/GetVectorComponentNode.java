package de.blazemcworld.fireflow.code.node.impl.vector;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.NumberType;
import de.blazemcworld.fireflow.code.type.StringType;
import de.blazemcworld.fireflow.code.type.VectorType;
import org.bukkit.Material;
import org.bukkit.util.Vector;

public class GetVectorComponentNode extends Node {
    public GetVectorComponentNode() {
        super("get_vector_component", "Get Vector Component", "Gets a specific axis of a vector", Material.DARK_PRISMARINE);
        Input<Vector> vector = new Input<>("vector", "Vector", VectorType.INSTANCE);
        Input<String> axis = new Input<>("axis", "Axis", StringType.INSTANCE).options("X", "Y", "Z");
        Output<Double> output = new Output<>("output", "Output", NumberType.INSTANCE);

        output.valueFrom((ctx -> {
            Vector inputVector = vector.getValue(ctx);
            return switch (axis.getValue(ctx)) {
                case "X" -> inputVector.getX();
                case "Y" -> inputVector.getY();
                case "Z" -> inputVector.getZ();
                default -> 0.0;
            };
        }));
    }

    @Override
    public Node copy() {
        return new GetVectorComponentNode();
    }
}