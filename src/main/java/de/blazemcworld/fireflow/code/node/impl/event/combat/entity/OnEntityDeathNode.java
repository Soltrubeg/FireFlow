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
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;

public class OnEntityDeathNode extends Node implements EventNode {

    private final Output<Void> signal;
    private final Output<EntityValue> entity;
    private final Output<Double> amount;
    private final Output<String> type;

    public OnEntityDeathNode() {
        super("on_entity_death", "On Entity Death", "Emits a signal when an entity is about to die.", Material.BONE);

        signal = new Output<>("signal", "Signal", SignalType.INSTANCE);
        entity = new Output<>("entity", "Entity", EntityType.INSTANCE);
        amount = new Output<>("amount", "Damage Amount", NumberType.INSTANCE);
        type = new Output<>("damage", "Damage Type", StringType.INSTANCE);
        entity.valueFromScope();
        amount.valueFromScope();
        type.valueFromScope();
    }

    @Override
    public Node copy() {
        return new OnEntityDeathNode();
    }

    @Override
    public void handleEvent(EventContext context) {
        if (!(context.event instanceof EntityDeathEvent e && !(e.getEntity() instanceof Player))) return;

        CodeThread thread = context.newCodeThread();
        thread.setScopeValue(this.entity, new EntityValue(e.getEntity()));
        thread.setScopeValue(this.amount, e.getEntity().getLastDamage());
        thread.setScopeValue(this.type, RegistryAccess.registryAccess().getRegistry(RegistryKey.DAMAGE_TYPE).getKey(e.getDamageSource().getDamageType()).getKey());
        thread.sendSignal(signal);
        thread.clearQueue();
    }

}
