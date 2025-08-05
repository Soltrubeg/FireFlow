package de.blazemcworld.fireflow.code.node.impl.number;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.ConditionType;
import de.blazemcworld.fireflow.code.type.NumberType;
import org.bukkit.Material;

public class LessThanNode extends Node {

    public LessThanNode() {
        super("less_than", "Less Than", "Checks if the left number is less than the right number", Material.APPLE);

        Input<Double> left = new Input<>("left", "Left", NumberType.INSTANCE);
        Input<Double> right = new Input<>("right", "Right", NumberType.INSTANCE);
        Output<Boolean> result = new Output<>("result", "Result", ConditionType.INSTANCE);

        result.valueFrom((ctx) -> left.getValue(ctx) < right.getValue(ctx));
    }

    @Override
    public Node copy() {
        return new LessThanNode();
    }
}
