package de.blazemcworld.fireflow.command;

import com.mojang.brigadier.Command;
import de.blazemcworld.fireflow.space.Space;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

public class ReloadCommand {

    public static void register(Commands cd) {
        cd.register(Commands.literal("reload")
                .executes(ctx -> {
                    Player player = CommandHelper.getPlayer(ctx.getSource());
                    Space space = CommandHelper.getSpace(player);
                    if (!CommandHelper.isDeveloperOrOwner(player, space)) return Command.SINGLE_SUCCESS;

                    space.reload();
                    player.sendMessage(Component.text("Reloaded space!").color(NamedTextColor.AQUA));
                    return Command.SINGLE_SUCCESS;
                })
                .then(Commands.literal("live")
                        .executes(ctx -> {
                            Player player = CommandHelper.getPlayer(ctx.getSource());
                            Space space = CommandHelper.getSpace(player);
                            if (!CommandHelper.isDeveloperOrOwner(player, space)) return Command.SINGLE_SUCCESS;

                            space.evaluator.liveReload();
                            player.sendMessage(Component.text("Live reloaded space!").color(NamedTextColor.AQUA));
                            return Command.SINGLE_SUCCESS;
                        })
                ).build()
        );
    }

}
