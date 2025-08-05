package de.blazemcworld.fireflow.code.node.impl.player.meta;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.PlayerType;
import de.blazemcworld.fireflow.code.type.StringType;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class GetPlayerNameNode extends Node {
    public GetPlayerNameNode() {
        super("get_player_name", "Get Player Name", "Gets the name of the player. Returns an empty string if the player is not playing on the space.", Material.NAME_TAG);

        Input<PlayerValue> player = new Input<>("player", "Player", PlayerType.INSTANCE);
        Output<String> name = new Output<>("name", "Name", StringType.INSTANCE);

        name.valueFrom(ctx -> player.getValue(ctx).tryGet(ctx, Player::getName, ""));
    }

    @Override
    public Node copy() {
        return new GetPlayerNameNode();
    }
}