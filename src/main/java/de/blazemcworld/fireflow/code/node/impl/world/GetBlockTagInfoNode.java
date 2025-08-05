package de.blazemcworld.fireflow.code.node.impl.world;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.node.SingleGenericNode;
import de.blazemcworld.fireflow.code.type.*;
import de.blazemcworld.fireflow.code.value.ListValue;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.bukkit.Material;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class GetBlockTagInfoNode<T> extends SingleGenericNode<T> {

    public GetBlockTagInfoNode(WireType<T> type) {
        super("get_block_tag_info", type == null ? "Get Block Tag Info" : "Get " + type.getName() + " Block Tag Info", "Gets the current value and all valid options of a block's tag", Material.OAK_STAIRS, type);

        Input<Vector> position = new Input<>("position", "Position", VectorType.INSTANCE);
        Input<String> property = new Input<>("property", "Property", StringType.INSTANCE);
        Output<T> value = new Output<>("value", "Value", type);
        Output<ListValue<T>> allOptions = new Output<>("all_options", "All Options", ListType.of(type));

        value.valueFrom((ctx) -> {
            String propertyName = property.getValue(ctx);
            CraftBlockState state = (CraftBlockState) ctx.evaluator.world.getBlockState(position.getValue(ctx).toLocation(ctx.evaluator.world));
            for (Property<?> prop : state.getHandle().getProperties()) {
                if (prop.getName().equals(propertyName)) {
                    Comparable<?> propertyValue = state.getHandle().getValue(prop);
                    switch (propertyValue) {
                        case Integer intValue -> {
                            return type.convert(NumberType.INSTANCE, intValue.doubleValue());
                        }
                        case StringRepresentable enumValue -> {
                            String stringValue = enumValue.getSerializedName();
                            return type.convert(StringType.INSTANCE, stringValue);
                        }
                        case Boolean boolValue -> {
                            return type.convert(ConditionType.INSTANCE, boolValue);
                        }
                        default -> {}
                    }
                    break;
                }
            }
            return type.defaultValue();
        });

        allOptions.valueFrom((ctx) -> {
            String propertyName = property.getValue(ctx);
            CraftBlockState state = (CraftBlockState) ctx.evaluator.world.getBlockState(position.getValue(ctx).toLocation(ctx.evaluator.world));
            for (Property<?> prop : state.getHandle().getProperties()) {
                if (prop.getName().equals(propertyName)) {
                    switch (prop) {
                        case IntegerProperty intProperty -> {
                            List<T> list = new ArrayList<>();
                            for (Integer intValue : intProperty.getPossibleValues()) {
                                list.add(type.convert(NumberType.INSTANCE, Double.valueOf(intValue)));
                            }
                            return new ListValue<>(type, list);
                        }
                        case EnumProperty<?> enumProperty -> {
                            List<T> list = new ArrayList<>();
                            for (StringRepresentable stringIdentifiable : enumProperty.getPossibleValues()) {
                                list.add(type.convert(StringType.INSTANCE, stringIdentifiable.getSerializedName()));
                            }
                            return new ListValue<>(type, list);
                        }
                        case BooleanProperty booleanProperty -> {
                            List<T> list = new ArrayList<>();
                            for (boolean boolValue : booleanProperty.getPossibleValues()) {
                                list.add(type.convert(ConditionType.INSTANCE, boolValue));
                            }
                            return new ListValue<>(type, list);
                        }
                        default -> {}
                    }
                    break;
                }
            }
            return allOptions.type.defaultValue();
        });
    }

    @Override
    public Node copy() {
        return new GetBlockTagInfoNode<>(type);
    }

    @Override
    public boolean acceptsType(WireType<?> type, int index) {
        return type == ConditionType.INSTANCE || type == NumberType.INSTANCE || type == StringType.INSTANCE;
    }

    @Override
    public Node copyWithType(WireType<?> type) {
        return new GetBlockTagInfoNode<>(type);
    }

}