package de.blazemcworld.fireflow.code.node.impl.player.inventory;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.ConditionType;
import de.blazemcworld.fireflow.code.type.ItemType;
import de.blazemcworld.fireflow.code.type.PlayerType;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

public class PlayerHasItemNode extends Node {
    public PlayerHasItemNode() {
        super("player_has_item", "Player has Item", "Checks if the player has a specific item in their inventory", Material.CHEST);

        Input<PlayerValue> player = new Input<>("player", "Player", PlayerType.INSTANCE);
        Input<ItemStack> item = new Input<>("item", "Item", ItemType.INSTANCE);
        Output<Boolean> found = new Output<>("found", "Found", ConditionType.INSTANCE);

        found.valueFrom((ctx) -> player.getValue(ctx).tryGet(ctx, p -> {
            ItemStack val = item.getValue(ctx);
            if (p.getInventory().contains(val)) return true;
            if (p.getOpenInventory().getType() == InventoryType.CRAFTING) {
                return p.getOpenInventory().getTopInventory().contains(val);
            }
            return false;
        }, false));
    }

    @Override
    public PlayerHasItemNode copy() {
        return new PlayerHasItemNode();
    }
}