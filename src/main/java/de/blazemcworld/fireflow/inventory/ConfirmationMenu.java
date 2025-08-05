package de.blazemcworld.fireflow.inventory;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ConfirmationMenu implements InventoryMenu {

    private final Inventory inventory;
    private final Player player;
    private final Runnable confirm;
    private final Runnable cancel;
    private boolean isAnswered = false;

    private ConfirmationMenu(Player player, String message, Runnable confirm, Runnable cancel) {
        this.player = player;
        this.confirm = confirm == null ? () -> {} : confirm;
        this.cancel = cancel == null ? () -> {} : cancel;
        inventory = Bukkit.createInventory(this, 9 * 3, Component.text(message));

        ItemStack cancelBtn = new ItemStack(Material.REDSTONE_BLOCK);
        ItemMeta meta = cancelBtn.getItemMeta();
        meta.customName(Component.text("Cancel").color(NamedTextColor.RED));
        cancelBtn.setItemMeta(meta);

        ItemStack confirmBtn = new ItemStack(Material.EMERALD_BLOCK);
        meta = confirmBtn.getItemMeta();
        meta.customName(Component.text("Confirm").color(NamedTextColor.GREEN));
        confirmBtn.setItemMeta(meta);

        ItemStack questionStack = new ItemStack(Material.PAPER);
        meta = questionStack.getItemMeta();
        meta.customName(Component.text(message).color(NamedTextColor.WHITE));
        meta.lore(List.of(
                Component.text("Are you sure about this?").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY),
                Component.text("If unsure, press cancel or close this menu.").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY)
        ));
        questionStack.setItemMeta(meta);

        inventory.setItem(10, cancelBtn);
        inventory.setItem(13, questionStack);
        inventory.setItem(16, confirmBtn);
    }

    public static void open(Player player, String message, Runnable confirm, Runnable cancel) {
        player.openInventory(new ConfirmationMenu(player, message, confirm, cancel).getInventory());
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    @Override
    public void handleClick(InventoryClickEvent event) {
        if (event.getWhoClicked() != player) return;
        if (isAnswered) return;

        if (event.getSlot() == 11) {
            isAnswered = true;
            this.player.closeInventory();
            this.cancel.run();
            return;
        }

        if (event.getSlot() == 14) {
            isAnswered = true;
            this.player.closeInventory();
            this.confirm.run();
            return;
        }
    }

    @Override
    public void handleClose(InventoryCloseEvent event) {
        if (isAnswered) return;
        isAnswered = true;
        cancel.run();
    }

}
