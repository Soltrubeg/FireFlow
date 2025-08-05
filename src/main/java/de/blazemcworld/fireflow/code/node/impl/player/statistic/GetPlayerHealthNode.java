package de.blazemcworld.fireflow.code.node.impl.player.statistic;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.NumberType;
import de.blazemcworld.fireflow.code.type.PlayerType;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;

public class GetPlayerHealthNode extends Node {
    public GetPlayerHealthNode() {
        super("get_player_health", "Get Player Health", "Gets the health of the player", Material.RED_WOOL);
        Input<PlayerValue> player = new Input<>("player", "Player", PlayerType.INSTANCE);
        Output<Double> health = new Output<>("health", "Health", NumberType.INSTANCE);

        health.valueFrom(ctx -> player.getValue(ctx).tryGet(ctx, Damageable::getHealth, 0.0));
    }

    @Override
    public Node copy() {
        return new GetPlayerHealthNode();
    }
}
