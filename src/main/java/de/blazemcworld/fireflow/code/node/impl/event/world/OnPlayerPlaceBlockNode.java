package de.blazemcworld.fireflow.code.node.impl.event.world;

import de.blazemcworld.fireflow.code.CodeThread;
import de.blazemcworld.fireflow.code.EventContext;
import de.blazemcworld.fireflow.code.node.EventNode;
import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.ItemType;
import de.blazemcworld.fireflow.code.type.PlayerType;
import de.blazemcworld.fireflow.code.type.SignalType;
import de.blazemcworld.fireflow.code.type.VectorType;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import org.bukkit.Material;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class OnPlayerPlaceBlockNode extends Node implements EventNode {

    private final Output<Void> signal;
    private final Output<PlayerValue> player;
    private final Output<Vector> position;
    private final Output<ItemStack> item;

    public OnPlayerPlaceBlockNode() {
        super("on_player_place_block", "On Player Place Block", "Emits a signal when a player places a block.", Material.GOLDEN_SHOVEL);

        signal = new Output<>("signal", "Signal", SignalType.INSTANCE);
        player = new Output<>("player", "Player", PlayerType.INSTANCE);
        position = new Output<>("position", "Position", VectorType.INSTANCE);
        item = new Output<>("item", "Item", ItemType.INSTANCE);
        player.valueFromScope();
        position.valueFromScope();
        item.valueFromScope();
    }

    @Override
    public Node copy() {
        return new OnPlayerPlaceBlockNode();
    }

    @Override
    public void handleEvent(EventContext context) {
        if (!(context.event instanceof BlockPlaceEvent event)) return;
        CodeThread thread = context.newCodeThread();
        thread.setScopeValue(this.player, new PlayerValue(event.getPlayer()));
        thread.setScopeValue(this.position, event.getBlock().getLocation().toVector());
        thread.setScopeValue(this.item, event.getItemInHand());
        thread.sendSignal(signal);
        thread.clearQueue();
    }
}


