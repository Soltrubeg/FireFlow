package de.blazemcworld.fireflow.code.node.impl.event.action;

import de.blazemcworld.fireflow.code.CodeThread;
import de.blazemcworld.fireflow.code.EventContext;
import de.blazemcworld.fireflow.code.node.EventNode;
import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.PlayerType;
import de.blazemcworld.fireflow.code.type.SignalType;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class OnPlayerSwapHandsNode extends Node implements EventNode {

    private final Output<Void> signal;
    private final Output<PlayerValue> player;

    public OnPlayerSwapHandsNode() {
        super("on_player_swap_hands", "On Player Swap Hands", "Emits a signal when a player swaps their hand Material.", Material.SHIELD);

        signal = new Output<>("signal", "Signal", SignalType.INSTANCE);
        player = new Output<>("player", "Player", PlayerType.INSTANCE);
        player.valueFromScope();
    }

    @Override
    public void handleEvent(EventContext context) {
        if (!(context.event instanceof PlayerSwapHandItemsEvent event)) return;
        CodeThread thread = context.newCodeThread();
        thread.setScopeValue(this.player, new PlayerValue(event.getPlayer()));
        thread.sendSignal(signal);
        thread.clearQueue();
    }

    @Override
    public Node copy() {
        return new OnPlayerSwapHandsNode();
    }
}

