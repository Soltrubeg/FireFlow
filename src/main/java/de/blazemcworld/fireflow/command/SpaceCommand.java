package de.blazemcworld.fireflow.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import de.blazemcworld.fireflow.inventory.ConfirmationMenu;
import de.blazemcworld.fireflow.space.Space;
import de.blazemcworld.fireflow.space.SpaceManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.entity.Player;

public class SpaceCommand {

    public static void register(Commands cd) {
        LiteralArgumentBuilder<CommandSourceStack> node = Commands.literal("space")
                .then(Commands.literal("icon")
                        .then(Commands.argument("icon", StringArgumentType.greedyString())
                                .suggests((ctx, builder) -> {
                                    for (NamespacedKey id : Registry.ITEM.keyStream().toList()) {
                                        builder.suggest(id.getKey());
                                    }
                                    return builder.buildFuture();
                                })
                                .executes(ctx -> {
                                    Player player = CommandHelper.getPlayer(ctx.getSource());
                                    Space space = CommandHelper.getSpace(player);
                                    if (!CommandHelper.isOwner(player, space)) return Command.SINGLE_SUCCESS;

                                    Material m = Material.getMaterial(StringArgumentType.getString(ctx, "icon"));
                                    if (m != null && m.isItem()) {
                                        space.info.icon = m;
                                        player.sendMessage(Component.text("Changed space icon!").color(NamedTextColor.AQUA));
                                        return Command.SINGLE_SUCCESS;
                                    }

                                    player.sendMessage(Component.text("Invalid icon!").color(NamedTextColor.RED));
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
                .then(Commands.literal("name")
                        .then(Commands.argument("name", StringArgumentType.greedyString())
                                .executes(ctx -> {
                                    Player player = CommandHelper.getPlayer(ctx.getSource());
                                    Space space = CommandHelper.getSpace(player);
                                    if (!CommandHelper.isOwner(player, space)) return Command.SINGLE_SUCCESS;

                                    String name = StringArgumentType.getString(ctx, "name");
                                    if (name.length() > 256) {
                                        player.sendMessage(Component.text("Name too long!").color(NamedTextColor.RED));
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    space.info.name = name;

                                    player.sendMessage(Component.text("Changed space name!").color(NamedTextColor.AQUA));
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
                .then(Commands.literal("delete")
                        .executes(ctx -> {
                            Player player = CommandHelper.getPlayer(ctx.getSource());
                            Space space = CommandHelper.getSpace(player);
                            if (!CommandHelper.isOwner(player, space)) return Command.SINGLE_SUCCESS;

                            ConfirmationMenu.open(player, "Delete this space?", () -> {
                                SpaceManager.delete(space);
                                player.sendMessage(Component.text("Deleted space!").color(NamedTextColor.AQUA));
                            }, null);

                            return Command.SINGLE_SUCCESS;
                        })
                );
        MonitorCommand.attach(node);
        ContributorCommand.attach(node);
        VariablesCommand.attach(node);
        cd.register(node.build());
    }

}
