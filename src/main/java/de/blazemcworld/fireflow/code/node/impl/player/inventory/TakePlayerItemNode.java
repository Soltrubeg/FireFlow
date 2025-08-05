package de.blazemcworld.fireflow.code.node.impl.player.inventory;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.ItemType;
import de.blazemcworld.fireflow.code.type.PlayerType;
import de.blazemcworld.fireflow.code.type.SignalType;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class TakePlayerItemNode extends Node {

    public TakePlayerItemNode() {
        super("take_player_item", "Take Player Item", "Takes an item from the player", Material.HOPPER_MINECART);

        Input<Void> signal = new Input<>("signal", "Signal", SignalType.INSTANCE);
        Input<PlayerValue> player = new Input<>("player", "Player", PlayerType.INSTANCE);
        Input<ItemStack> item = new Input<>("item", "Item", ItemType.INSTANCE);
        Output<Void> next = new Output<>("next", "Next", SignalType.INSTANCE);

        signal.onSignal((ctx) -> {
            player.getValue(ctx).tryUse(ctx, p -> {
                ItemStack stack = item.getValue(ctx);
                if (stack.isEmpty()) return;
                for (ItemStack overflow : p.getInventory().removeItemAnySlot(stack).values()) {
                    if (p.getOpenInventory().getType() == InventoryType.CRAFTING) {
                        p.getOpenInventory().getTopInventory().removeItemAnySlot(overflow);
                    }
                }
            });
            ctx.sendSignal(next);
        });
    }

    @Override
    public Node copy() {
        return new TakePlayerItemNode();
    }

}

