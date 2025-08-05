package de.blazemcworld.fireflow.code.widget;

import com.google.gson.JsonObject;
import de.blazemcworld.fireflow.code.CodeInteraction;
import de.blazemcworld.fireflow.util.TextWidth;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Color;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;
import java.util.UUID;

public class TextWidget extends Widget {

    private TextDisplay display;
    private double xScale = 1;
    private double yScale = 1;
    private int rotation = 0;
    private Component text;
    private final String webUUID = UUID.randomUUID().toString();

    public TextWidget(WidgetVec pos) {
        super(pos);
    }

    public TextWidget(WidgetVec pos, Component text) {
        this(pos);
        setText(text);
    }

    public void setText(Component text) {
        if (display != null) display.text(text);
        this.text = text;
    }

    @Override
    public void update() {
        WidgetVec pos = pos();
        WidgetVec size = size();

        pos = pos.add(-size.x() / 2.0, -size.y());

        if (display == null || !display.isValid()) {
            display = pos.world().createEntity(pos.loc(), TextDisplay.class);
            display.text(text);
            display.setBackgroundColor(Color.fromARGB(0));
            display.setLineWidth(Integer.MAX_VALUE);
            display.setInterpolationDuration(1);
            display.setTeleportDuration(1);
            display.setTransformation(new Transformation(
                    display.getTransformation().getTranslation(),
                    new Quaternionf(0, 0, (float) Math.sin(Math.toRadians(rotation) * 0.5), (float) Math.cos(Math.toRadians(rotation) * 0.5)),
                    new Vector3f((float) xScale, (float) yScale, 1),
                    display.getTransformation().getRightRotation()
            ));
            display.spawnAt(pos.loc());
        } else {
            display.teleport(pos.loc());
        }

        JsonObject json = new JsonObject();
        json.addProperty("type", "text");
        json.addProperty("id", webUUID);
        json.addProperty("x", pos().x());
        json.addProperty("y", pos().y());
        json.addProperty("text", getPlainText(text));
        json.addProperty("scaleX", xScale);
        json.addProperty("scaleY", yScale);
        json.addProperty("rotation", rotation);
        TextColor c = text.style().color();
        json.addProperty("color", c == null ? "" : c.asHexString());
        pos.editor().webBroadcast(json);
    }

    private String getPlainText(Component text) {
        StringBuilder builder = new StringBuilder();
        if (text instanceof TextComponent plain) {
            builder.append(plain.content());
        }
        for (Component child : text.children()) {
            builder.append(getPlainText(child));
        }
        return builder.toString();
    }

    @Override
    public void remove() {
        if (display == null) return;
        display.remove();
        JsonObject json = new JsonObject();
        json.addProperty("type", "remove");
        json.addProperty("id", webUUID);
        pos().editor().webBroadcast(json);
    }

    @Override
    public List<Widget> getChildren() {
        return List.of();
    }

    @Override
    public WidgetVec size() {
        return new WidgetVec(pos().editor(), TextWidth.calculate(text) / 40.0 * xScale, 0.25 * yScale);
    }

    public TextWidget stretch(double x, double y) {
        xScale = x;
        yScale = y;
        if (display == null) return this;

        display.setTransformation(new Transformation(
                display.getTransformation().getTranslation(),
                display.getTransformation().getLeftRotation(),
                new Vector3f((float) xScale, (float) yScale, 1),
                display.getTransformation().getRightRotation()
        ));
        return this;
    }

    public void setRotation(int deg) {
        this.rotation = deg;
        if (display == null) return;

        double rotation = Math.toRadians(deg);
        display.setTransformation(new Transformation(
                display.getTransformation().getTranslation(),
                new Quaternionf(0, 0, (float) Math.sin(rotation * 0.5), (float) Math.cos(rotation * 0.5)),
                display.getTransformation().getScale(),
                display.getTransformation().getRightRotation()
        ));
    }

    @Override
    public boolean interact(CodeInteraction i) {
        return false;
    }
}
