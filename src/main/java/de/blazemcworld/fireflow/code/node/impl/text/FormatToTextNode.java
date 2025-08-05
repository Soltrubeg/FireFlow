package de.blazemcworld.fireflow.code.node.impl.text;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.StringType;
import de.blazemcworld.fireflow.code.type.TextType;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

public class FormatToTextNode extends Node {
    
    public FormatToTextNode() {
        super("format_to_text", "Format To Text", "Converts a string to a text", Material.DARK_OAK_SIGN);

        Input<String> input = new Input<>("string", "String", StringType.INSTANCE);
        Output<Component> output = new Output<>("text", "Text", TextType.INSTANCE);

        output.valueFrom((ctx) -> TextType.INSTANCE.parseInset(input.getValue(ctx)));
    }

    @Override
    public Node copy() {
        return new FormatToTextNode();
    }

}
