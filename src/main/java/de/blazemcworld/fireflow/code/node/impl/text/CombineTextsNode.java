package de.blazemcworld.fireflow.code.node.impl.text;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.TextType;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

public class CombineTextsNode extends Node {
    public CombineTextsNode() {
        super("combine_texts", "Combine Texts", "Combines multiple texts into one", Material.SLIME_BALL);

        Varargs<Component> texts = new Varargs<>("texts", "Texts", TextType.INSTANCE);
        Output<Component> combined = new Output<>("combined", "Combined", TextType.INSTANCE);

        combined.valueFrom(ctx -> {
            Component out = Component.empty();
            for (Component text : texts.getVarargs(ctx)) {
                out = out.append(text);
            }
            return out;
        });
    }

    @Override
    public Node copy() {
        return new CombineTextsNode();
    }
}

