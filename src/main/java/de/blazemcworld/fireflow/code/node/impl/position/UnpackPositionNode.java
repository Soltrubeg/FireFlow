package de.blazemcworld.fireflow.code.node.impl.position;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.NumberType;
import de.blazemcworld.fireflow.code.type.PositionType;
import org.bukkit.Location;
import org.bukkit.Material;

public class UnpackPositionNode extends Node {

    public UnpackPositionNode() {
        super("unpack_position", "Unpack Position", "Unpacks a position into its components", Material.GOLD_INGOT);

        Input<Location> position = new Input<>("position", "Position", PositionType.INSTANCE);
        Output<Double> x = new Output<>("x", "X", NumberType.INSTANCE);
        Output<Double> y = new Output<>("y", "Y", NumberType.INSTANCE);
        Output<Double> z = new Output<>("z", "Z", NumberType.INSTANCE);
        Output<Double> pitch = new Output<>("pitch", "Pitch", NumberType.INSTANCE);
        Output<Double> yaw = new Output<>("yaw", "Yaw", NumberType.INSTANCE);

        x.valueFrom(ctx -> position.getValue(ctx).x());
        y.valueFrom(ctx -> position.getValue(ctx).y());
        z.valueFrom(ctx -> position.getValue(ctx).z());
        pitch.valueFrom(ctx -> (double) position.getValue(ctx).getPitch());
        yaw.valueFrom(ctx -> (double) position.getValue(ctx).getYaw());
    }

    @Override
    public Node copy() {
        return new UnpackPositionNode();
    }
}
