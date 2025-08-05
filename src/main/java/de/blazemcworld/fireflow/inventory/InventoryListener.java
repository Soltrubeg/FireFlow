package de.blazemcworld.fireflow.inventory;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof InventoryMenu menu) {
            event.setCancelled(true);
            menu.handleClick(event);
            return;
        }
        if (event.getClickedInventory() != null && event.getClickedInventory().getHolder() instanceof InventoryMenu menu) {
            event.setCancelled(true);
            menu.handleClick(event);
            return;
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() instanceof InventoryMenu menu) {
            menu.handleClose(event);
        }
    }

}
