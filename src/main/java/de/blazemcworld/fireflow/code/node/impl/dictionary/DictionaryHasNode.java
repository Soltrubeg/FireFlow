package de.blazemcworld.fireflow.code.node.impl.dictionary;

import de.blazemcworld.fireflow.code.node.DualGenericNode;
import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.ConditionType;
import de.blazemcworld.fireflow.code.type.DictionaryType;
import de.blazemcworld.fireflow.code.type.WireType;
import de.blazemcworld.fireflow.code.value.DictionaryValue;
import org.bukkit.Material;

public class DictionaryHasNode<K, V> extends DualGenericNode<K, V> {

    public DictionaryHasNode(WireType<K> type1, WireType<V> type2) {
        super("dictionary_has", type1 == null || type2 == null ? "Dictionary Has" : type1.getName() + " " + type2.getName() + " Dictionary Has", "Checks if a dictionary has a key.", Material.ARROW, type1, type2);

        Input<DictionaryValue<K, V>> dict = new Input<>("dictionary", "Dictionary", DictionaryType.of(type1, type2));
        Input<K> key = new Input<>("key", "Key", type1);

        Output<Boolean> found = new Output<>("found", "Found", ConditionType.INSTANCE);

        found.valueFrom((ctx) -> dict.getValue(ctx).has(key.getValue(ctx)));
    }

    @Override
    public Node copy() {
        return new DictionaryHasNode<>(type1, type2);
    }

    @Override
    public Node copyWithType(WireType<?> type1, WireType<?> type2) {
        return new DictionaryHasNode<>(type1, type2);
    }
}


