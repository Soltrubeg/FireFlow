package de.blazemcworld.fireflow.code.node.option;

import de.blazemcworld.fireflow.code.CodeInteraction;
import de.blazemcworld.fireflow.code.widget.ChoiceWidget;
import de.blazemcworld.fireflow.code.widget.NodeIOWidget;
import de.blazemcworld.fireflow.code.widget.WidgetVec;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.NamespacedKey;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class EnchantmentOptions implements InputOptions {

    public static final EnchantmentOptions INSTANCE = new EnchantmentOptions();
    private final List<String> effects = new ArrayList<>();

    private EnchantmentOptions() {
        for (Iterator<NamespacedKey> it = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).keyStream().iterator(); it.hasNext(); ) {
            effects.add(it.next().value());
        }
        effects.sort(String::compareTo);
    }

    @Override
    public boolean handleRightClick(Consumer<String> update, NodeIOWidget io, CodeInteraction i) {
        WidgetVec pos = io.pos().add(2.5, 0);
        ChoiceWidget w = new ChoiceWidget(pos, effects, update);
        pos.editor().rootWidgets.add(w);
        w.update();
        return true;
    }

    @Override
    public String fallback() {
        return "sharpness";
    }
}
