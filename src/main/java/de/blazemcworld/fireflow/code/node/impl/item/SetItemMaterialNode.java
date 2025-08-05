package de.blazemcworld.fireflow.code.node.impl.item;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.ItemType;
import de.blazemcworld.fireflow.code.type.StringType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class SetItemMaterialNode extends Node {

    public SetItemMaterialNode() {
        super("set_item_material", "Set Item Material", "Changes the type of an item", Material.PAPER);

        Input<ItemStack> item = new Input<>("item", "Item", ItemType.INSTANCE);
        Input<String> material = new Input<>("material", "Material", StringType.INSTANCE);
        Output<ItemStack> updated = new Output<>("updated", "Updated", ItemType.INSTANCE);

        updated.valueFrom((ctx) -> {
            ItemStack i = item.getValue(ctx);
            Material m = Material.getMaterial(material.getValue(ctx));
            if (m == null || !m.isItem()) return i;
            return i.withType(m);
        });
    }

    @Override
    public Node copy() {
        return new SetItemMaterialNode();
    }
}
