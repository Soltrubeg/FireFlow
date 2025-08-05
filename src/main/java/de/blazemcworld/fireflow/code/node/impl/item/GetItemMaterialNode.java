package de.blazemcworld.fireflow.code.node.impl.item;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.ItemType;
import de.blazemcworld.fireflow.code.type.StringType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class GetItemMaterialNode extends Node {

    public GetItemMaterialNode() {
        super("get_item_material", "Get Item Material", "Changes the type of an item", Material.BOOK);

        Input<ItemStack> item = new Input<>("item", "Item", ItemType.INSTANCE);
        Output<String> material = new Output<>("material", "Material", StringType.INSTANCE);

        material.valueFrom((ctx) -> item.getValue(ctx).getType().key().value());
    }

    @Override
    public Node copy() {
        return new GetItemMaterialNode();
    }
}
