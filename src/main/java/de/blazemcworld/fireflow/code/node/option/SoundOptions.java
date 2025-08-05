package de.blazemcworld.fireflow.code.node.option;

import de.blazemcworld.fireflow.code.CodeInteraction;
import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.widget.ChoiceWidget;
import de.blazemcworld.fireflow.code.widget.NodeIOWidget;
import de.blazemcworld.fireflow.code.widget.WidgetVec;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;

import java.util.*;
import java.util.function.Consumer;

public class SoundOptions implements InputOptions {

    public static final SoundOptions INSTANCE = new SoundOptions();
    private final List<String> soundIds = new ArrayList<>();

    private SoundOptions() {
        for (Iterator<NamespacedKey> it = Registry.SOUNDS.keyStream().iterator(); it.hasNext(); ) {
            soundIds.add(it.next().value());
        }
    }

    @Override
    public String fallback() {
        return "block.note_block.pling";
    }

    @Override
    public boolean handleRightClick(Consumer<String> update, NodeIOWidget io, CodeInteraction i) {
        openSelector(io.pos().add(2.5, 0), io.input, update, "back_if_empty");
        return true;
    }

    private void openSelector(WidgetVec pos, Node.Input<?> input, Consumer<String> update, String mode) {
        Set<String> choices = new HashSet<>();
        String current = input.inset;
        for (String sound : soundIds) {
            if (!sound.startsWith(current)) continue;
            String part = sound.substring(current.length());
            if (part.startsWith(".")) continue;
            int nextDot = part.indexOf('.');
            if (nextDot != -1) {
                part = part.substring(0, nextDot + 1);
            }
            if (part.isEmpty()) continue;
            choices.add(part);
        }

        if (choices.isEmpty()) {
            if (mode.equals("hide_if_empty")) return;
            if (mode.equals("back_if_empty")) {
                String newValue = current.substring(0, current.length() - 1);
                int lastDot = newValue.lastIndexOf('.');
                if (lastDot != -1) {
                    update.accept(newValue.substring(0, lastDot + 1));
                } else {
                    update.accept("");
                }
                openSelector(pos, input, update, "hide_if_empty");
                return;
            }
        }

        List<String> list = new ArrayList<>(choices);
        list.sort(String::compareTo);

        if (!current.isEmpty()) {
            list.addFirst("🠄 Back");
        }

        ChoiceWidget w = new ChoiceWidget(pos, list, (choice) -> {
            String newValue = current + choice;
            if (choice.equals("🠄 Back")) {
                newValue = current.substring(0, current.length() - 1);
                int lastDot = newValue.lastIndexOf('.');
                if (lastDot != -1) {
                    newValue = newValue.substring(0, lastDot + 1);
                } else {
                    newValue = "";
                }
            }
            update.accept(newValue);
            openSelector(pos, input, update, "hide_if_empty");
        });
        pos.editor().rootWidgets.add(w);
        w.update();
    }
}
