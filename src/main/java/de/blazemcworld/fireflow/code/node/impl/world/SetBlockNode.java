package de.blazemcworld.fireflow.code.node.impl.world;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.ConditionType;
import de.blazemcworld.fireflow.code.type.SignalType;
import de.blazemcworld.fireflow.code.type.StringType;
import de.blazemcworld.fireflow.code.type.VectorType;
import org.bukkit.Material;
import org.bukkit.util.Vector;

public class SetBlockNode extends Node {
    public SetBlockNode() {
        super("set_block", "Set Block", "Sets a block at a position", Material.STONE);
        Input<Void> signal = new Input<>("signal", "Signal", SignalType.INSTANCE);
        Input<Vector> position = new Input<>("position", "Position", VectorType.INSTANCE);
        Input<String> block = new Input<>("block", "Block", StringType.INSTANCE);
        Input<Boolean> sendUpdate = new Input<>("send_update", "Send Update", ConditionType.INSTANCE);
        Output<Void> next = new Output<>("next", "Next", SignalType.INSTANCE);

        signal.onSignal((ctx) -> {
            Material material = Material.getMaterial(block.getValue(ctx));
            if (material != null && material.isBlock()) {
                Vector pos = position.getValue(ctx);
                if (pos.getX() < -512 || pos.getX() > 511 || pos.getZ() < -512 || pos.getZ() > 511
                        || pos.getY() < ctx.evaluator.world.getMinHeight() || pos.getY() > ctx.evaluator.world.getMaxHeight()) {
                    ctx.sendSignal(next);
                    return;
                }
                boolean updates = sendUpdate.getValue(ctx);
                ctx.evaluator.world.getBlockAt(pos.toLocation(ctx.evaluator.world)).setType(material, updates);
            }
            ctx.sendSignal(next);
        });
    }

    @Override
    public Node copy() {
        return new SetBlockNode();
    }
}