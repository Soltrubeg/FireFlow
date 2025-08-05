package de.blazemcworld.fireflow.code.node.impl.item;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.ConditionType;
import de.blazemcworld.fireflow.code.type.ItemType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

public class ItemsEqualNode extends Node {
    public ItemsEqualNode() {
        super("items_equal", "Items Equal", "Checks if two items are equal", Material.ANVIL);

        Input<ItemStack> first = new Input<>("first", "First", ItemType.INSTANCE);
        Input<ItemStack> second = new Input<>("second", "Second", ItemType.INSTANCE);
        Input<Boolean> checkCount = new Input<>("check_count", "Check Count", ConditionType.INSTANCE);
        Output<Boolean> isCase = new Output<>("case", "Case", ConditionType.INSTANCE);

        isCase.valueFrom((ctx) -> {
            ItemStack firstItem = first.getValue(ctx);
            ItemStack secondItem = second.getValue(ctx);
            return checkCount.getValue(ctx) ? firstItem.equals(secondItem) : firstItem.isSimilar(secondItem);
        });
    }

    @Override
    public Node copy() {
        return new ItemsEqualNode();
    }
}