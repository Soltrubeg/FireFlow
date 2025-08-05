package de.blazemcworld.fireflow.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import de.blazemcworld.fireflow.space.Space;
import de.blazemcworld.fireflow.space.SpaceInfo;
import de.blazemcworld.fireflow.space.SpaceManager;
import de.blazemcworld.fireflow.util.ModeManager;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

public class BuildCommand {

    public static void register(Commands cd) {
        cd.register(Commands.literal("build")
                .executes(ctx -> {
                    Player player = CommandHelper.getPlayer(ctx.getSource());
                    Space space = CommandHelper.getSpace(player);
                    if (space == null) return Command.SINGLE_SUCCESS;

                    if (!space.info.isOwnerOrBuilder(player.getUniqueId())) {
                        player.sendMessage(Component.text("You are not allowed to do that!").color(NamedTextColor.RED));
                        return Command.SINGLE_SUCCESS;
                    }

                    ModeManager.move(player, ModeManager.Mode.BUILD, space);
                    return Command.SINGLE_SUCCESS;
                })
                .then(Commands.argument("id", IntegerArgumentType.integer())
                        .executes(ctx -> {
                            Player player = CommandHelper.getPlayer(ctx.getSource());
                            if (player == null) return Command.SINGLE_SUCCESS;

                            int id = IntegerArgumentType.getInteger(ctx, "id");
                            SpaceInfo info = SpaceManager.getInfo(id);
                            if (info == null) {
                                player.sendMessage(Component.text("Could not find space with id " + id + "!").color(NamedTextColor.RED));
                                return Command.SINGLE_SUCCESS;
                            }

                            if (!info.isOwnerOrBuilder(player.getUniqueId())) {
                                player.sendMessage(Component.text("You are not allowed to do that!").color(NamedTextColor.RED));
                                return Command.SINGLE_SUCCESS;
                            }

                            ModeManager.move(player, ModeManager.Mode.BUILD, SpaceManager.getOrLoadSpace(info));
                            return Command.SINGLE_SUCCESS;
                        })
                ).build()
        );
    }

}
