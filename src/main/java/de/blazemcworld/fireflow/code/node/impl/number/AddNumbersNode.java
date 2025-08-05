package de.blazemcworld.fireflow.code.node.impl.number;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.NumberType;
import org.bukkit.Material;

public class AddNumbersNode extends Node {

    public AddNumbersNode() {
        super("add_numbers", "Add Numbers", "Adds multiple numbers together", Material.ANVIL);

        Input<Double> left = new Input<>("left", "Left", NumberType.INSTANCE);
        Varargs<Double> right = new Varargs<>("right", "Right", NumberType.INSTANCE);
        Output<Double> result = new Output<>("result", "Result", NumberType.INSTANCE);

        result.valueFrom((ctx) -> {
            double out = left.getValue(ctx);
            for (double v : right.getVarargs(ctx)) out += v;
            return out;
        });
    }

    @Override
    public Node copy() {
        return new AddNumbersNode();
    }
}
