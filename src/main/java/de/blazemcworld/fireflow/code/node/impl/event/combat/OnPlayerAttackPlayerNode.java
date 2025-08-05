package de.blazemcworld.fireflow.code.node.impl.event.combat;

import de.blazemcworld.fireflow.code.CodeThread;
import de.blazemcworld.fireflow.code.EventContext;
import de.blazemcworld.fireflow.code.node.EventNode;
import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.ConditionType;
import de.blazemcworld.fireflow.code.type.NumberType;
import de.blazemcworld.fireflow.code.type.PlayerType;
import de.blazemcworld.fireflow.code.type.SignalType;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class OnPlayerAttackPlayerNode extends Node implements EventNode {

    private final Output<Void> signal;
    private final Output<PlayerValue> attacker;
    private final Output<PlayerValue> victim;
    private final Output<Double> amount;
    private final Output<Boolean> isCritical;

    public OnPlayerAttackPlayerNode() {
        super("on_player_attack_player", "On Player Attack Player", "Emits a signal when a player attacks another player.", Material.WOODEN_SWORD);

        signal = new Output<>("signal", "Signal", SignalType.INSTANCE);
        attacker = new Output<>("attacker", "Attacker", PlayerType.INSTANCE);
        victim = new Output<>("victim", "Victim", PlayerType.INSTANCE);
        amount = new Output<>("amount", "Damage", NumberType.INSTANCE);
        isCritical = new Output<>("isCritical", "Is Critical", ConditionType.INSTANCE);
        attacker.valueFromScope();
        victim.valueFromScope();
        amount.valueFromScope();
        isCritical.valueFromScope();
    }

    @Override
    public void handleEvent(EventContext context) {
        if (!(context.event instanceof EntityDamageByEntityEvent e && e.getDamager() instanceof Player attacker && e.getEntity() instanceof Player victim)) return;

        CodeThread thread = context.newCodeThread();
        thread.setScopeValue(this.attacker, new PlayerValue(attacker));
        thread.setScopeValue(this.victim, new PlayerValue(victim));
        thread.setScopeValue(this.amount, e.getDamage());
        thread.setScopeValue(isCritical, e.isCritical());
        thread.sendSignal(signal);
        thread.clearQueue();
    }

    @Override
    public Node copy() {
        return new OnPlayerAttackPlayerNode();
    }

}
