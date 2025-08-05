package de.blazemcworld.fireflow.code.node.impl.world;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.node.SingleGenericNode;
import de.blazemcworld.fireflow.code.type.*;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.bukkit.Material;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.util.Vector;

import java.util.Optional;

public class SetBlockTagNode<T> extends SingleGenericNode<T> {

    @SuppressWarnings("unchecked")
    public <S extends Comparable<S>> SetBlockTagNode(WireType<T> type) {
        super("set_block_tag", type == null ? "Set Block Tag" : "Set " + type.getName() + " Block Tag", "Sets the value of a block's tag", Material.STONECUTTER, type);

        Input<Void> signal = new Input<>("signal", "Signal", SignalType.INSTANCE);
        Input<Vector> position = new Input<>("position", "Position", VectorType.INSTANCE);
        Input<String> tag = new Input<>("tag", "Tag", StringType.INSTANCE);
        Input<T> value = new Input<>("value", "Value", type);
        Output<Void> next = new Output<>("next", "Next", SignalType.INSTANCE);

        signal.onSignal((ctx) -> {
            Vector pos = position.getValue(ctx);
            if (pos.getX() < -512 || pos.getX() > 511 || pos.getZ() < -512 || pos.getZ() > 511 || pos.getY() < ctx.evaluator.world.getMinHeight() || pos.getY() > ctx.evaluator.world.getMaxHeight()) {
                ctx.sendSignal(next);
                return;
            }

            String propertyName = tag.getValue(ctx);
            T propertyValue = value.getValue(ctx);

            CraftBlockState state = (CraftBlockState) pos.toLocation(ctx.evaluator.world).getBlock().getState();
            for (Property<?> property : state.getHandle().getProperties()) {
                if (property.getName().equals(propertyName)) {
                    switch (property) {
                        case BooleanProperty booleanProperty when type == ConditionType.INSTANCE -> {
                            boolean booleanValue = (boolean) propertyValue;
                            state.setData(state.getHandle().setValue(booleanProperty, booleanValue));
                        }
                        case IntegerProperty intProperty when type == NumberType.INSTANCE -> {
                            int intValue = ((Double) propertyValue).intValue();
                            if (intProperty.getInternalIndex(intValue) >= 0) {
                                state.setData(state.getHandle().setValue(intProperty, intValue));
                            }
                        }
                        case EnumProperty<?> enumProperty when type == StringType.INSTANCE -> {
                            String stringValue = (String) propertyValue;
                            Optional<S> parsedValue = ((Property<S>) property).getValue(stringValue);
                            parsedValue.ifPresent(s -> state.setData(state.getHandle().setValue((Property<S>) property, s)));
                        }
                        default -> {
                        }
                    }
                    break;
                }
            }

            ctx.sendSignal(next);
        });
    }

    @Override
    public Node copy() {
        return new SetBlockTagNode<>(type);
    }

    @Override
    public boolean acceptsType(WireType<?> type, int index) {
        return type == ConditionType.INSTANCE || type == NumberType.INSTANCE || type == StringType.INSTANCE;
    }

    @Override
    public Node copyWithType(WireType<?> type) {
        return new SetBlockTagNode<>(type);
    }

}