package de.blazemcworld.fireflow.code.node.impl.item;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.ItemType;
import de.blazemcworld.fireflow.code.type.NumberType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

public class SetItemCountNode extends Node {

    public SetItemCountNode() {
        super("set_item_count", "Set Item Count", "Sets the count of an item", Material.BUNDLE);

        Input<ItemStack> item = new Input<>("item", "Item", ItemType.INSTANCE);
        Input<Double> count = new Input<>("count", "Count", NumberType.INSTANCE);
        Output<ItemStack> updated = new Output<>("updated", "Updated", ItemType.INSTANCE);

        updated.valueFrom((ctx) -> {
            ItemStack i = item.getValue(ctx).clone();
            i.setAmount(count.getValue(ctx).intValue());
            return i;
        });
    }

    @Override
    public Node copy() {
        return new SetItemCountNode();
    }

}
