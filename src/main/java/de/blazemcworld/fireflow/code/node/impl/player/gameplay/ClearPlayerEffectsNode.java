package de.blazemcworld.fireflow.code.node.impl.player.gameplay;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.PlayerType;
import de.blazemcworld.fireflow.code.type.SignalType;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;

public class ClearPlayerEffectsNode extends Node {

    public ClearPlayerEffectsNode() {
        super("clear_player_effects", "Clear Player Effects", "Removes all potion effects from a player.", Material.MILK_BUCKET);

        Input<Void> signal = new Input<>("signal", "Signal", SignalType.INSTANCE);
        Input<PlayerValue> player = new Input<>("player", "Player", PlayerType.INSTANCE);
        Output<Void> next = new Output<>("next", "Next", SignalType.INSTANCE);

        signal.onSignal((ctx) -> {
            player.getValue(ctx).tryUse(ctx, LivingEntity::clearActivePotionEffects);
            ctx.sendSignal(next);
        });
    }

    @Override
    public Node copy() {
        return new ClearPlayerEffectsNode();
    }
}
