package de.blazemcworld.fireflow.code.node.impl.item;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.ItemType;
import de.blazemcworld.fireflow.code.type.ListType;
import de.blazemcworld.fireflow.code.type.StringType;
import de.blazemcworld.fireflow.code.value.ListValue;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GetHiddenItemInfoNode extends Node {

    public GetHiddenItemInfoNode() {
        super("get_hidden_item_info", "Get Hidden Item Info", "Returns which of the items components are hidden from the lore.", Material.CHISELED_BOOKSHELF);

        Input<ItemStack> item = new Input<>("item", "Item", ItemType.INSTANCE);
        Output<ListValue<String>> hidden = new Output<>("hidden_info", "Hidden", ListType.of(StringType.INSTANCE));

        hidden.valueFrom(ctx -> {
            List<String> list = new ArrayList<>();
            for (ItemFlag flag : item.getValue(ctx).getItemMeta().getItemFlags()) {
                list.add(flag.name().toLowerCase());
            }
            return new ListValue<>(StringType.INSTANCE, list);
        });
    }

    @Override
    public Node copy() {
        return new GetHiddenItemInfoNode();
    }
}
