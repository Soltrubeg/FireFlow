package de.blazemcworld.fireflow.code.node.impl.event.action;

import de.blazemcworld.fireflow.code.CodeThread;
import de.blazemcworld.fireflow.code.EventContext;
import de.blazemcworld.fireflow.code.node.EventNode;
import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.PlayerType;
import de.blazemcworld.fireflow.code.type.SignalType;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class OnPlayerStopFlyingNode extends Node implements EventNode {

    private final Output<Void> signal;
    private final Output<PlayerValue> player;

    public OnPlayerStopFlyingNode() {
        super("on_player_stop_fly", "On Player Stop Fly", "Emits a signal when a player stops flying (not gliding with an elytra).", Material.ANDESITE);

        signal = new Output<>("signal", "Signal", SignalType.INSTANCE);
        player = new Output<>("player", "Player", PlayerType.INSTANCE);
        player.valueFromScope();
    }

    @Override
    public Node copy() {
        return new OnPlayerStopFlyingNode();
    }

    @Override
    public void handleEvent(EventContext context) {
        if (!(context.event instanceof PlayerToggleFlightEvent event && !event.isFlying())) return;
        CodeThread thread = context.newCodeThread();
        thread.setScopeValue(this.player, new PlayerValue(event.getPlayer()));
        thread.sendSignal(signal);
        thread.clearQueue();
    }
}
