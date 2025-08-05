package de.blazemcworld.fireflow.code.widget;

import com.google.gson.JsonObject;
import de.blazemcworld.fireflow.code.CodeInteraction;
import org.bukkit.Material;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.joml.Vector3f;

import java.util.List;
import java.util.UUID;

public class ItemWidget extends Widget {

    private ItemDisplay display;
    private final ItemStack item;
    private final double size;
    private final String webUUID = UUID.randomUUID().toString();

    public ItemWidget(WidgetVec pos, ItemStack item, double size) {
        super(pos);
        this.size = size;
        this.item = item;
    }

    public ItemWidget(WidgetVec pos, Material type) {
        this(pos, new ItemStack(type), 0.25);
    }

    @Override
    public WidgetVec size() {
        return new WidgetVec(pos().editor(), size, size);
    }

    @Override
    public void update() {
        if (display == null || !display.isValid()) {
            display = pos().world().createEntity(pos().loc(), ItemDisplay.class);
            display.setItemStack(item);
            display.setInterpolationDuration(1);
            display.setTeleportDuration(1);
            display.spawnAt(pos().loc().add(0, 0, -0.001));
        } else {
            display.teleport(pos().loc().add(0, 0, -0.001));
        }
        display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.GUI);
        display.setTransformation(new Transformation(
                new Vector3f((float) size / 2, (float) -size / 2, 0),
                display.getTransformation().getLeftRotation(),
                new Vector3f((float) -size, (float) size, -0.001f),
                display.getTransformation().getRightRotation()
        ));

        JsonObject json = new JsonObject();
        json.addProperty("type", "item");
        json.addProperty("id", webUUID);
        json.addProperty("x", pos().x());
        json.addProperty("y", pos().y());
        json.addProperty("size", size);
        json.addProperty("item", display.getItemStack().getType().key().value());
        pos().editor().webBroadcast(json);
    }

    @Override
    public List<Widget> getChildren() {
        return List.of();
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
    public boolean interact(CodeInteraction i) {
        return false;
    }
}
