package de.blazemcworld.fireflow.code.type;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;

import java.util.UUID;

public class PlayerType extends WireType<PlayerValue> {

    public static final PlayerType INSTANCE = new PlayerType();

    private PlayerType() {
        super("player", NamedTextColor.GOLD, Material.PLAYER_HEAD);
    }

    @Override
    public PlayerValue defaultValue() {
        return new PlayerValue(UUID.fromString("00000000-0000-0000-0000-000000000000"));
    }

    @Override
    public PlayerValue checkType(Object obj) {
        if (obj instanceof PlayerValue player) return player;
        return null;
    }

    @Override
    public JsonElement toJson(PlayerValue obj) {
        return new JsonPrimitive(obj.uuid.toString());
    }

    @Override
    public PlayerValue fromJson(JsonElement json) {
        return new PlayerValue(UUID.fromString(json.getAsString()));
    }

    @Override
    public boolean valuesEqual(PlayerValue a, PlayerValue b) {
        return a.uuid.equals(b.uuid);
    }

    @Override
    public String getName() {
        return "Player";
    }

    @Override
    protected String stringifyInternal(PlayerValue value, String mode) {
        return value.uuid.toString();
    }
}
