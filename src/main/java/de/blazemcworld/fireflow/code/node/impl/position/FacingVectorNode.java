package de.blazemcworld.fireflow.code.node.impl.position;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.PositionType;
import de.blazemcworld.fireflow.code.type.VectorType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;

public class FacingVectorNode extends Node {

    public FacingVectorNode() {
        super("facing_vector", "Facing Vector", "Returns the direction of a position", Material.ENDER_EYE);

        Input<Location> position = new Input<>("position", "Position", PositionType.INSTANCE);
        Output<Vector> vector = new Output<>("vector", "Vector", VectorType.INSTANCE);

        vector.valueFrom(ctx -> {
            return position.getValue(ctx).getDirection();
        });
    }

    @Override
    public Node copy() {
        return new FacingVectorNode();
    }
}
