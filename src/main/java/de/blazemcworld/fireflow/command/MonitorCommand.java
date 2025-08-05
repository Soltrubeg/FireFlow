package de.blazemcworld.fireflow.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import de.blazemcworld.fireflow.FireFlow;
import de.blazemcworld.fireflow.space.Space;
import de.blazemcworld.fireflow.space.SpaceManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MonitorCommand {

    private static final Map<Player, Space> monitors = new ConcurrentHashMap<>();

    public static void attach(LiteralArgumentBuilder<CommandSourceStack> node) {
        node.then(Commands.literal("monitor")
                .executes(ctx -> {
                    Player player = CommandHelper.getPlayer(ctx.getSource());

                    if (player != null && monitors.containsKey(player)) {
                        monitors.remove(player);
                        player.sendMessage(Component.text("Stopped monitoring!").color(NamedTextColor.AQUA));
                        return Command.SINGLE_SUCCESS;
                    }

                    Space space = CommandHelper.getSpace(player);
                    if (!CommandHelper.isDeveloperOrOwner(player, space)) return Command.SINGLE_SUCCESS;

                    monitors.put(player, space);
                    player.sendMessage(Component.text("Now monitoring space #" + space.info.id).color(NamedTextColor.AQUA));
                    return Command.SINGLE_SUCCESS;
                })
        );

        Bukkit.getScheduler().runTaskTimer(FireFlow.instance, (s) -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                Space space = monitors.get(p);
                if (space == null) continue;

                if (SpaceManager.getSpaceForPlayer(p) != space || !space.info.isOwnerOrDeveloper(p.getUniqueId())) {
                    monitors.remove(p);
                    return;
                }

                int percent = space.evaluator.cpuPercentage();

                int red = (int) (percent * 2.55);
                int green = 255 - red;
                TextColor color = TextColor.color(red, green, 0);

                int bars = (int) (percent * 0.8);

                p.sendMessage(
                        Component.text("CPU ").color(color)
                                .append(Component.text("[").color(NamedTextColor.WHITE))
                                .append(Component.text("|".repeat(bars)).color(color))
                                .append(Component.text("|".repeat(80 - bars)).color(NamedTextColor.GRAY))
                                .append(Component.text("]").color(NamedTextColor.WHITE))
                                .append(Component.text(" " + percent + "%").color(color))
                );
            }
        }, 1, 1);
    }

}
