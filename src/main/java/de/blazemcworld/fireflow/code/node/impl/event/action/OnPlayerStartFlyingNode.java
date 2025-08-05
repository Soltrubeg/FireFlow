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

public class OnPlayerStartFlyingNode extends Node implements EventNode {

    private final Output<Void> signal;
    private final Output<PlayerValue> player;

    public OnPlayerStartFlyingNode() {
        super("on_player_start_fly", "On Player Start Fly", "Emits a signal when a player starts flying (not gliding with an elytra).", Material.FEATHER);

        signal = new Output<>("signal", "Signal", SignalType.INSTANCE);
        player = new Output<>("player", "Player", PlayerType.INSTANCE);
        player.valueFromScope();
    }

    @Override
    public Node copy() {
        return new OnPlayerStartFlyingNode();
    }

    @Override
    public void handleEvent(EventContext context) {
        if (!(context.event instanceof PlayerToggleFlightEvent event && event.isFlying())) return;
        CodeThread thread = context.newCodeThread();
        thread.setScopeValue(this.player, new PlayerValue(event.getPlayer()));
        thread.sendSignal(signal);
        thread.clearQueue();
    }
}
