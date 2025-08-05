package de.blazemcworld.fireflow.code.node.impl.event.action;

import de.blazemcworld.fireflow.code.CodeThread;
import de.blazemcworld.fireflow.code.EventContext;
import de.blazemcworld.fireflow.code.node.EventNode;
import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.PlayerType;
import de.blazemcworld.fireflow.code.type.SignalType;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class OnPlayerStartSneakingNode extends Node implements EventNode {

    private final Output<Void> signal;
    private final Output<PlayerValue> player;

    public OnPlayerStartSneakingNode() {
        super("on_player_start_sneaking", "On Player Start Sneaking", "Emits a signal when a player starts to sneak.", Material.GOLDEN_LEGGINGS);

        signal = new Output<>("signal", "Signal", SignalType.INSTANCE);
        player = new Output<>("player", "Player", PlayerType.INSTANCE);
        player.valueFromScope();
    }

    @Override
    public void handleEvent(EventContext context) {
        if (!(context.event instanceof PlayerToggleSneakEvent event && event.isSneaking())) return;
        CodeThread thread = context.newCodeThread();
        thread.setScopeValue(this.player, new PlayerValue(event.getPlayer()));
        thread.sendSignal(signal);
        thread.handleEvent();
    }

    @Override
    public Node copy() {
        return new OnPlayerStartSneakingNode();
    }

}
