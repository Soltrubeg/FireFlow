package de.blazemcworld.fireflow.code.node.impl.player.gameplay;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.PlayerType;
import de.blazemcworld.fireflow.code.type.SignalType;
import de.blazemcworld.fireflow.code.type.StringType;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import org.bukkit.Material;

public class PlayerAnimationNode extends Node {

    public PlayerAnimationNode() {
        super("player_animation", "Player Animation", "Makes the player do an animation", Material.GLOWSTONE_DUST);

        Input<Void> signal = new Input<>("signal", "Signal", SignalType.INSTANCE);
        Input<PlayerValue> player = new Input<>("player", "Player", PlayerType.INSTANCE);
        Input<String> animation = new Input<>("animation", "Animation", StringType.INSTANCE)
                .options("damage", "main_hand", "off_hand");
        Output<Void> next = new Output<>("next", "Next", SignalType.INSTANCE);

        signal.onSignal((ctx) -> {
            player.getValue(ctx).tryUse(ctx, p -> {
                switch (animation.getValue(ctx)) {
                    case "damage" -> p.playHurtAnimation(0);
                    case "main_hand" -> p.swingMainHand();
                    case "off_hand" -> p.swingOffHand();
                };
            });
            ctx.sendSignal(next);
        });
    }

    @Override
    public Node copy() {
        return new PlayerAnimationNode();
    }
}
