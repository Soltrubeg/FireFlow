package de.blazemcworld.fireflow.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import de.blazemcworld.fireflow.code.VariableStore;
import de.blazemcworld.fireflow.space.Space;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.function.Predicate;

public class VariablesCommand {

    public static void attach(LiteralArgumentBuilder<CommandSourceStack> node) {
        attach(node, "variables");
        attach(node, "vars");
    }

    public static void attach(LiteralArgumentBuilder<CommandSourceStack> node, String alias) {
        node.then(Commands.literal(alias)
                .executes(ctx -> {
                    listVariables(ctx.getSource(), null);
                    return Command.SINGLE_SUCCESS;
                })
                .then(Commands.argument("filter", StringArgumentType.greedyString())
                        .executes(ctx -> {
                            listVariables(ctx.getSource(), StringArgumentType.getString(ctx, "filter"));
                            return Command.SINGLE_SUCCESS;
                        })
                )
        );
    }

    private static void listVariables(CommandSourceStack source, String query) {
        Player player = CommandHelper.getPlayer(source);
        Space space = CommandHelper.getSpace(player);
        if (!CommandHelper.isDeveloperOrOwner(player, space)) return;

        String lowerQuery = query == null ? null : query.toLowerCase();
        Predicate<String> filter = query == null ? s -> true : s -> s.toLowerCase().contains(lowerQuery);

        Set<VariableStore.VarEntry> vars = space.savedVariables.iterator(filter, 50);
        for (VariableStore.VarEntry v : vars) {
            player.sendMessage(Component.text(v.name()).color(v.type().color)
                    .append(Component.text(": ").color(NamedTextColor.GRAY))
                    .append(Component.text(v.type().stringify(v.value(), "display")).color(NamedTextColor.WHITE)));
        }

        if (vars.size() >= 50) return;

        for (VariableStore.VarEntry v : space.evaluator.sessionVariables.iterator(filter, 50 - vars.size())) {
            player.sendMessage(Component.text(v.name()).color(v.type().color)
                    .append(Component.text(": ").color(NamedTextColor.GRAY))
                    .append(Component.text(v.type().stringify(v.value(), "display")).color(NamedTextColor.WHITE)));
        }
    }

}
