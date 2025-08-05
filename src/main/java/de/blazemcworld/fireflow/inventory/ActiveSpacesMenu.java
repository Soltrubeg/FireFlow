package de.blazemcworld.fireflow.inventory;

import de.blazemcworld.fireflow.FireFlow;
import de.blazemcworld.fireflow.space.Space;
import de.blazemcworld.fireflow.space.SpaceInfo;
import de.blazemcworld.fireflow.space.SpaceManager;
import de.blazemcworld.fireflow.util.ModeManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ActiveSpacesMenu implements InventoryMenu {

    private final Inventory inventory;
    public final List<SpaceInfo> infos = new ArrayList<>();

    private ActiveSpacesMenu() {
        inventory = Bukkit.createInventory(this, 9 * 3, Component.text("Active Spaces"));

        for (Space s : SpaceManager.activeSpaces()) {
            infos.add(s.info);
            if (infos.size() >= 27) break;
        }

        for (int i = 0; i < infos.size(); i++) {
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
    }

    public static void open(Player player) {
        player.openInventory(new ActiveSpacesMenu().getInventory());
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    @Override
    public void handleClick(InventoryClickEvent event) {
        event.setCancelled(true);
        if (event.getSlot() < 0 || event.getSlot() >= infos.size()) return;
        if (!(event.getWhoClicked() instanceof Player player)) return;
        SpaceInfo info = infos.get(event.getSlot());
        ModeManager.move(player, ModeManager.Mode.PLAY, SpaceManager.getOrLoadSpace(info));
    }

}
