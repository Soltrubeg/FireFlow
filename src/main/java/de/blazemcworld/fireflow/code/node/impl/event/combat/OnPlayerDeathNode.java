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
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;

public class OnPlayerDeathNode extends Node implements EventNode {

    private final Output<Void> signal;
    private final Output<PlayerValue> player;
    private final Output<Double> amount;
    private final Output<String> type;

    public OnPlayerDeathNode() {
        super("on_player_player_death", "On Player Death", "Emits a signal when a player is about to die.", Material.SKELETON_SKULL);

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
        if (!(context.event instanceof EntityDeathEvent e && e.getEntity() instanceof Player p)) return;

        CodeThread thread = context.newCodeThread();
        thread.setScopeValue(this.player, new PlayerValue(p));
        thread.setScopeValue(this.amount, e.getEntity().getLastDamage());
        thread.setScopeValue(this.type, RegistryAccess.registryAccess().getRegistry(RegistryKey.DAMAGE_TYPE).getKey(e.getDamageSource().getDamageType()).getKey());
        thread.sendSignal(signal);
        thread.clearQueue();
    }


    @Override
    public Node copy() {
        return new OnPlayerDeathNode();
    }
}

