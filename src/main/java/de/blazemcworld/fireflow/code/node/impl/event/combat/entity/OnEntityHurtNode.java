package de.blazemcworld.fireflow.code.node.impl.event.combat.entity;

import de.blazemcworld.fireflow.code.CodeThread;
import de.blazemcworld.fireflow.code.EventContext;
import de.blazemcworld.fireflow.code.node.EventNode;
import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.EntityType;
import de.blazemcworld.fireflow.code.type.NumberType;
import de.blazemcworld.fireflow.code.type.SignalType;
import de.blazemcworld.fireflow.code.type.StringType;
import de.blazemcworld.fireflow.code.value.EntityValue;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

public class OnEntityHurtNode extends Node implements EventNode {

    private final Output<Void> signal;
    private final Output<EntityValue> entity;
    private final Output<Double> amount;
    private final Output<String> type;

    public OnEntityHurtNode() {
        super("on_entity_hurt", "On Entity Hurt", "Emits a signal when an entity is about to take damage.", Material.REDSTONE_ORE);

        signal = new Output<>("signal", "Signal", SignalType.INSTANCE);
        entity = new Output<>("entity", "entity", EntityType.INSTANCE);
        amount = new Output<>("amount", "Damage Amount", NumberType.INSTANCE);
        type = new Output<>("type", "Damage Type", StringType.INSTANCE);
        entity.valueFromScope();
        amount.valueFromScope();
        type.valueFromScope();
    }

    @Override
    public void handleEvent(EventContext context) {
        if (!(context.event instanceof EntityDamageEvent e && !(e.getEntity() instanceof Player))) return;

        CodeThread thread = context.newCodeThread();
        thread.setScopeValue(this.entity, new EntityValue(e.getEntity()));
        thread.setScopeValue(this.amount, e.getDamage());
        thread.sendSignal(signal);
        thread.clearQueue();
    }
    @Override
    public Node copy() {
        return new OnEntityHurtNode();
    }
}

