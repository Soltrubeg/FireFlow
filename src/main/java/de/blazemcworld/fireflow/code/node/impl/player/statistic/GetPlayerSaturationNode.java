package de.blazemcworld.fireflow.code.node.impl.player.statistic;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.NumberType;
import de.blazemcworld.fireflow.code.type.PlayerType;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import org.bukkit.Material;

public class GetPlayerSaturationNode extends Node {
    public GetPlayerSaturationNode() {
        super("get_player_saturation", "Get Player Saturation", "Gets the saturation of the player", Material.GOLDEN_CARROT);
        Input<PlayerValue> player = new Input<>("player", "Player", PlayerType.INSTANCE);
        Output<Double> saturation = new Output<>("saturation", "Saturation", NumberType.INSTANCE);

        saturation.valueFrom(ctx -> player.getValue(ctx).tryGet(ctx, p -> (double) p.getSaturation(), 0.0));
    }

    @Override
    public Node copy() {
        return new GetPlayerSaturationNode();
    }
}
