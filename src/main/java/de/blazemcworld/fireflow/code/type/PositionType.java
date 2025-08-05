package de.blazemcworld.fireflow.code.type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;

public class PositionType extends WireType<Location> {

    public static final PositionType INSTANCE = new PositionType();

    private PositionType() {
        super("position", NamedTextColor.DARK_PURPLE, Material.COMPASS);
    }

    @Override
    public Location defaultValue() {
        return new Location(null, 0, 0, 0);
    }

    @Override
    public Location checkType(Object obj) {
        if (obj instanceof Location p) return p;
        return null;
    }

    @Override
    public JsonElement toJson(Location pos) {
        JsonObject out = new JsonObject();
        out.addProperty("x", pos.getX());
        out.addProperty("y", pos.getY());
        out.addProperty("z", pos.getZ());
        out.addProperty("pitch", pos.getPitch());
        out.addProperty("yaw", pos.getYaw());
        return out;
    }

    @Override
    public Location fromJson(JsonElement json) {
        JsonObject obj = json.getAsJsonObject();
        return new Location(
                null,
                obj.get("x").getAsDouble(),
                obj.get("y").getAsDouble(),
                obj.get("z").getAsDouble(),
                obj.get("pitch").getAsFloat(),
                obj.get("yaw").getAsFloat()
        );
    }

    @Override
    public boolean valuesEqual(Location a, Location b) {
        return a.equals(b);
    }

    @Override
    protected String stringifyInternal(Location value, String mode) {
        return switch (mode) {
            case "x" -> "%.2f".formatted(value.getX());
            case "y" -> "%.2f".formatted(value.getY());
            case "z" -> "%.2f".formatted(value.getZ());
            case "pitch" -> "%.2f".formatted(value.getPitch());
            case "yaw" -> "%.2f".formatted(value.getYaw());
            default -> "(%.2f, %.2f, %.2f, %.2f, %.2f)".formatted(
                    value.getX(), value.getY(), value.getZ(),
                    value.getPitch(), value.getYaw()
            );
        };
    }

    @Override
    public String getName() {
        return "Position";
    }

    @Override
    protected boolean canConvertInternal(WireType<?> other) {
        return other == VectorType.INSTANCE;
    }

    @Override
    protected Location convertInternal(WireType<?> other, Object v) {
        if (other == VectorType.INSTANCE && v instanceof Vector vec) {
            return new Location(null, vec.getX(), vec.getY(), vec.getZ());
        }
        return super.convertInternal(other, v);
    }
}
