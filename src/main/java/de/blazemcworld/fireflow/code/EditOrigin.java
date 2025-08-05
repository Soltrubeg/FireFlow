package de.blazemcworld.fireflow.code;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.blazemcworld.fireflow.code.web.WebEditor;
import de.blazemcworld.fireflow.code.widget.WidgetVec;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.WeakHashMap;

public class EditOrigin {

    private static final WeakHashMap<Player, EditOrigin> playerOrigins = new WeakHashMap<>();
    private static final WeakHashMap<WebEditor.WebUser, EditOrigin> webOrigins = new WeakHashMap<>();

    private Player player;
    private final WebEditor.WebUser web;

    private EditOrigin(Player player, WebEditor.WebUser web) {
        this.player = player;
        this.web = web;
    }

    public static EditOrigin ofPlayer(Player player) {
        EditOrigin origin = playerOrigins.computeIfAbsent(player, p -> new EditOrigin(p, null));
        origin.player = player; // ServerPlayerEntities can be recreated, having the same hash code
        return origin;
    }

    public static EditOrigin ofWeb(WebEditor.WebUser web) {
        return webOrigins.computeIfAbsent(web, w -> new EditOrigin(null, w));
    }

    public @Nullable Player getPlayer() {
        return player;
    }

    public Optional<WidgetVec> getCursor(CodeEditor editor) {
        if (web == null) return Optional.empty();
        WidgetVec cursor = web.cursor;
        if (cursor == null) return Optional.empty();
        return Optional.of(new WidgetVec(editor, cursor.x(), cursor.y()));
    }

    public void sendError(String msg) {
        if (player != null) player.sendMessage(Component.text(msg).color(NamedTextColor.RED));
        if (web != null) web.sendError(msg);
    }

    public void sendInfo(String msg) {
        if (player != null) player.sendMessage(Component.text(msg).color(NamedTextColor.YELLOW));
        if (web != null) web.sendInfo(msg);
    }

    public void sendSnippet(String data) {
        if (player != null) {
            player.sendMessage(Component.text("Snippet created! Click to copy.").style(
                    Style.empty().clickEvent(ClickEvent.copyToClipboard(data)).color(NamedTextColor.AQUA)
            ));
        }
        if (web != null) {
            JsonObject json = new JsonObject();
            json.addProperty("type", "snippet");
            json.addProperty("data", data);
            web.send(json);
        }
    }

    public boolean isWeb() {
        return web != null;
    }

    public boolean tryAuth(String webId) {
        if (!isWeb()) return false;
        return web.tryAuth(webId);
    }

    public void sendWebIfAuthorized(JsonElement json) {
        if (!isWeb()) return;
        if (web.isAuthorized()) web.send(json);
    }

    public void disconnectWeb() {
        if (!isWeb()) return;
        web.doClose();
    }

    public void tick() {
        if (web != null) {
            web.tick();
        }
    }
}
