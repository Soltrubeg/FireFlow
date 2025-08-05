package de.blazemcworld.fireflow.code.type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;

public class VectorType extends WireType<Vector> {

    public static final VectorType INSTANCE = new VectorType();

    private VectorType() {
        super("vector", NamedTextColor.RED, Material.ARROW);
    }

    @Override
    public Vector defaultValue() {
        return new Vector();
    }

    @Override
    public Vector checkType(Object obj) {
        if (obj instanceof Vector p) return p;
        return null;
    }

    @Override
    public JsonElement toJson(Vector vec) {
        JsonObject out = new JsonObject();
        out.addProperty("x", vec.getX());
        out.addProperty("y", vec.getY());
        out.addProperty("z", vec.getZ());
        return out;
    }

    @Override
    public Vector fromJson(JsonElement json) {
        JsonObject obj = json.getAsJsonObject();
        return new Vector(
                obj.get("x").getAsDouble(),
                obj.get("y").getAsDouble(),
                obj.get("z").getAsDouble()
        );
    }

    @Override
    public String getName() {
        return "Vector";
    }

    @Override
    public boolean valuesEqual(Vector a, Vector b) {
        return a.equals(b);
    }

    @Override
    protected String stringifyInternal(Vector value, String mode) {
        return switch (mode) {
            case "x" -> "%.2f".formatted(value.getX());
            case "y" -> "%.2f".formatted(value.getY());
            case "z" -> "%.2f".formatted(value.getZ());
            default -> "<%.2f, %.2f, %.2f>".formatted(
                    value.getX(), value.getY(), value.getZ()
            );
        };
    }

    @Override
    protected boolean canConvertInternal(WireType<?> other) {
        return other == PositionType.INSTANCE;
    }

    @Override
    protected Vector convertInternal(WireType<?> other, Object v) {
        if (other == PositionType.INSTANCE && v instanceof Location pos) {
            return pos.toVector();
        }
        return super.convertInternal(other, v);
    }
}
