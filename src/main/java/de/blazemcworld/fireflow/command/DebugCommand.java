package de.blazemcworld.fireflow.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import de.blazemcworld.fireflow.code.EditOrigin;
import de.blazemcworld.fireflow.space.Space;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.Player;

public class DebugCommand {

    public static void register(Commands dispatcher) {
        dispatcher.register(Commands.literal("debug")
                .then(Commands.argument("id", StringArgumentType.greedyString())
                        .executes(ctx -> {
                            Player player = CommandHelper.getPlayer(ctx.getSource());
                            Space space = CommandHelper.getSpace(player);
                            if (!CommandHelper.isDeveloperOrOwner(player, space)) return Command.SINGLE_SUCCESS;

                            space.evaluator.nextTick(() -> {
                                space.evaluator.triggerDebug(StringArgumentType.getString(ctx, "id"), EditOrigin.ofPlayer(player));
                            });
                            return Command.SINGLE_SUCCESS;
                        })
                ).build()
        );
    }

}
