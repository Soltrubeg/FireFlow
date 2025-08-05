package de.blazemcworld.fireflow.code.node.impl.item;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.ConditionType;
import de.blazemcworld.fireflow.code.type.ItemType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SetItemUnbreakableNode extends Node {

    public SetItemUnbreakableNode() {
        super("set_item_unbreakable", "Set Item Unbreakable", "Changes whether an item is unbreakable or not.", Material.NETHERITE_HOE);

        Input<ItemStack> item = new Input<>("item", "Item", ItemType.INSTANCE);
        Input<Boolean> unbreakable = new Input<>("unbreakable", "Unbreakable", ConditionType.INSTANCE);
        Output<ItemStack> updated = new Output<>("updated", "Updated", ItemType.INSTANCE);

        updated.valueFrom((ctx) -> {
            ItemStack i = item.getValue(ctx).clone();
            ItemMeta meta = i.getItemMeta();
            meta.setUnbreakable(unbreakable.getValue(ctx));
            i.setItemMeta(meta);
            return i;
        });
    }

    @Override
    public Node copy() {
        return new SetItemUnbreakableNode();
    }
}
