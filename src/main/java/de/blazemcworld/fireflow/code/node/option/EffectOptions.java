package de.blazemcworld.fireflow.code.node.option;

import de.blazemcworld.fireflow.code.CodeInteraction;
import de.blazemcworld.fireflow.code.widget.ChoiceWidget;
import de.blazemcworld.fireflow.code.widget.NodeIOWidget;
import de.blazemcworld.fireflow.code.widget.WidgetVec;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class EffectOptions implements InputOptions {

    public static final EffectOptions INSTANCE = new EffectOptions();
    private final List<String> effects = new ArrayList<>();

    private EffectOptions() {
        for (Iterator<NamespacedKey> it = Registry.POTION.keyStream().iterator(); it.hasNext(); ) {
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
        return "speed";
    }
}
