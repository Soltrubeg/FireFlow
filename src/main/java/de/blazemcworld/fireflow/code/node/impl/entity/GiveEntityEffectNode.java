package de.blazemcworld.fireflow.code.node.impl.entity;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.node.option.EffectOptions;
import de.blazemcworld.fireflow.code.type.*;
import de.blazemcworld.fireflow.code.value.EntityValue;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GiveEntityEffectNode extends Node {

    public GiveEntityEffectNode() {
        super("give_entity_effect", "Give Entity Effect", "Gives the entity a potion effect.", Material.SPLASH_POTION);

        Input<Void> signal = new Input<>("signal", "Signal", SignalType.INSTANCE);
        Input<EntityValue> entity = new Input<>("entity", "Entity", EntityType.INSTANCE);
        Input<String> effect = new Input<>("effect", "Effect", StringType.INSTANCE).options(EffectOptions.INSTANCE);
        Input<Double> duration = new Input<>("duration", "Duration", NumberType.INSTANCE);
        Input<Double> amplifier = new Input<>("amplifier", "Amplifier", NumberType.INSTANCE);
        Input<Boolean> isAmbient = new Input<>("ambient", "Ambient", ConditionType.INSTANCE);
        Input<Boolean> showParticles = new Input<>("show_particles", "Show Particles", ConditionType.INSTANCE);
        Input<Boolean> showIcon = new Input<>("show_icon", "Show Icon", ConditionType.INSTANCE);
        Output<Void> next = new Output<>("next", "Next", SignalType.INSTANCE);

        signal.onSignal((ctx) -> {
            entity.getValue(ctx).use(ctx, e -> {
                if (!(e instanceof LivingEntity living)) return;
                PotionEffectType type = Registry.POTION_EFFECT_TYPE.get(NamespacedKey.minecraft(effect.getValue(ctx)));
                if (type == null) return;

                living.addPotionEffect(new PotionEffect(
                        type, duration.getValue(ctx).intValue(), amplifier.getValue(ctx).intValue(),
                        isAmbient.getValue(ctx), showParticles.getValue(ctx), showIcon.getValue(ctx)
                ));
            });
            ctx.sendSignal(next);
        });
    }

    @Override
    public Node copy() {
        return new GiveEntityEffectNode();
    }
}