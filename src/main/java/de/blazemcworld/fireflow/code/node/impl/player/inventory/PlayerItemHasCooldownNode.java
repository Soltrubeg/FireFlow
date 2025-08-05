package de.blazemcworld.fireflow.code.node.impl.player.inventory;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.ConditionType;
import de.blazemcworld.fireflow.code.type.ItemType;
import de.blazemcworld.fireflow.code.type.PlayerType;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

public class PlayerItemHasCooldownNode extends Node {

    public PlayerItemHasCooldownNode() {
        super("player_item_has_cooldown", "Player Item Has Cooldown", 
            "Checks if a player item is on cooldown", Material.CLOCK);

        Input<PlayerValue> player = new Input<>("player", "Player", PlayerType.INSTANCE);
        Input<ItemStack> item = new Input<>("item", "Item", ItemType.INSTANCE);
        
        Output<Boolean> hasCooldown = new Output<>("has_cooldown", "Has Cooldown", ConditionType.INSTANCE);

        hasCooldown.valueFrom((ctx) -> {
            return player.getValue(ctx).tryGet(ctx, p -> {
                ItemStack stack = item.getValue(ctx);
                return p.hasCooldown(stack);
            }, false);
        });
    }

    @Override
    public Node copy() {
        return new PlayerItemHasCooldownNode();
    }
}
