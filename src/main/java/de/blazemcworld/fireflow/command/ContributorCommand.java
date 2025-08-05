package de.blazemcworld.fireflow.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import de.blazemcworld.fireflow.FireFlow;
import de.blazemcworld.fireflow.space.Space;
import de.blazemcworld.fireflow.space.SpaceInfo;
import de.blazemcworld.fireflow.space.SpaceManager;
import de.blazemcworld.fireflow.util.ModeManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class ContributorCommand {

    public static void attach(LiteralArgumentBuilder<CommandSourceStack> node) {
        attach(node, "builder", info -> info.builders);
        attach(node, "developer", info -> info.developers);
    }

    private static void attach(LiteralArgumentBuilder<CommandSourceStack> node, String id, Function<SpaceInfo, Set<UUID>> getMap) {
        node.then(Commands.literal(id)
                .then(Commands.literal("list")
                        .executes(ctx -> {
                            Player player = CommandHelper.getPlayer(ctx.getSource());
                            Space space = CommandHelper.getSpace(player);
                            if (!CommandHelper.isOwner(player, space)) return Command.SINGLE_SUCCESS;

                            Set<UUID> contributors = getMap.apply(space.info);

                            if (contributors.isEmpty()) {
                                player.sendMessage(Component.text("There are no " + id + "s!").color(NamedTextColor.RED));
                                return Command.SINGLE_SUCCESS;
                            }

                            player.sendMessage(Component.text("Space " + id + (contributors.size() == 1 ? "" : "s") + " (" + contributors.size() + "):").color(NamedTextColor.AQUA));
                            for (UUID uuid : contributors) {
                                resolveName(uuid, name -> player.sendMessage(Component.text("- " + name).color(NamedTextColor.DARK_AQUA)));
                            }

                            return Command.SINGLE_SUCCESS;
                        })
                )
                .then(Commands.literal("add")
                        .then(Commands.argument("name", StringArgumentType.word())
                                .suggests((ctx, builder) -> {
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        builder.suggest(player.getName());
                                    }
                                    return builder.buildFuture();
                                })
                                .executes(ctx -> {
                                    Player player = CommandHelper.getPlayer(ctx.getSource());
                                    Space space = CommandHelper.getSpace(player);
                                    if (!CommandHelper.isOwner(player, space)) return Command.SINGLE_SUCCESS;

                                    String name = ctx.getArgument("name", String.class);

                                    if (player.getName().equalsIgnoreCase(name)) {
                                        player.sendMessage(Component.text("You are always a " + id + "!").color(NamedTextColor.RED));
                                        return Command.SINGLE_SUCCESS;
                                    }

                                    resolveUUID(name, uuid -> {
                                        if (uuid == null) {
                                            player.sendMessage(Component.text("Could not find player with name " + name).color(NamedTextColor.RED));
                                            return;
                                        }

                                        Set<UUID> contributors = getMap.apply(space.info);
                                        if (contributors.contains(uuid)) {
                                            player.sendMessage(Component.text("Player " + name + " is already a " + id).color(NamedTextColor.RED));
                                            return;
                                        }
                                        contributors.add(uuid);
                                        player.sendMessage(Component.text("Added " + name + " as " + id).color(NamedTextColor.AQUA));
                                    });

                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
                .then(Commands.literal("remove")
                        .then(Commands.argument("name", StringArgumentType.word())
                                .suggests((ctx, builder) -> {
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        builder.suggest(player.getName());
                                    }
                                    return builder.buildFuture();
                                })
                                .executes(ctx -> {
                                    Player player = CommandHelper.getPlayer(ctx.getSource());
                                    Space space = CommandHelper.getSpace(player);
                                    if (!CommandHelper.isOwner(player, space)) return Command.SINGLE_SUCCESS;

                                    String name = ctx.getArgument("name", String.class);

                                    if (player.getName().equalsIgnoreCase(name)) {
                                        player.sendMessage(Component.text("You cannot remove yourself!").color(NamedTextColor.RED));
                                        return Command.SINGLE_SUCCESS;
                                    }

                                    resolveUUID(name, uuid -> {
                                        if (uuid == null) {
                                            player.sendMessage(Component.text("Could not find player with name " + name).color(NamedTextColor.RED));
                                            return;
                                        }

                                        Set<UUID> contributors = getMap.apply(space.info);
                                        if (!contributors.contains(uuid)) {
                                            player.sendMessage(Component.text("Player " + name + " is not a " + id).color(NamedTextColor.RED));
                                            return;
                                        }
                                        contributors.remove(uuid);

                                        Player target = Bukkit.getPlayer(uuid);
                                        if (target != null && SpaceManager.getSpaceForPlayer(target) == space) {
                                            ModeManager.Mode mode = ModeManager.getFor(target);
                                            if (id.equals("builder") && mode == ModeManager.Mode.BUILD) {
                                                ModeManager.move(target, ModeManager.Mode.LOBBY, space);
                                            }
                                            if (id.equals("developer") && mode == ModeManager.Mode.CODE) {
                                                ModeManager.move(target, ModeManager.Mode.LOBBY, space);
                                            }
                                        }

                                        player.sendMessage(Component.text("Removed " + name + " as " + id).color(NamedTextColor.AQUA));
                                    });

                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
        );
    }

    private static void resolveName(UUID uuid, Consumer<String> callback) {
        Thread.startVirtualThread(() -> {
            String name = Bukkit.getOfflinePlayer(uuid).getName();
            if (name == null) name = "<" + uuid + ">";
            String result = name;
            Bukkit.getScheduler().runTask(FireFlow.instance, () -> callback.accept(result));
        });
    }

    private static void resolveUUID(String name, Consumer<UUID> callback) {
        Thread.startVirtualThread(() -> {
            OfflinePlayer player = Bukkit.getOfflinePlayer(name);
            Bukkit.getScheduler().runTask(FireFlow.instance, () -> callback.accept(player.hasPlayedBefore() ? player.getUniqueId() : null));
        });
    }
}
