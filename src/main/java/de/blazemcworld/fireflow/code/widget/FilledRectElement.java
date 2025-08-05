package de.blazemcworld.fireflow.code.widget;

import com.google.gson.JsonObject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Color;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Transformation;
import org.joml.Vector3f;

import java.util.UUID;

public class FilledRectElement {
    
    public WidgetVec pos;
    public WidgetVec size;
    public int color;
    private TextDisplay display;
    private final String webUUID = UUID.randomUUID().toString();

    public FilledRectElement(WidgetVec pos, int color) {
        this.pos = pos;
        this.color = color;
    }

    public void update() {

        if (display == null || !display.isValid()) {
            display = pos.world().createEntity(pos.loc(), TextDisplay.class);
            display.setBackgroundColor(Color.fromARGB(color));
            display.text(Component.text(" "));
            display.setLineWidth(Integer.MAX_VALUE);
            display.setInterpolationDuration(1);
            display.setTeleportDuration(1);
        }

        display.setTransformation(new Transformation(
                display.getTransformation().getTranslation(),
                display.getTransformation().getLeftRotation(),
                new Vector3f((float) size.x() * 8, (float) size.y() * 4, 1),
                display.getTransformation().getRightRotation()
        ));

        if (display.isValid()) {
            display.teleport(pos.sub(size.x() / 2.5, size.y()).loc());
        } else {
            display.spawnAt(pos.sub(size.x() / 2.5, size.y()).loc());
        }

        display.setBackgroundColor(Color.fromARGB(color));

        JsonObject json = new JsonObject();
        json.addProperty("type", "filled-rect");
        json.addProperty("id", webUUID);
        json.addProperty("x", pos.x());
        json.addProperty("y", pos.y());
        json.addProperty("width", size.x());
        json.addProperty("height", size.y());
        json.addProperty("color", TextColor.color(color).asHexString());
        pos.editor().webBroadcast(json);
    }

    public void remove() {
        if (display == null) return;
        display.remove();

        JsonObject json = new JsonObject();
        json.addProperty("type", "remove");
        json.addProperty("id", webUUID);
        pos.editor().webBroadcast(json);
    }
}