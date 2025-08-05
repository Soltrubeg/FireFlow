package de.blazemcworld.fireflow.code.node.impl.position;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.NumberType;
import de.blazemcworld.fireflow.code.type.PositionType;
import org.bukkit.Location;
import org.bukkit.Material;

public class PositionDistanceNode extends Node {

    public PositionDistanceNode() {
        super("position_distance", "Position Distance", "Returns the distance between two positions", Material.DIAMOND);

        Input<Location> aPos = new Input<>("a", "A", PositionType.INSTANCE);
        Input<Location> bPos = new Input<>("b", "B", PositionType.INSTANCE);
        Output<Double> distance = new Output<>("distance", "Distance", NumberType.INSTANCE);

        distance.valueFrom(ctx -> aPos.getValue(ctx).distance(bPos.getValue(ctx)));
    }

    @Override
    public Node copy() {
        return new PositionDistanceNode();
    }

}
