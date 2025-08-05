package de.blazemcworld.fireflow.code.type;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;

public class ConditionType extends WireType<Boolean> {

    public static final ConditionType INSTANCE = new ConditionType();

    private ConditionType() {
        super("condition", NamedTextColor.BLUE, Material.COMPARATOR);
    }

    @Override
    public String getName() {
        return "Condition";
    }

    @Override
    public Boolean defaultValue() {
        return false;
    }

    @Override
    public Boolean parseInset(String str) {
        if (str.equalsIgnoreCase("true")) return true;
        if (str.equalsIgnoreCase("false")) return false;
        return null;
    }

    @Override
    protected String stringifyInternal(Boolean value, String mode) {
        return String.valueOf(value);
    }

    @Override
    public Boolean checkType(Object obj) {
        if (obj instanceof Boolean b) return b;
        return null;
    }

    @Override
    public JsonElement toJson(Boolean obj) {
        return new JsonPrimitive(obj);
    }

    @Override
    public Boolean fromJson(JsonElement json) {
        return json.getAsBoolean();
    }

    @Override
    public boolean valuesEqual(Boolean a, Boolean b) {
        return a.equals(b);
    }
}
