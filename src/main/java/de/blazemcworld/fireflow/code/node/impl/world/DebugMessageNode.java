package de.blazemcworld.fireflow.code.node.impl.world;

import com.google.gson.JsonObject;
import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.ConditionType;
import de.blazemcworld.fireflow.code.type.SignalType;
import de.blazemcworld.fireflow.code.type.StringType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class DebugMessageNode extends Node {

    public DebugMessageNode() {
        super("debug_message", "Debug Message", "Sends a message to all developers on the space.", Material.NETHER_STAR);

        Input<Void> signal = new Input<>("signal", "Signal", SignalType.INSTANCE);
        Input<Boolean> disable = new Input<>("disable", "Disable", ConditionType.INSTANCE);
        Varargs<String> info = new Varargs<>("info", "Info", StringType.INSTANCE);
        Output<Void> next = new Output<>("next", "Next", SignalType.INSTANCE);

        signal.onSignal((ctx) -> {
            if (disable.getValue(ctx)) {
                ctx.sendSignal(next);
                return;
            }

            StringBuilder out = new StringBuilder();
            for (String msg : info.getVarargs(ctx)) {
                out.append(msg).append(" ");
            }
            out.setLength(out.length() - 1);

            Component msg = Component.text("Debug: ").color(NamedTextColor.AQUA).append(Component.text(out.toString()).color(NamedTextColor.DARK_AQUA));
            for (Player player : ctx.evaluator.space.getPlayers()) {
                if (ctx.evaluator.space.info.isOwnerOrDeveloper(player.getUniqueId())) {
                    player.sendMessage(msg);
                }
            }
            JsonObject json = new JsonObject();
            json.addProperty("type", "info");
            json.addProperty("message", "Debug: " + out);
            ctx.evaluator.space.editor.webBroadcast(json);
            ctx.sendSignal(next);
        });
    }

    @Override
    public Node copy() {
        return new DebugMessageNode();
    }
}
