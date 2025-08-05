package de.blazemcworld.fireflow.code.node.impl.item;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.node.option.EnchantmentOptions;
import de.blazemcworld.fireflow.code.type.ItemType;
import de.blazemcworld.fireflow.code.type.NumberType;
import de.blazemcworld.fireflow.code.type.StringType;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class EnchantItemNode extends Node {

    public EnchantItemNode() {
        super("enchant_item", "Enchant Item", "Adds an enchantment to an item. The enchantment will be removed if the level is less than or equal to 0, so without a level specified the enchantment will be removed.", Material.ENCHANTING_TABLE);

        Input<ItemStack> item = new Input<>("item", "Item", ItemType.INSTANCE);
        Input<String> enchantment = new Input<>("enchantment", "Enchantment", StringType.INSTANCE).options(EnchantmentOptions.INSTANCE);
        Input<Double> level = new Input<>("level", "Level", NumberType.INSTANCE);
        Output<ItemStack> updated = new Output<>("updated", "Updated", ItemType.INSTANCE);

        updated.valueFrom((ctx) -> {
            ItemStack i = item.getValue(ctx).clone();
            Enchantment ench = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).get(NamespacedKey.minecraft(enchantment.getValue(ctx)));
            if (ench == null) return i;

            i.addUnsafeEnchantment(ench, level.getValue(ctx).intValue());
            return i;
        });
    }

    @Override
    public Node copy() {
        return new EnchantItemNode();
    }
}
