package de.blazemcworld.fireflow.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import de.blazemcworld.fireflow.space.Space;
import de.blazemcworld.fireflow.space.SpaceManager;
import de.blazemcworld.fireflow.util.ModeManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class LocateCommand {

    public static void register(Commands cd) {
        register(cd, "locate");
        register(cd, "find");
    }

    private static void register(Commands cd, String alias) {
        cd.register(Commands.literal(alias)
                .executes(ctx -> {
                    Player target = CommandHelper.getPlayer(ctx.getSource());
                    return target == null ? Command.SINGLE_SUCCESS : locateAndRespond(target, ctx);
                })
                .then(Commands.argument("player", StringArgumentType.word())
                        .suggests((ctx, builder) -> {
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                builder.suggest(player.getName());
                            }
                            return builder.buildFuture();
                        })
                        .executes(ctx -> {
                            Player target = Bukkit.getPlayer(StringArgumentType.getString(ctx, "player"));
                            if (target == null) {
                                ctx.getSource().getSender().sendMessage(Component.text("Player not found!").color(NamedTextColor.RED));
                                return Command.SINGLE_SUCCESS;
                            }

                            return locateAndRespond(target, ctx);
                        })).build()
        );
    }

    /**
     * Locates the target player and sends a response to the command source
     * @param target The player to locate
     * @param ctx The command context
     * @return The success code, hardcoded to <code>Command.SINGLE_SUCCESS</code>
     */
    private static int locateAndRespond(Player target, CommandContext<CommandSourceStack> ctx) {
        Space space = SpaceManager.getSpaceForPlayer(target);
        ModeManager.Mode mode = ModeManager.getFor(target);

        switch (mode) {
            case LOBBY: {
                ctx.getSource().getSender().sendMessage(Component.text(
                        target.getName() + " is currently in the lobby."
                ).color(NamedTextColor.GREEN));
                break;
            }
            case PLAY: {
                ctx.getSource().getSender().sendMessage(Component.text(
                        target.getName() + " is currently playing on space #" + space.info.id
                ).color(NamedTextColor.GREEN));
                break;
            }
            case CODE: {
                ctx.getSource().getSender().sendMessage(Component.text(
                        target.getName() + " is currently coding on space #" + space.info.id
                ).color(NamedTextColor.GREEN));
                break;
            }
            case BUILD: {
                ctx.getSource().getSender().sendMessage(Component.text(
                        target.getName() + " is currently building on space #" + space.info.id
                ).color(NamedTextColor.GREEN));
                break;
            }
        }

        return Command.SINGLE_SUCCESS;
    }

}
