package de.blazemcworld.fireflow.util;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

public class Statistics {

    public static void reset(Player player) {
        player.clearActivePotionEffects();
        player.setFoodLevel(20);
        player.setSaturation(20);
        player.setHealth(20);
        player.setGameMode(GameMode.ADVENTURE);
        player.setFlying(false);
        player.setAllowFlight(false);
        player.closeInventory();
        player.getInventory().clear();
        if (player.getOpenInventory().getTopInventory().getType() == InventoryType.CRAFTING) {
            player.getOpenInventory().getTopInventory().clear();
        }
        player.setExperienceLevelAndProgress(0);
        player.setDeathScreenScore(0);
        player.getEnderChest().clear();
        player.setLastDeathLocation(null);
        player.setInvulnerable(false);
        player.setInvisible(false);
    }

}
