package de.blazemcworld.fireflow.code.node.impl.item;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.ItemType;
import de.blazemcworld.fireflow.code.type.ListType;
import de.blazemcworld.fireflow.code.type.TextType;
import de.blazemcworld.fireflow.code.value.ListValue;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class SetItemLoreNode extends Node {

    public SetItemLoreNode() {
        super("set_item_lore", "Set Item Lore", "Changes the description of an item", Material.WRITABLE_BOOK);

        Input<ItemStack> item = new Input<>("item", "Item", ItemType.INSTANCE);
        Input<ListValue<Component>> lore = new Input<>("lore", "Lore", ListType.of(TextType.INSTANCE));
        Output<ItemStack> updated = new Output<>("updated", "Updated", ItemType.INSTANCE);

        updated.valueFrom((ctx) -> {
            ItemStack i = item.getValue(ctx).clone();
            ItemMeta meta = i.getItemMeta();
            meta.lore(new ArrayList<>(lore.getValue(ctx).view()));
            i.setItemMeta(meta);
            return i;
        });
    }

    @Override
    public Node copy() {
        return new SetItemLoreNode();
    }
}