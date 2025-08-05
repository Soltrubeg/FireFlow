package de.blazemcworld.fireflow.code.node.impl.item;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.ItemType;
import de.blazemcworld.fireflow.code.type.TextType;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

public class SetItemNameNode extends Node {

    public SetItemNameNode() {
        super("set_item_name", "Set Item Name", "Renames an item", Material.NAME_TAG);

        Input<ItemStack> item = new Input<>("item", "Item", ItemType.INSTANCE);
        Input<Component> name = new Input<>("name", "Name", TextType.INSTANCE);
        Output<ItemStack> updated = new Output<>("updated", "Updated", ItemType.INSTANCE);

        updated.valueFrom((ctx) -> {
            ItemStack i = item.getValue(ctx).clone();
            ItemMeta meta = i.getItemMeta();
            meta.customName(name.getValue(ctx));
            i.setItemMeta(meta);
            return i;
        });
    }

    @Override
    public Node copy() {
        return new SetItemNameNode();
    }
}