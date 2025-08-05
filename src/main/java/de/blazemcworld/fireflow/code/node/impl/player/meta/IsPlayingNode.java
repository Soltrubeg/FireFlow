package de.blazemcworld.fireflow.code.node.impl.player.meta;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.ConditionType;
import de.blazemcworld.fireflow.code.type.PlayerType;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import org.bukkit.Material;

public class IsPlayingNode extends Node {

    public IsPlayingNode() {
        super("is_playing", "Is Playing", "Checks if the player is currently playing on the space", Material.OAK_SAPLING);

        Input<PlayerValue> player = new Input<>("player", "Player", PlayerType.INSTANCE);
        Output<Boolean> playing = new Output<>("playing", "Playing", ConditionType.INSTANCE);

        playing.valueFrom(ctx -> player.getValue(ctx).tryGet(ctx, p -> true, false));
    }

    @Override
    public Node copy() {
        return new IsPlayingNode();
    }
}
