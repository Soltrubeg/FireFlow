package de.blazemcworld.fireflow.code.node.impl.event.meta;

import de.blazemcworld.fireflow.code.CodeThread;
import de.blazemcworld.fireflow.code.EventContext;
import de.blazemcworld.fireflow.code.node.EventNode;
import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.PlayerType;
import de.blazemcworld.fireflow.code.type.SignalType;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class OnPlayerLeaveNode extends Node implements EventNode {

    private final Output<Void> signal;
    private final Output<PlayerValue> player;

    public OnPlayerLeaveNode() {
        super("on_player_leave", "On Player Leave", "Emits a signal when a player leaves.", Material.IRON_DOOR);

        signal = new Output<>("signal", "Signal", SignalType.INSTANCE);
        player = new Output<>("player", "Player", PlayerType.INSTANCE);
        player.valueFromScope();
    }

    @Override
    public Node copy() {
        return new OnPlayerLeaveNode();
    }
    @Override
    public void handleEvent(EventContext context) {
        if (context.customEvent instanceof LeaveEvent(Player p)) {
            CodeThread thread = context.newCodeThread();
            thread.setScopeValue(player, new PlayerValue(p));
            thread.sendSignal(signal);
            thread.clearQueue();
        }
    }

    public record LeaveEvent(Player player) {}
}
