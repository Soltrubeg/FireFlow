package de.blazemcworld.fireflow.code.node.impl.event.world;

import de.blazemcworld.fireflow.code.CodeThread;
import de.blazemcworld.fireflow.code.EventContext;
import de.blazemcworld.fireflow.code.node.EventNode;
import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.ConditionType;
import de.blazemcworld.fireflow.code.type.PlayerType;
import de.blazemcworld.fireflow.code.type.SignalType;
import de.blazemcworld.fireflow.code.type.VectorType;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;

public class OnPlayerInteractBlockNode extends Node implements EventNode  {

    private final Output<Void> signal;
    private final Output<PlayerValue> player;
    private final Output<Vector> position;
    private final Output<Vector> side;
    private final Output<Boolean> isMainHand;

    public OnPlayerInteractBlockNode() {
        super("on_player_interact_block", "On Player Interact Block", "Emits a signal when a player attempts to interact with a block.", Material.OAK_BUTTON);

        signal = new Output<>("signal", "Signal", SignalType.INSTANCE);
        player = new Output<>("player", "Player", PlayerType.INSTANCE);
        position = new Output<>("position", "Position", VectorType.INSTANCE);
        side = new Output<>("side", "Side", VectorType.INSTANCE);
        isMainHand = new Output<>("is_main_hand", "Is Main Hand", ConditionType.INSTANCE);

        player.valueFromScope();
        position.valueFromScope();
        side.valueFromScope();
        isMainHand.valueFromScope();
    }

    @Override
    public Node copy() {
        return new OnPlayerInteractBlockNode();
    }

    @Override
    public void handleEvent(EventContext context) {
        if (!(context.event instanceof PlayerInteractEvent event && event.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
        CodeThread thread = context.newCodeThread();
        thread.setScopeValue(this.player, new PlayerValue(event.getPlayer()));
        thread.setScopeValue(this.position, event.getClickedBlock().getLocation().toVector());
        thread.setScopeValue(this.side, event.getBlockFace().getDirection());
        thread.setScopeValue(this.isMainHand, event.getHand() == EquipmentSlot.HAND);
        thread.sendSignal(signal);
        thread.clearQueue();
    }
}
