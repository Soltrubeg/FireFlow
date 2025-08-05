package de.blazemcworld.fireflow.code.node.impl.item;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.ListType;
import de.blazemcworld.fireflow.code.type.StringType;
import de.blazemcworld.fireflow.code.value.ListValue;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;

import java.util.ArrayList;
import java.util.List;

public class ItemIdListNode extends Node {

    private static final ListValue<String> constant = new ListValue<>(StringType.INSTANCE, collect());

    private static List<String> collect() {
        List<String> list = new ArrayList<>();
        for (NamespacedKey id : Registry.ITEM.keyStream().toList()) {
            list.add(id.getKey());
        }
        return list;
    }

    public ItemIdListNode() {
        super("item_id_list", "Item ID List", "Returns a list of all item ids in minecraft.", Material.KNOWLEDGE_BOOK);

        Output<ListValue<String>> list = new Output<>("list", "List", ListType.of(StringType.INSTANCE));
        list.valueFrom(ctx -> constant);
    }

    @Override
    public Node copy() {
        return new ItemIdListNode();
    }

}
