package de.blazemcworld.fireflow.code.type;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import de.blazemcworld.fireflow.code.value.EntityValue;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;

import java.util.UUID;

public class EntityType extends WireType<EntityValue> {

    public static final EntityType INSTANCE = new EntityType();

    private EntityType() {
        super("entity", TextColor.color(0x2ba181), Material.ZOMBIE_HEAD);
    }

    @Override
    public EntityValue defaultValue() {
        return new EntityValue((Entity) null);
    }

    @Override
    public EntityValue checkType(Object obj) {
        if (obj instanceof EntityValue entity) return entity;
        return null;
    }

    @Override
    public String getName() {
        return "Entity";
    }

    @Override
    public JsonElement toJson(EntityValue obj) {
        return new JsonPrimitive(obj.uuid.toString());
    }

    @Override
    public EntityValue fromJson(JsonElement json) {
        return new EntityValue(UUID.fromString(json.getAsString()));
    }

    @Override
    public boolean valuesEqual(EntityValue a, EntityValue b) {
        return a.uuid.equals(b.uuid);
    }

    @Override
    protected String stringifyInternal(EntityValue value, String mode) {
        return value.uuid.toString();
    }
}