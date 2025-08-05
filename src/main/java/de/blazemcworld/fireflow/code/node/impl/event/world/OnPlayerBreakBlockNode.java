package de.blazemcworld.fireflow.code.node.impl.event.world;

import de.blazemcworld.fireflow.code.CodeThread;
import de.blazemcworld.fireflow.code.EventContext;
import de.blazemcworld.fireflow.code.node.EventNode;
import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.PlayerType;
import de.blazemcworld.fireflow.code.type.SignalType;
import de.blazemcworld.fireflow.code.type.VectorType;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.util.Vector;

public class OnPlayerBreakBlockNode extends Node implements EventNode {

    private final Output<Void> signal;
    private final Output<PlayerValue> player;
    private final Output<Vector> position;

    public OnPlayerBreakBlockNode() {
        super("on_player_break_block", "On Player Break Block", "Emits a signal when a player breaks a block.", Material.IRON_PICKAXE);

        signal = new Output<>("signal", "Signal", SignalType.INSTANCE);
        player = new Output<>("player", "Player", PlayerType.INSTANCE);
        position = new Output<>("position", "Position", VectorType.INSTANCE);

        player.valueFromScope();
        position.valueFromScope();
    }

    @Override
    public Node copy() {
        return new OnPlayerBreakBlockNode();
    }

    @Override
    public void handleEvent(EventContext context) {
        if (!(context.event instanceof BlockBreakEvent event)) return;
        CodeThread thread = context.newCodeThread();
        thread.setScopeValue(this.player, new PlayerValue(event.getPlayer()));
        thread.setScopeValue(this.position, event.getBlock().getLocation().toVector());
        thread.sendSignal(signal);
        thread.clearQueue();
    }
}
