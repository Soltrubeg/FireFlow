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
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class OnEntityAttackEntityNode extends Node implements EventNode {

    private final Output<Void> signal;
    private final Output<EntityValue> attacker;
    private final Output<EntityValue> victim;
    private final Output<Double> amount;

    public OnEntityAttackEntityNode() {
        super("on_entity_attack_entity", "On Entity Attack Entity", "Emits a signal when an entity attacks an entity.", Material.GOLDEN_SHOVEL);

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
        if (!(context.event instanceof EntityDamageByEntityEvent e && !(e.getDamager() instanceof Player) && !(e.getEntity() instanceof Player))) return;

        CodeThread thread = context.newCodeThread();
        thread.setScopeValue(this.attacker, new EntityValue(e.getDamager()));
        thread.setScopeValue(this.victim, new EntityValue(e.getEntity()));
        thread.setScopeValue(this.amount, e.getDamage());
        thread.sendSignal(signal);
        thread.clearQueue();
    }


    @Override
    public Node copy() {
        return new OnEntityAttackEntityNode();
    }

}