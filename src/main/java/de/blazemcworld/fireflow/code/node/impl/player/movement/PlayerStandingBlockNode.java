package de.blazemcworld.fireflow.code.node.impl.player.movement;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.ConditionType;
import de.blazemcworld.fireflow.code.type.PlayerType;
import de.blazemcworld.fireflow.code.type.StringType;
import de.blazemcworld.fireflow.code.type.VectorType;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Optional;

public class PlayerStandingBlockNode extends Node {

    public PlayerStandingBlockNode() {
        super("player_standing_block", "Player Standing Block", "Checks if the player is standing on something or floating, and which block it is.", Material.BAMBOO_PRESSURE_PLATE);

        Input<PlayerValue> player = new Input<>("player", "Player", PlayerType.INSTANCE);
        Output<Vector> position = new Output<>("position", "Position", VectorType.INSTANCE);
        Output<String> block = new Output<>("block", "Block", StringType.INSTANCE);
        Output<Boolean> floating = new Output<>("floating", "Floating", ConditionType.INSTANCE);

        position.valueFrom((ctx) -> player.getValue(ctx).tryGet(ctx, p ->
                supportingPos(p).orElse(p.getLocation()).toVector(), new Vector()
        ));

        block.valueFrom((ctx) -> player.getValue(ctx).tryGet(ctx, p -> supportingPos(p).map(l -> l.getBlock().getType().key().value()).orElse("unknown"), "unknown"
        ));

        floating.valueFrom((ctx) -> player.getValue(ctx).tryGet(ctx, p -> supportingPos(p).isEmpty(), false));
    }

    @Override
    public Node copy() {
        return new PlayerStandingBlockNode();
    }

    private static Optional<Location> supportingPos(Player player) {
        return ((CraftPlayer) player).getHandle().mainSupportingBlockPos.map(b -> new Location(player.getWorld(), b.getX(), b.getY(), b.getZ()));
    }
}
