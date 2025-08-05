package de.blazemcworld.fireflow.code.node.impl.variable;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.node.SingleGenericNode;
import de.blazemcworld.fireflow.code.type.WireType;
import org.bukkit.Material;

import java.util.List;
import java.util.Random;

public class RandomChoiceNode<T> extends SingleGenericNode<T> {

    private final Random rng = new Random();

    public RandomChoiceNode(WireType<T> type) {
        super("random_choice", type == null ? "Random Choice" : "Random " + type.getName() + " Choice", "Chooses a random value from the given options.", Material.TRIAL_KEY, type);

        Varargs<T> options = new Varargs<>("options", "Options", type);
        Output<T> chosen = new Output<>("chosen", "Chosen", type);

        chosen.valueFrom((ctx) -> {
            List<T> opt = options.getVarargs(ctx);
            if (opt.isEmpty()) return type.defaultValue();
            return opt.get(rng.nextInt(opt.size()));
        });
    }

    @Override
    public Node copy() {
        return new RandomChoiceNode<>(type);
    }

    @Override
    public Node copyWithType(WireType<?> type) {
        return new RandomChoiceNode<>(type);
    }
}
