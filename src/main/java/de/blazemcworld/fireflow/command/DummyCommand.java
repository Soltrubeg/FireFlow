package de.blazemcworld.fireflow.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import de.blazemcworld.fireflow.space.Space;
import de.blazemcworld.fireflow.util.DummyPlayer;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

public class DummyCommand {

    public static void register(Commands cd) {
        cd.register(Commands.literal("dummy")
                .then(Commands.literal("spawn")
                        .then(Commands.argument("id", IntegerArgumentType.integer(1, 5))
                                .executes(ctx -> {
                                    int id = IntegerArgumentType.getInteger(ctx, "id");
                                    Player player = CommandHelper.getPlayer(ctx.getSource());
                                    Space space = CommandHelper.getSpace(player);
                                    if (!CommandHelper.isDeveloperOrOwner(player, space)) return Command.SINGLE_SUCCESS;

                                    if (space.dummyManager.getDummy(id) != null) {
                                        player.sendMessage(Component.text("That dummy has already been spawned!").color(NamedTextColor.RED));
                                        return Command.SINGLE_SUCCESS;
                                    }

                                    space.dummyManager.spawnDummy(id);
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
                .then(Commands.literal("remove")
                        .then(Commands.argument("id", IntegerArgumentType.integer(1, 5))
                                .executes(ctx -> {
                                    int id = IntegerArgumentType.getInteger(ctx, "id");
                                    Player player = CommandHelper.getPlayer(ctx.getSource());
                                    Space space = CommandHelper.getSpace(player);
                                    if (!CommandHelper.isDeveloperOrOwner(player, space)) return Command.SINGLE_SUCCESS;

                                    DummyPlayer dummy = space.dummyManager.getDummy(id);
                                    if (dummy == null) {
                                        player.sendMessage(Component.text("That dummy has not been spawned!").color(NamedTextColor.RED));
                                        return Command.SINGLE_SUCCESS;
                                    }

                                    dummy.discard();
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                ).build()
        );
    }

}
