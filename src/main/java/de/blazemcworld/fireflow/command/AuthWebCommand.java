package de.blazemcworld.fireflow.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import de.blazemcworld.fireflow.space.Space;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.Player;

public class AuthWebCommand {

    public static void register(Commands cd) {
        cd.register(Commands.literal("authorize-web")
                .then(Commands.argument("id", StringArgumentType.greedyString())
                        .executes(ctx -> {
                            Player player = CommandHelper.getPlayer(ctx.getSource());
                            Space space = CommandHelper.getSpace(player);
                            if (!CommandHelper.isDeveloperOrOwner(player, space)) return Command.SINGLE_SUCCESS;

                            space.editor.authorizeWeb(StringArgumentType.getString(ctx, "id"), player);
                            return Command.SINGLE_SUCCESS;
                        })
                ).build()
        );
    }

}
