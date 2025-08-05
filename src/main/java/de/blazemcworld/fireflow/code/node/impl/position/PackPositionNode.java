package de.blazemcworld.fireflow.code.node.impl.position;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.NumberType;
import de.blazemcworld.fireflow.code.type.PositionType;
import org.bukkit.Location;
import org.bukkit.Material;

public class PackPositionNode extends Node {

    public PackPositionNode() {
        super("pack_position", "Pack Position", "Creates a position from x, y, z, pitch and yaw", Material.GOLD_BLOCK);

        Input<Double> x = new Input<>("x", "X", NumberType.INSTANCE);
        Input<Double> y = new Input<>("y", "Y", NumberType.INSTANCE);
        Input<Double> z = new Input<>("z", "Z", NumberType.INSTANCE);
        Input<Double> pitch = new Input<>("pitch", "Pitch", NumberType.INSTANCE);
        Input<Double> yaw = new Input<>("yaw", "Yaw", NumberType.INSTANCE);
        Output<Location> position = new Output<>("position", "Position", PositionType.INSTANCE);

        position.valueFrom(ctx -> new Location(
                ctx.evaluator.world,
                x.getValue(ctx),
                y.getValue(ctx),
                z.getValue(ctx),
                pitch.getValue(ctx).floatValue(),
                yaw.getValue(ctx).floatValue()
        ));
    }

    @Override
    public PackPositionNode copy() {
        return new PackPositionNode();
    }

}
