package de.blazemcworld.fireflow.code.node.impl.vector;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.NumberType;
import de.blazemcworld.fireflow.code.type.StringType;
import de.blazemcworld.fireflow.code.type.VectorType;
import org.bukkit.Material;
import org.bukkit.util.Vector;

public class SetVectorComponentNode extends Node {
    public SetVectorComponentNode() {
        super("set_vector_component", "Set Vector Component", "Changes a single axis of a vector", Material.PRISMARINE_SHARD);
        Input<Vector> vector = new Input<>("vector", "Vector", VectorType.INSTANCE);
        Input<String> axis = new Input<>("axis", "Axis", StringType.INSTANCE).options("X", "Y", "Z");
        Input<Double> value = new Input<>("value", "Value", NumberType.INSTANCE);
        Output<Vector> output = new Output<>("output", "Output", VectorType.INSTANCE);

        output.valueFrom((ctx) -> {
            Vector outputVec = vector.getValue(ctx).clone();
            double outputValue = value.getValue(ctx);
            switch (axis.getValue(ctx)) {
                case "X" -> outputVec.setX(outputValue);
                case "Y" -> outputVec.setY(outputValue);
                case "Z" -> outputVec.setZ(outputValue);
            };
            return outputVec;
        });
    }


    @Override
    public Node copy() {
        return new SetVectorComponentNode();
    }
}