package de.blazemcworld.fireflow.code.node.impl.player.statistic;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.NumberType;
import de.blazemcworld.fireflow.code.type.PlayerType;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import org.bukkit.Material;

public class GetPlayerFoodNode extends Node {
    public GetPlayerFoodNode() {
        super("get_player_food", "Get Player Food", "Gets the food level of the player", Material.ROTTEN_FLESH);
        Input<PlayerValue> player = new Input<>("player", "Player", PlayerType.INSTANCE);
        Output<Double> food = new Output<>("food", "Food", NumberType.INSTANCE);

        food.valueFrom(ctx -> player.getValue(ctx).tryGet(ctx, p -> (double) p.getFoodLevel(), 0.0));
    }

    @Override
    public Node copy() {
        return new GetPlayerFoodNode();
    }
}
