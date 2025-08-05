package de.blazemcworld.fireflow.code.widget;

import com.google.gson.JsonObject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.UUID;

public class LineElement {

    public WidgetVec from;
    public WidgetVec to;
    private TextDisplay display;
    private final String webUUID = UUID.randomUUID().toString();
    private TextColor color = NamedTextColor.WHITE;

    public LineElement(WidgetVec pos) {
        from = pos;
        to = pos;
    }

    public void update() {
        double dist = from.distance(to);

        float angle = (float) Math.atan2(to.y() - from.y(), from.x() - to.x());

        Location loc = new WidgetVec(
                from.editor(),
                (from.x() + to.x()) / 2 + (Math.cos(angle) * dist * 0.1 - Math.sin(angle) * 0.135),
                (from.y() + to.y()) / 2 + (-Math.sin(angle) * dist * 0.1 - Math.cos(angle) * 0.135)
        ).loc();

        if (display == null || !display.isValid()) {
            display = loc.getWorld().createEntity(loc, TextDisplay.class);
            display.setBackgroundColor(Color.fromARGB(0));
            display.setLineWidth(Integer.MAX_VALUE);
            display.setInterpolationDuration(1);
            display.setTeleportDuration(1);
            display.text(Component.text("-").color(color));
            display.spawnAt(loc);
        } else {
            display.teleport(loc);
        }

        display.setTransformation(new Transformation(
                new Vector3f(),
                new Quaternionf(0, 0, Math.sin(angle * 0.5), (float) Math.cos(angle * 0.5)),
                new Vector3f((float) dist * 8, 1, 1),
                new Quaternionf()
        ));

        sendWeb();
    }

    public void remove() {
        if (display == null) return;
        display.remove();

        JsonObject json = new JsonObject();
        json.addProperty("type", "remove");
        json.addProperty("id", webUUID);
        from.editor().webBroadcast(json);
    }

    public void color(TextColor color) {
        this.color = color;
        if (display == null) return;
        display.text(Component.text("-").color(color));
        sendWeb();
    }

    private void sendWeb() {
        JsonObject json = new JsonObject();
        json.addProperty("type", "line");
        json.addProperty("id", webUUID);
        json.addProperty("fromX", from.x());
        json.addProperty("fromY", from.y());
        json.addProperty("toX", to.x());
        json.addProperty("toY", to.y());
        json.addProperty("color", color == null ? "" : color.asHexString());
        from.editor().webBroadcast(json);
    }
}
