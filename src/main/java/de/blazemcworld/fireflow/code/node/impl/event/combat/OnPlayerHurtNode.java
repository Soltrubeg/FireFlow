package de.blazemcworld.fireflow.code.node.impl.event.combat;

import de.blazemcworld.fireflow.code.CodeThread;
import de.blazemcworld.fireflow.code.EventContext;
import de.blazemcworld.fireflow.code.node.EventNode;
import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.NumberType;
import de.blazemcworld.fireflow.code.type.PlayerType;
import de.blazemcworld.fireflow.code.type.SignalType;
import de.blazemcworld.fireflow.code.type.StringType;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

public class OnPlayerHurtNode extends Node implements EventNode {

    private final Output<Void> signal;
    private final Output<PlayerValue> player;
    private final Output<Double> amount;
    private final Output<String> type;

    public OnPlayerHurtNode() {
        super("on_player_hurt", "On Player Hurt", "Emits a signal when a player is about to take damage.", Material.REDSTONE);

        signal = new Output<>("signal", "Signal", SignalType.INSTANCE);
        player = new Output<>("player", "Player", PlayerType.INSTANCE);
        amount = new Output<>("amount", "Damage Amount", NumberType.INSTANCE);
        type = new Output<>("type", "Damage Type", StringType.INSTANCE);
        player.valueFromScope();
        amount.valueFromScope();
        type.valueFromScope();
    }

    @Override
    public void handleEvent(EventContext context) {
        if (!(context.event instanceof EntityDamageEvent e && e.getEntity() instanceof Player p)) return;

        CodeThread thread = context.newCodeThread();
        thread.setScopeValue(this.player, new PlayerValue(p));
        thread.setScopeValue(this.amount, e.getDamage());
        thread.sendSignal(signal);
        thread.clearQueue();
    }

    @Override
    public Node copy() {
        return new OnPlayerHurtNode();
    }
}

