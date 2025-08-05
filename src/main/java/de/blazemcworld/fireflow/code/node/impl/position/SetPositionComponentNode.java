package de.blazemcworld.fireflow.code.node.impl.position;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.NumberType;
import de.blazemcworld.fireflow.code.type.PositionType;
import de.blazemcworld.fireflow.code.type.StringType;
import org.bukkit.Location;
import org.bukkit.Material;

public class SetPositionComponentNode extends Node {
    public SetPositionComponentNode() {
        super("set_position_component", "Set Position Component", "Changes a single component of a position", Material.COMPASS);
        
        Input<Location> position = new Input<>("position", "Position", PositionType.INSTANCE);
        Input<String> component = new Input<>("component", "Component", StringType.INSTANCE)
                .options("X", "Y", "Z", "Pitch", "Yaw");
        Input<Double> value = new Input<>("value", "Value", NumberType.INSTANCE);
        Output<Location> output = new Output<>("output", "Output", PositionType.INSTANCE);

        output.valueFrom((ctx) -> {
            Location v = position.getValue(ctx).clone();
            double newValue = value.getValue(ctx);
            switch (component.getValue(ctx)) {
                case "X" -> v.setX(newValue);
                case "Y" -> v.setY(newValue);
                case "Z" -> v.setZ(newValue);
                case "Pitch" -> v.setPitch((float) newValue);
                case "Yaw" -> v.setYaw((float) newValue);
            };
            return v;
        });
    }

    @Override
    public Node copy() {
        return new SetPositionComponentNode();
    }
}
