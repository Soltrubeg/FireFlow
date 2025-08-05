package de.blazemcworld.fireflow.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import de.blazemcworld.fireflow.code.EditOrigin;
import de.blazemcworld.fireflow.space.Space;
import de.blazemcworld.fireflow.util.ModeManager;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

public class AddNodeCommand {

    public static void register(Commands cd) {
        register(cd, "add", false);
        register(cd, "add?", true);
    }

    private static void register(Commands cd, String id, boolean flag) {
        cd.register(Commands.literal(id)
                .then(Commands.argument("node", StringArgumentType.greedyString())
                        .executes(ctx -> {
                            Player player = CommandHelper.getPlayer(ctx.getSource());
                            Space space = CommandHelper.getSpace(player);
                            if (space == null) return Command.SINGLE_SUCCESS;

                            if (ModeManager.getFor(player) != ModeManager.Mode.CODE) {
                                player.sendMessage(Component.text("You must be in code mode to do that!").color(NamedTextColor.RED));
                                return Command.SINGLE_SUCCESS;
                            }

                            space.editor.addNode(EditOrigin.ofPlayer(player), StringArgumentType.getString(ctx, "node"), flag);

                            return Command.SINGLE_SUCCESS;
                        })
                ).build()
        );
    }


}
