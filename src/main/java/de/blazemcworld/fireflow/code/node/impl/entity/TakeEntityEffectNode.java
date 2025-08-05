package de.blazemcworld.fireflow.code.node.impl.entity;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.node.option.EffectOptions;
import de.blazemcworld.fireflow.code.type.EntityType;
import de.blazemcworld.fireflow.code.type.SignalType;
import de.blazemcworld.fireflow.code.type.StringType;
import de.blazemcworld.fireflow.code.value.EntityValue;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.potion.PotionEffectType;

public class TakeEntityEffectNode extends Node {

    public TakeEntityEffectNode() {
        super("take_entity_effect", "Take Entity Effect", "Takes an effect from the entity", Material.LINGERING_POTION);

        Input<Void> signal = new Input<>("signal", "Signal", SignalType.INSTANCE);
        Input<EntityValue> entity = new Input<>("entity", "Entity", EntityType.INSTANCE);
        Input<String> effect = new Input<>("effect", "Effect", StringType.INSTANCE).options(EffectOptions.INSTANCE);
        Output<Void> next = new Output<>("next", "Next", SignalType.INSTANCE);

        signal.onSignal((ctx) -> {
            entity.getValue(ctx).use(ctx, e -> {
                if (!(e instanceof org.bukkit.entity.LivingEntity living)) return;
                PotionEffectType type = Registry.POTION_EFFECT_TYPE.get(NamespacedKey.minecraft(effect.getValue(ctx)));
                if (type == null) return;
                living.removePotionEffect(type);
            });
            ctx.sendSignal(next);
        });
    }

    @Override
    public Node copy() {
        return new TakeEntityEffectNode();
    }

}
