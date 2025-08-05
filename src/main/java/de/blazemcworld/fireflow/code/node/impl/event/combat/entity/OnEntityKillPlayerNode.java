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
import org.bukkit.event.entity.PlayerDeathEvent;

public class OnEntityKillPlayerNode extends Node implements EventNode {

    private final Output<Void> signal;
    private final Output<EntityValue> attacker;
    private final Output<PlayerValue> victim;
    private final Output<Double> amount;

    public OnEntityKillPlayerNode() {
        super("on_entity_kill_player", "On Entity Kill Player", "Emits a signal when an entity kills a player.", Material.BOW);

        signal = new Output<>("signal", "Signal", SignalType.INSTANCE);
        attacker = new Output<>("attacker", "Attacker", EntityType.INSTANCE);
        victim = new Output<>("victim", "Victim", PlayerType.INSTANCE);
        amount = new Output<>("amount", "Amount", NumberType.INSTANCE);
        attacker.valueFromScope();
        victim.valueFromScope();
        amount.valueFromScope();
    }

    @Override
    public void handleEvent(EventContext context) {
        if (!(context.event instanceof PlayerDeathEvent e && e.getDamageSource().getCausingEntity() != null && !(e.getDamageSource().getCausingEntity() instanceof Player) && e.getEntity() instanceof Player victim)) return;

        CodeThread thread = context.newCodeThread();
        thread.setScopeValue(this.attacker, new EntityValue(e.getDamageSource().getCausingEntity()));
        thread.setScopeValue(this.victim, new PlayerValue(victim));
        thread.setScopeValue(this.amount, victim.getLastDamage());
        thread.sendSignal(signal);
        thread.clearQueue();
    }

    @Override
    public Node copy() {
        return new OnEntityKillPlayerNode();
    }

}
