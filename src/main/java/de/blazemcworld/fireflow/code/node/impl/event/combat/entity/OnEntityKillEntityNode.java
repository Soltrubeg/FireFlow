package de.blazemcworld.fireflow.code.node.impl.event.combat.entity;

import de.blazemcworld.fireflow.code.CodeThread;
import de.blazemcworld.fireflow.code.EventContext;
import de.blazemcworld.fireflow.code.node.EventNode;
import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.EntityType;
import de.blazemcworld.fireflow.code.type.NumberType;
import de.blazemcworld.fireflow.code.type.SignalType;
import de.blazemcworld.fireflow.code.value.EntityValue;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;

public class OnEntityKillEntityNode extends Node implements EventNode {

    private final Output<Void> signal;
    private final Output<EntityValue> attacker;
    private final Output<EntityValue> victim;
    private final Output<Double> amount;

    public OnEntityKillEntityNode() {
        super("on_entity_kill_entity", "On Entity Kill Entity", "Emits a signal when an entity kills an entity.", Material.CROSSBOW);

        signal = new Output<>("signal", "Signal", SignalType.INSTANCE);
        attacker = new Output<>("attacker", "Attacker", EntityType.INSTANCE);
        victim = new Output<>("victim", "Victim", EntityType.INSTANCE);
        amount = new Output<>("amount", "Damage Amount", NumberType.INSTANCE);
        attacker.valueFromScope();
        victim.valueFromScope();
        amount.valueFromScope();
    }

    @Override
    public void handleEvent(EventContext context) {
        if (!(context.event instanceof EntityDeathEvent e && e.getDamageSource().getCausingEntity() != null && !(e.getDamageSource().getCausingEntity() instanceof Player) && !(e.getEntity() instanceof Player))) return;

        CodeThread thread = context.newCodeThread();
        thread.setScopeValue(this.attacker, new EntityValue(e.getDamageSource().getCausingEntity()));
        thread.setScopeValue(this.victim, new EntityValue(e.getEntity()));
        thread.setScopeValue(this.amount, e.getEntity().getLastDamage());
        thread.sendSignal(signal);
        thread.clearQueue();
    }

    @Override
    public Node copy() {
        return new OnEntityKillEntityNode();
    }

}