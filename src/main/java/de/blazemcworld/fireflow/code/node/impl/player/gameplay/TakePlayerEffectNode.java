package de.blazemcworld.fireflow.code.node.impl.player.gameplay;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.node.option.EffectOptions;
import de.blazemcworld.fireflow.code.type.PlayerType;
import de.blazemcworld.fireflow.code.type.SignalType;
import de.blazemcworld.fireflow.code.type.StringType;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.potion.PotionEffectType;

public class TakePlayerEffectNode extends Node {

    public TakePlayerEffectNode() {
        super("take_player_effect", "Take Player Effect", "Takes an effect from the player", Material.LINGERING_POTION);

        Input<Void> signal = new Input<>("signal", "Signal", SignalType.INSTANCE);
        Input<PlayerValue> player = new Input<>("player", "Player", PlayerType.INSTANCE);
        Input<String> effect = new Input<>("effect", "Effect", StringType.INSTANCE).options(EffectOptions.INSTANCE);
        Output<Void> next = new Output<>("next", "Next", SignalType.INSTANCE);

        signal.onSignal((ctx) -> {
            player.getValue(ctx).tryUse(ctx, p -> {
                PotionEffectType type = Registry.EFFECT.get(NamespacedKey.minecraft(effect.getValue(ctx)));
                if (type == null) return;
                p.removePotionEffect(type);
            });
            ctx.sendSignal(next);
        });
    }

    @Override
    public Node copy() {
        return new TakePlayerEffectNode();
    }

}
