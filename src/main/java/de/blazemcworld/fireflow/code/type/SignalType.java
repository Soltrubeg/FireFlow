package de.blazemcworld.fireflow.code.type;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;

public class SignalType extends WireType<Void> {

    public static final SignalType INSTANCE = new SignalType();

    private SignalType() {
        super("signal", NamedTextColor.AQUA, Material.REDSTONE);
    }

    @Override
    public String getName() {
        return "Signal";
    }

    @Override
    public Void defaultValue() {
        return null;
    }

    @Override
    public Void checkType(Object obj) {
        return null;
    }

    @Override
    public JsonElement toJson(Void obj) {
        return JsonNull.INSTANCE;
    }

    @Override
    public Void fromJson(JsonElement json) {
        return null;
    }

    @Override
    public boolean valuesEqual(Void a, Void b) {
        return false;
    }

    @Override
    protected String stringifyInternal(Void value, String mode) {
        return "SIGNAL";
    }

}
