package de.blazemcworld.fireflow.code.node.impl.player.visual;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.PlayerType;
import de.blazemcworld.fireflow.code.type.SignalType;
import de.blazemcworld.fireflow.code.type.StringType;
import de.blazemcworld.fireflow.code.type.VectorType;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;

public class SendBlockChangeNode extends Node {
    public SendBlockChangeNode() {
        super("send_block_change", "Send Block Change", "Sends a fake block change packet", Material.AXOLOTL_BUCKET);
        Input<Void> signal = new Input<>("signal", "Signal", SignalType.INSTANCE);
        Input<String> block = new Input<>("block", "Block", StringType.INSTANCE);
        Input<Vector> position = new Input<>("position", "Position", VectorType.INSTANCE);
        Input<PlayerValue> player = new Input<>("player", "Player", PlayerType.INSTANCE);
        Output<Void> next = new Output<>("next", "Next", SignalType.INSTANCE);
        signal.onSignal((ctx) -> {
            Material m = Material.getMaterial(block.getValue(ctx));
            if (m != null && m.isBlock()) {
                Vector pos = position.getValue(ctx);
                player.getValue(ctx).tryUse(ctx, p -> {
                    p.sendBlockChange(new Location(ctx.evaluator.world, pos.getX(), pos.getY(), pos.getZ()), m.createBlockData());
                });
            }
            ctx.sendSignal(next);
        });
    }

    @Override
    public Node copy() {
        return new SendBlockChangeNode();
    }
}