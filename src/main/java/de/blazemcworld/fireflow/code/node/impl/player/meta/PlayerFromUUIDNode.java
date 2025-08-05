package de.blazemcworld.fireflow.code.node.impl.player.meta;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.PlayerType;
import de.blazemcworld.fireflow.code.type.StringType;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import java.util.UUID;
import org.bukkit.Material;

public class PlayerFromUUIDNode extends Node {
    public PlayerFromUUIDNode() {
        super("player_from_uuid", "Player From UUID", "Gets a player based on their UUID", Material.SKELETON_SKULL);

        Input<String> uuid = new Input<>("uuid", "UUID", StringType.INSTANCE);
        Output<PlayerValue> player = new Output<>("player", "Player", PlayerType.INSTANCE);

        player.valueFrom(ctx -> {
            try {
                return new PlayerValue(UUID.fromString(uuid.getValue(ctx)));
            } catch (IllegalArgumentException e) {
                return PlayerType.INSTANCE.defaultValue();
            }
        });
    }

    @Override
    public Node copy() {
        return new PlayerFromUUIDNode();
    }
}