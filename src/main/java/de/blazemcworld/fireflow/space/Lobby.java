package de.blazemcworld.fireflow.space;

import de.blazemcworld.fireflow.FireFlow;
import de.blazemcworld.fireflow.inventory.ActiveSpacesMenu;
import de.blazemcworld.fireflow.inventory.MySpacesMenu;
import de.blazemcworld.fireflow.util.WorldUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Lobby implements Listener {

    public static World world;
    public static Location spawnPos;

    public static void init() {
        world = Bukkit.getWorld("world");
        if (world == null) throw new IllegalStateException("Failed to get default world!");
        WorldUtil.init(world);

        spawnPos = FireFlow.instance.getConfig().getLocation("spawn-pos");
        if (spawnPos == null) spawnPos = new Location(world, 0, 0, 0);
        spawnPos.setWorld(world);
    }

    private static ItemStack mySpacesItem() {
        ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = item.getItemMeta();
        meta.customName(Component.text("My Spaces").color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.ITALIC, false));
        meta.lore(List.of(
                Component.text("Manage your spaces").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY),
                Component.text("using this item.").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY)
        ));
        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack activeSpacesItem() {
        ItemStack item = new ItemStack(Material.BLAZE_POWDER);
        ItemMeta meta = item.getItemMeta();
        meta.customName(Component.text("Active Spaces").color(NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false));
        meta.lore(List.of(
                Component.text("View currently played on").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY),
                Component.text("spaces using this item.").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY)
        ));
        item.setItemMeta(meta);
        return item;
    }

    public static void onSpawn(Player player) {
        player.getInventory().setItem(0, mySpacesItem());
        player.getInventory().setItem(4, activeSpacesItem());
        player.setInvulnerable(true);

        if (spawnPos != null) {
            player.teleport(spawnPos);
        }
    }

    @EventHandler
    public void onUseItem(PlayerInteractEvent event) {
        if (event.getPlayer().getWorld() != world) return;

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (mySpacesItem().equals(event.getItem())) {
                MySpacesMenu.open(event.getPlayer());
                return;
            }
            if (activeSpacesItem().equals(event.getItem())) {
                ActiveSpacesMenu.open(event.getPlayer());
                return;
            }
        }
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent event) {
        if (event.getPlayer().getWorld() != world) return;
        event.setCancelled(true);
    }
}
