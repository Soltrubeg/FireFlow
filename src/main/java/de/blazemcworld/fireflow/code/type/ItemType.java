package de.blazemcworld.fireflow.code.type;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import de.blazemcworld.fireflow.FireFlow;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Base64;
import java.util.logging.Level;

public class ItemType extends WireType<ItemStack> {

    public static final ItemType INSTANCE = new ItemType();

    private ItemType() {
        super("item", NamedTextColor.GRAY, Material.ITEM_FRAME);
    }

    @Override
    public ItemStack defaultValue() {
        return new ItemStack(Material.AIR);
    }

    @Override
    public ItemStack checkType(Object obj) {
        if (obj instanceof ItemStack item) return item;
        return null;
    }

    @Override
    public JsonElement toJson(ItemStack item) {
        if (item.isEmpty()) return JsonNull.INSTANCE;
        try {
            return new JsonPrimitive(new String(Base64.getEncoder().encode(item.serializeAsBytes())));
        } catch (Exception err) {
            FireFlow.logger.log(Level.WARNING, "Failed to serialize item", err);
            return JsonNull.INSTANCE;
        }
    }

    @Override
    public ItemStack fromJson(JsonElement json) {
        if (json.isJsonNull()) return new ItemStack(Material.AIR);
        try {
            return ItemStack.deserializeBytes(Base64.getDecoder().decode(json.getAsString()));
        } catch (Exception err) {
            FireFlow.logger.log(Level.WARNING, "Failed to deserialize item", err);
            return new ItemStack(Material.AIR);
        }
    }

    @Override
    public boolean valuesEqual(ItemStack a, ItemStack b) {
        return a.equals(b);
    }

    @Override
    public ItemStack parseInset(String str) {
        Material m = Material.getMaterial(str);
        if (m == null || !m.isItem()) return null;
        return new ItemStack(m);
    }

    @Override
    public String getName() {
        return "Item";
    }

    @Override
    protected String stringifyInternal(ItemStack value, String mode) {
        return switch (mode) {
            case "id", "type", "material" -> value.getType().key().value();
            case "count" -> String.valueOf(value.getAmount());
            default -> value.getType().key().value() + " x" + value.getAmount();
        };
    }

    @Override
    public boolean canConvert(WireType<?> other) {
        return other == StringType.INSTANCE;
    }

    @Override
    public ItemStack convert(WireType<?> other, Object v) {
        if (v instanceof String str) {
            ItemStack i = parseInset(str);
            if (i != null) return i;
        }
        return new ItemStack(Material.AIR);
    }
}
