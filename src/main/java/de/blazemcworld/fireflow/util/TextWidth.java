package de.blazemcworld.fireflow.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.blazemcworld.fireflow.FireFlow;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextDecoration;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class TextWidth {

    private static final HashMap<String, Info> known = new HashMap<>();
    private static Info missing;

    public static void init() {
        try (InputStream stream = FireFlow.instance.getResource("fontwidth.json")) {
            String raw = new String(stream.readAllBytes());

            JsonObject parsed = JsonParser.parseString(raw).getAsJsonObject();

            missing = new Gson().fromJson(parsed.get("missing_char"), Info.class);

            for (Map.Entry<String, JsonElement> entry : parsed.get("chars").getAsJsonObject().entrySet()) {
                known.put(entry.getKey(), new Gson().fromJson(entry.getValue(), Info.class));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static double calculate(Component text) {
        return calculate(text, false);
    }

    public static double calculate(Component text, boolean bold) {
        if (text.style().decoration(TextDecoration.BOLD) != TextDecoration.State.NOT_SET) {
            bold = text.style().decoration(TextDecoration.BOLD) == TextDecoration.State.TRUE;
        }

        double width = 0;
        if (text instanceof TextComponent plain) {
            width += calculate(plain.content(), bold);
        }
        for (Component child : text.children()) {
            width += calculate(child, bold);
        }
        return width;
    }

    public static double calculate(String text, boolean bold) {
        double width = 0;

        for (String s : text.split("")) {
            Info info = known.getOrDefault(s, missing);
            width += info.width;
            if (bold) width += info.bold;
        }

        return width;
    }

    private record Info(double width, double bold) {}
}
