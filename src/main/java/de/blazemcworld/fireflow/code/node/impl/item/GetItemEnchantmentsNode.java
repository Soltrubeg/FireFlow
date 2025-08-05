package de.blazemcworld.fireflow.code.node.impl.item;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.DictionaryType;
import de.blazemcworld.fireflow.code.type.ItemType;
import de.blazemcworld.fireflow.code.type.NumberType;
import de.blazemcworld.fireflow.code.type.StringType;
import de.blazemcworld.fireflow.code.value.DictionaryValue;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class GetItemEnchantmentsNode extends Node {

    public GetItemEnchantmentsNode() {
        super("get_item_enchantments", "Get Item Enchantments", "Returns all enchantments of an item.", Material.ENCHANTED_BOOK);

        Input<ItemStack> item = new Input<>("item", "Item", ItemType.INSTANCE);
        Output<DictionaryValue<String, Double>> enchantments = new Output<>("enchantments", "Enchantments", DictionaryType.of(StringType.INSTANCE, NumberType.INSTANCE));

        Registry<@NotNull Enchantment> reg = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);

        enchantments.valueFrom((ctx) -> {
            ItemStack i = item.getValue(ctx);
            HashMap<String, Double> out = new HashMap<>();
            for (Map.Entry<Enchantment, Integer> entry : i.getEnchantments().entrySet()) {
                out.put(reg.getKey(entry.getKey()).getKey(), entry.getValue().doubleValue());
            }

            return new DictionaryValue<>(StringType.INSTANCE, NumberType.INSTANCE, out);
        });
    }

    @Override
    public Node copy() {
        return new GetItemEnchantmentsNode();
    }

}
