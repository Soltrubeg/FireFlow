package de.blazemcworld.fireflow.code.node.impl.player.movement;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.PlayerType;
import de.blazemcworld.fireflow.code.type.StringType;
import de.blazemcworld.fireflow.code.type.VectorType;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

public class PlayerCrosshairTargetNode extends Node {

    public PlayerCrosshairTargetNode() {
        super("player_crosshair_target", "Player Crosshair Target", "Checks the block the player is currently looking at. Ignores fluids unless set to true.", Material.TIPPED_ARROW);

        Input<PlayerValue> player = new Input<>("player", "Player", PlayerType.INSTANCE);
        Output<Vector> position = new Output<>("position", "Position", VectorType.INSTANCE);
        Output<String> block = new Output<>("block", "Block", StringType.INSTANCE);

        position.valueFrom((ctx) -> player.getValue(ctx).tryGet(ctx, p -> {
                    Block b = p.getTargetBlockExact(4);
                    if (b == null) return p.getEyeLocation().toVector();
                    return b.getLocation().toVector();
                }, new Vector()
        ));

        block.valueFrom((ctx) -> player.getValue(ctx).tryGet(ctx, p -> {
                    Block b = p.getTargetBlockExact(4);
                    if (b == null) return "air";
                    return b.getType().key().value();
                }, "air"
        ));
    }

    @Override
    public Node copy() {
        return new PlayerCrosshairTargetNode();
    }

}
