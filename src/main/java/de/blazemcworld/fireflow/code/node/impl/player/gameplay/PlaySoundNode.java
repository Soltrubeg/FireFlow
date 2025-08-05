package de.blazemcworld.fireflow.code.node.impl.player.gameplay;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.node.option.SoundOptions;
import de.blazemcworld.fireflow.code.type.*;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import org.bukkit.*;
import org.bukkit.util.Vector;

public class PlaySoundNode extends Node {

    public PlaySoundNode() {
        super("play_sound", "Play Sound", "Make a player hear a sound as if it was coming from a specific position.", Material.NOTE_BLOCK);

        Input<Void> signal = new Input<>("signal", "Signal", SignalType.INSTANCE);
        Input<PlayerValue> player = new Input<>("player", "Player", PlayerType.INSTANCE);
        Input<String> sound = new Input<>("sound", "Sound", StringType.INSTANCE).options(SoundOptions.INSTANCE);
        Input<String> mode = new Input<>("mode", "Mode", StringType.INSTANCE);
        Input<Double> volume = new Input<>("volume", "Volume", NumberType.INSTANCE);
        Input<Double> pitch = new Input<>("pitch", "Pitch", NumberType.INSTANCE);
        Input<Vector> position = new Input<>("position", "Position", VectorType.INSTANCE);
        Output<Void> next = new Output<>("next", "Next", SignalType.INSTANCE);

        signal.onSignal((ctx) -> {
            Vector pos = position.getValue(ctx);
            player.getValue(ctx).tryUse(ctx, p -> {
                String modeValue = mode.getValue(ctx);
                Sound snd = Registry.SOUNDS.get(NamespacedKey.minecraft(sound.getValue(ctx)));
                if (snd == null) return;

                SoundCategory category = SoundCategory.MASTER;
                try {
                    category = SoundCategory.valueOf(modeValue.toUpperCase());
                } catch (IllegalArgumentException ignore) {}

                p.playSound(
                        new Location(ctx.evaluator.world, pos.getX(), pos.getY(), pos.getZ()), snd, category,
                        volume.getValue(ctx).floatValue(), pitch.getValue(ctx).floatValue()
                );
            });
            ctx.sendSignal(next);
        });
    }

    @Override
    public Node copy() {
        return new PlaySoundNode();
    }
}

