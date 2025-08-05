package de.blazemcworld.fireflow.code.type;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import de.blazemcworld.fireflow.FireFlow;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;

public class TextType extends WireType<Component> {

    public static final TextType INSTANCE = new TextType();

    private TextType() {
        super("text", NamedTextColor.LIGHT_PURPLE, Material.BOOK);
    }

    @Override
    public String getName() {
        return "Text";
    }

    @Override
    public Component defaultValue() {
        return Component.empty();
    }

    @Override
    public Component parseInset(String str) {
        return FireFlow.MM.deserialize(str);
    }

    @Override
    protected String stringifyInternal(Component value, String mode) {
        return switch (mode) {
            case "plain" -> getPlainContent(value);
            default -> FireFlow.MM.serialize(value);
        };
    }

    private static String getPlainContent(Component text) {
        StringBuilder out = new StringBuilder();
        if (text instanceof TextComponent plain) {
            out.append(plain.content());
        }
        for (Component child : text.children()) {
            out.append(getPlainContent(child));
        }
        return out.toString();
    }

    @Override
    public Component checkType(Object obj) {
        if (obj instanceof Component comp) return comp;
        return null;
    }

    @Override
    public JsonElement toJson(Component obj) {
        return new JsonPrimitive(FireFlow.MM.serialize(obj));
    }

    @Override
    public Component fromJson(JsonElement json) {
        return FireFlow.MM.deserialize(json.getAsString());
    }

    @Override
    public boolean valuesEqual(Component a, Component b) {
        return stringifyInternal(a, "display").equals(stringifyInternal(b, "display"));
    }

    @Override
    protected boolean canConvertInternal(WireType<?> other) {
        return AllTypes.isValue(other);
    }

    @Override
    protected Component convertInternal(WireType<?> other, Object v) {
        return Component.text(other.stringify(v, "display"));
    }
}
