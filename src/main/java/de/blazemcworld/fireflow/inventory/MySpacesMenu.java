package de.blazemcworld.fireflow.inventory;

import de.blazemcworld.fireflow.FireFlow;
import de.blazemcworld.fireflow.space.SpaceInfo;
import de.blazemcworld.fireflow.space.SpaceManager;
import de.blazemcworld.fireflow.util.ModeManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;

public class MySpacesMenu implements InventoryMenu {

    private final Inventory inventory;
    public final List<SpaceInfo> infos;
    private final Player player;

    private MySpacesMenu(Player player) {
        this.player = player;
        inventory = Bukkit.createInventory(this, 9 * 3, Component.text("My Spaces"));

        infos = SpaceManager.getOwnedSpaces(player);

        for (int i = 0; i < Math.min(infos.size(), 16); i++) {
            SpaceInfo info = infos.get(i);

            ItemStack item = new ItemStack(info.icon);
            ItemMeta meta = item.getItemMeta();
            meta.customName(FireFlow.MM.deserialize(info.name).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            meta.lore(List.of(
                    Component.text("by " + Bukkit.getOfflinePlayer(info.owner).getName()).decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY),
                    Component.text("Players: " + info.playerCount()).decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY),
                    Component.text("ID: " + info.id).decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY)
            ));
            item.setItemMeta(meta);
            inventory.setItem(i, item);
        }

        if (infos.size() < 5) {
            inventory.setItem(26, createSpaceItem());
        }
    }

    public static void open(Player player) {
        player.openInventory(new MySpacesMenu(player).getInventory());
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    @Override
    public void handleClick(InventoryClickEvent event) {
        event.setCancelled(true);
        if (event.getWhoClicked() != player) return;

        if (event.getSlot() == 26 && infos.size() < 5) {
            SpaceInfo info = new SpaceInfo(SpaceManager.lastId++);
            info.name = player.getName() + "'s New Space";
            info.icon = Material.PAPER;
            info.owner = player.getUniqueId();
            info.developers = new HashSet<>();
            info.builders = new HashSet<>();
            infos.add(info);
            SpaceManager.info.put(info.id, info);
            MySpacesMenu.open((Player) event.getWhoClicked());
            return;
        }

        if (event.getSlot() < 0 || event.getSlot() >= infos.size()) return;
        SpaceInfo info = infos.get(event.getSlot());
        ModeManager.move(player, ModeManager.Mode.PLAY, SpaceManager.getOrLoadSpace(info));
    }

    private static ItemStack createSpaceItem() {
        ItemStack item = new ItemStack(Material.GREEN_STAINED_GLASS);
        ItemMeta meta = item.getItemMeta();
        meta.customName(Component.text("Create Space").color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        meta.lore(List.of(
                Component.text("Click to create").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY),
                Component.text("a new space.").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY)
        ));
        item.setItemMeta(meta);
        return item;
    }
}
