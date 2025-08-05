package de.blazemcworld.fireflow.code.node.impl.event.combat.entity;

import de.blazemcworld.fireflow.code.CodeThread;
import de.blazemcworld.fireflow.code.EventContext;
import de.blazemcworld.fireflow.code.node.EventNode;
import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.EntityType;
import de.blazemcworld.fireflow.code.type.NumberType;
import de.blazemcworld.fireflow.code.type.PlayerType;
import de.blazemcworld.fireflow.code.type.SignalType;
import de.blazemcworld.fireflow.code.value.EntityValue;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;

public class OnPlayerKillEntityNode extends Node implements EventNode {

    private final Output<Void> signal;
    private final Output<PlayerValue> attacker;
    private final Output<EntityValue> victim;
    private final Output<Double> amount;

    public OnPlayerKillEntityNode() {
        super("on_player_kill_entity", "On Player Kill Entity", "Emits a signal when a player kills an entity.", Material.DIAMOND_SWORD);

        signal = new Output<>("signal", "Signal", SignalType.INSTANCE);
        attacker = new Output<>("attacker", "Attacker", PlayerType.INSTANCE);
        victim = new Output<>("victim", "Victim", EntityType.INSTANCE);
        amount = new Output<>("amount", "Damage Amount", NumberType.INSTANCE);
        attacker.valueFromScope();
        victim.valueFromScope();
        amount.valueFromScope();
    }

    @Override
    public void handleEvent(EventContext context) {
        if (!(context.event instanceof EntityDeathEvent e && e.getDamageSource().getCausingEntity() instanceof Player p && !(e.getEntity() instanceof Player))) return;

        CodeThread thread = context.newCodeThread();
        thread.setScopeValue(this.attacker, new PlayerValue(p));
        thread.setScopeValue(this.victim, new EntityValue(e.getEntity()));
        thread.setScopeValue(this.amount, e.getEntity().getLastDamage());
        thread.sendSignal(signal);
        thread.clearQueue();
    }

    @Override
    public Node copy() {
        return new OnPlayerKillEntityNode();
    }

}