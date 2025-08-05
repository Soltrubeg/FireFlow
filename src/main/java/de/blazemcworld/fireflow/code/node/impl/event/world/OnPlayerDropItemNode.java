package de.blazemcworld.fireflow.code.node.impl.event.world;

import de.blazemcworld.fireflow.code.CodeThread;
import de.blazemcworld.fireflow.code.EventContext;
import de.blazemcworld.fireflow.code.node.EventNode;
import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.PlayerType;
import de.blazemcworld.fireflow.code.type.SignalType;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerDropItemEvent;

public class OnPlayerDropItemNode extends Node implements EventNode {

    private final Output<Void> signal;
    private final Output<PlayerValue> player;

    public OnPlayerDropItemNode() {
        super("on_player_drop_item", "On Player Drop Item", "Called when a player drops an item.", Material.DROPPER);

        signal = new Output<>("signal", "Signal", SignalType.INSTANCE);
        player = new Output<>("player", "Player", PlayerType.INSTANCE);
        player.valueFromScope();
    }

    @Override
    public Node copy() {
        return new OnPlayerDropItemNode();
    }

    @Override
    public void handleEvent(EventContext context) {
        if (!(context.event instanceof PlayerDropItemEvent event)) return;
        CodeThread thread = context.newCodeThread();
        thread.setScopeValue(this.player, new PlayerValue(event.getPlayer()));
        thread.sendSignal(signal);
        thread.clearQueue();
    }
}
