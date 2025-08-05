package de.blazemcworld.fireflow.code.node.impl.item;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.ItemType;
import de.blazemcworld.fireflow.code.type.ListType;
import de.blazemcworld.fireflow.code.type.StringType;
import de.blazemcworld.fireflow.code.value.ListValue;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SetHiddenItemInfoNode extends Node {

    public SetHiddenItemInfoNode() {
        super("set_hidden_item_info", "Set Hidden Item Info", "Changes which of the items components are hidden from the lore.", Material.BOOKSHELF);

        Input<ItemStack> item = new Input<>("item", "Item", ItemType.INSTANCE);
        Input<ListValue<String>> hidden = new Input<>("hidden_", "Hidden", ListType.of(StringType.INSTANCE));
        Output<ItemStack> result = new Output<>("result", "Result", ItemType.INSTANCE);

        result.valueFrom(ctx -> {
            ItemStack i = item.getValue(ctx).clone();
            ItemMeta meta = i.getItemMeta();
            meta.removeItemFlags(meta.getItemFlags().toArray(new ItemFlag[0]));
            for (String s : hidden.getValue(ctx).view()) {
                try {
                    meta.addItemFlags(ItemFlag.valueOf(s.toUpperCase()));
                } catch (IllegalArgumentException ignore) {
                }
            }
            i.setItemMeta(meta);
            return i;
        });
    }

    @Override
    public Node copy() {
        return new SetHiddenItemInfoNode();
    }
}
