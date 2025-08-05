package de.blazemcworld.fireflow.code.node.impl.event.action;

import de.blazemcworld.fireflow.code.CodeThread;
import de.blazemcworld.fireflow.code.EventContext;
import de.blazemcworld.fireflow.code.node.EventNode;
import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.ConditionType;
import de.blazemcworld.fireflow.code.type.PlayerType;
import de.blazemcworld.fireflow.code.type.SignalType;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class OnPlayerSwingHandNode extends Node implements EventNode {

    private final Output<Void> signal;
    private final Output<PlayerValue> player;
    private final Output<Boolean> isMainHand;

    public OnPlayerSwingHandNode() {
        super("on_player_swing_hand", "On Player Swing Hand", "Called when a player swings their hand, usually bound to left click.", Material.STONE_SWORD);

        signal = new Output<>("signal", "Signal", SignalType.INSTANCE);
        player = new Output<>("player", "Player", PlayerType.INSTANCE);
        isMainHand = new Output<>("is_main_hand", "Is Main Hand", ConditionType.INSTANCE);
        player.valueFromScope();
        isMainHand.valueFromScope();
    }

    @Override
    public Node copy() {
        return new OnPlayerSwingHandNode();
    }

    @Override
    public void handleEvent(EventContext context) {
        if (!(context.event instanceof PlayerInteractEvent event && (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK))) return;
        CodeThread thread = context.newCodeThread();
        thread.setScopeValue(this.player, new PlayerValue(event.getPlayer()));
        thread.setScopeValue(this.isMainHand, event.getHand() == EquipmentSlot.HAND);
        thread.sendSignal(signal);
        thread.clearQueue();
    }
}
