package de.blazemcworld.fireflow.code.node.impl.player.meta;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.PlayerType;
import de.blazemcworld.fireflow.code.type.StringType;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PlayerFromNameNode extends Node {
    public PlayerFromNameNode() {
        super("player_from_name", "Player From Name", "Gets an online player based on their name", Material.PLAYER_HEAD);

        Input<String> name = new Input<>("name", "Name", StringType.INSTANCE);
        Output<PlayerValue> player = new Output<>("player", "Player", PlayerType.INSTANCE);

        player.valueFrom(ctx -> {
            for (Player p : ctx.evaluator.world.getPlayers()) {
                if (p.getName().equalsIgnoreCase(name.getValue(ctx))) {
                    return new PlayerValue(p);
                }
            }
            return PlayerType.INSTANCE.defaultValue();
        });
    }

    @Override
    public Node copy() {
        return new PlayerFromNameNode();
    }
}