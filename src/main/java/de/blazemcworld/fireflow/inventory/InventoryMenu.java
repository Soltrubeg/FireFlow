package de.blazemcworld.fireflow.inventory;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;

public interface InventoryMenu extends InventoryHolder {

    void handleClick(InventoryClickEvent event);
    default void handleClose(InventoryCloseEvent event) {}

}