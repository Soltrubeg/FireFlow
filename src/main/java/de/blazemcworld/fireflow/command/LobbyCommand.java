package de.blazemcworld.fireflow.command;

import com.mojang.brigadier.Command;
import de.blazemcworld.fireflow.space.Lobby;
import de.blazemcworld.fireflow.util.ModeManager;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.util.List;

public class LobbyCommand {

    public static void register(Commands commands) {
        commands.register(Commands.literal("lobby")
                .executes(ctx -> {
                    Player player = CommandHelper.getPlayer(ctx.getSource());
                    if (player == null) return Command.SINGLE_SUCCESS;

                    if (player.getWorld() == Lobby.world) {
                        player.sendMessage(Component.text("You are already in the lobby!").color(NamedTextColor.RED));
                        return Command.SINGLE_SUCCESS;
                    }

                    ModeManager.move(player, ModeManager.Mode.LOBBY, null);
                    return Command.SINGLE_SUCCESS;
                }).build(), null, List.of("spawn")
        );
    }

}
