package de.blazemcworld.fireflow.code.node.impl.player.inventory;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.ItemType;
import de.blazemcworld.fireflow.code.type.ListType;
import de.blazemcworld.fireflow.code.type.PlayerType;
import de.blazemcworld.fireflow.code.value.ListValue;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GetPlayerInventoryNode extends Node {

    public GetPlayerInventoryNode() {
        super("get_player_inventory", "Get Player Inventory", "Gets a list of all items in the player's main inventory. Does not include the offhand, armor or crafting slots.", Material.BARREL);

        Input<PlayerValue> player = new Input<>("player", "Player", PlayerType.INSTANCE);
        Output<ListValue<ItemStack>> main = new Output<>("main", "Main", ListType.of(ItemType.INSTANCE));

        main.valueFrom((ctx) -> player.getValue(ctx).tryGet(ctx, p -> {
                    List<ItemStack> items = new ArrayList<>();
                    for (ItemStack item : p.getInventory()) {
                        items.add(item.clone());
                    }
                    return new ListValue<>(ItemType.INSTANCE, items);
                }, new ListValue<>(ItemType.INSTANCE, new ArrayList<>())
        ));
    }

    @Override
    public Node copy() {
        return new GetPlayerInventoryNode();
    }

}
