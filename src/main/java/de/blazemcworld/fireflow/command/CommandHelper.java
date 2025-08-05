package de.blazemcworld.fireflow.command;

import de.blazemcworld.fireflow.space.Space;
import de.blazemcworld.fireflow.space.SpaceManager;
import de.blazemcworld.fireflow.util.ModeManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

public class CommandHelper {

    public static Player getPlayer(CommandSourceStack src) {
        if (!(src.getSender() instanceof Player player)) {
            src.getSender().sendMessage(Component.text("You must be a player for this!").color(NamedTextColor.RED));
            return null;
        }
        return player;
    }

    public static Space getSpace(Player player) {
        if (player == null) return null;
        Space space = SpaceManager.getSpaceForPlayer(player);
        if (space == null) {
            player.sendMessage(Component.text("You must be on a space for this!").color(NamedTextColor.RED));
            return null;
        }
        return space;
    }

    public static boolean isOwner(Player player, Space space) {
        if (space == null || player == null) return false;
        if (!space.info.owner.equals(player.getUniqueId())) {
            player.sendMessage(Component.text("You are not allowed to do that!").color(NamedTextColor.RED));
            return false;
        }
        return true;
    }
    
    public static boolean isDeveloperOrOwner(Player player, Space space) {
        if (space == null || player == null) return false;
        if (!space.info.isOwnerOrDeveloper(player.getUniqueId())) {
            player.sendMessage(Component.text("You are not allowed to do that!").color(NamedTextColor.RED));
            return false;
        }
        return true;
    }

    public static boolean isInCode(Player player, Space space) {
        if (space == null || player == null) return false;
        if (ModeManager.getFor(player) != ModeManager.Mode.CODE) {
            player.sendMessage(Component.text("You must be in code mode for this!").color(NamedTextColor.RED));
            return false;
        }
        return true;
    }

}
