package de.blazemcworld.fireflow.code.node.impl.player.movement;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.ConditionType;
import de.blazemcworld.fireflow.code.type.PlayerType;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PlayerCanFlyNode extends Node {
    public PlayerCanFlyNode() {
        super("player_can_fly", "Player Can Fly", "Checks if the player can fly", Material.WHITE_WOOL);
        Input<PlayerValue> player = new Input<>("player", "Player", PlayerType.INSTANCE);
        Output<Boolean> allowed = new Output<>("allowed", "Allowed", ConditionType.INSTANCE);

        allowed.valueFrom(ctx -> player.getValue(ctx).tryGet(ctx, Player::getAllowFlight, false));
    }

    @Override
    public Node copy() {
        return new PlayerCanFlyNode();
    }
}
