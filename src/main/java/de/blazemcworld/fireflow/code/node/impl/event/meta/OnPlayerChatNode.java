package de.blazemcworld.fireflow.code.node.impl.event.meta;

import de.blazemcworld.fireflow.code.CodeThread;
import de.blazemcworld.fireflow.code.EventContext;
import de.blazemcworld.fireflow.code.node.EventNode;
import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.PlayerType;
import de.blazemcworld.fireflow.code.type.SignalType;
import de.blazemcworld.fireflow.code.type.StringType;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.Material;

public class OnPlayerChatNode extends Node implements EventNode {

    private final Output<Void> signal;
    private final Output<PlayerValue> player;
    private final Output<String> message;

    public OnPlayerChatNode() {
        super("on_player_chat", "On Player Chat", "Emits a signal when a player sends a chat message.", Material.NAME_TAG);

        signal = new Output<>("signal", "Signal", SignalType.INSTANCE);
        player = new Output<>("player", "Player", PlayerType.INSTANCE);
        message = new Output<>("message", "Message", StringType.INSTANCE);
        player.valueFromScope();
        message.valueFromScope();
    }


    @Override
    public Node copy() {
        return new OnPlayerChatNode();
    }

    @Override
    public void handleEvent(EventContext context) {
        if (context.event instanceof AsyncChatEvent event) {
            CodeThread thread = context.newCodeThread();
            thread.setScopeValue(player, new PlayerValue(event.getPlayer()));
            thread.setScopeValue(message, event.signedMessage().message());
            thread.sendSignal(signal);
            thread.clearQueue();
        }
    }
}