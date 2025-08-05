package de.blazemcworld.fireflow.space;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.blazemcworld.fireflow.FireFlow;
import org.apache.commons.io.FileUtils;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.logging.Level;

public class SpaceManager implements Listener {

    private static final HashMap<Integer, Space> spaces = new HashMap<>();
    public static final HashMap<Integer, SpaceInfo> info = new HashMap<>();
    private static final WeakHashMap<World, Space> world2space = new WeakHashMap<>();
    public static int lastId = 0;
    private static int nextSave = 200;

    public static void save(boolean stop) {
        try {
            JsonObject data = new JsonObject();
            JsonObject spaces = new JsonObject();
            for (SpaceInfo spaceInfo : info.values()) {
                JsonObject space = new JsonObject();
                space.addProperty("name", spaceInfo.name);
                space.addProperty("icon", spaceInfo.icon.key().value());
                space.addProperty("owner", spaceInfo.owner.toString());
                JsonArray developers = new JsonArray();
                for (UUID contributor : spaceInfo.developers) {
                    developers.add(contributor.toString());
                }
                space.add("developers", developers);
                JsonArray builders = new JsonArray();
                for (UUID contributor : spaceInfo.builders) {
                    builders.add(contributor.toString());
                }
                space.add("builders", builders);
                spaces.add(String.valueOf(spaceInfo.id), space);
            }
            data.add("spaces", spaces);
            data.addProperty("lastId", lastId);
            
            Files.writeString(Path.of("spaces.json"), data.toString());
        } catch (IOException e) {
            FireFlow.logger.log(Level.WARNING, "Failed to save spaces.json!", e);
        }

        for (Space space : getLoadedSpaces()) {
            space.save();
            if (space.isInactive() || stop) unloadSpace(space);
        }
    }

    @EventHandler
    public void onTick(ServerTickEndEvent event) {
        nextSave--;
        for (Space s : spaces.values()) s.tick();
        if (nextSave > 0) return;
        nextSave = 200;
        save(false);

    }

    public static void unloadSpace(Space space) {
        spaces.remove(space.info.id);
        space.unload();
        FireFlow.logger.info("Unloading space " + space.info.id);
    }

    public static void load() {
        try {
            if (!Files.exists(Path.of("spaces.json"))) return;

            JsonObject data = JsonParser.parseString(Files.readString(Path.of("spaces.json"))).getAsJsonObject();

            lastId = data.get("lastId").getAsInt();
            JsonObject spaces = data.getAsJsonObject("spaces");
            for (Map.Entry<String, JsonElement> raw : spaces.entrySet()) {
                JsonObject space = raw.getValue().getAsJsonObject();
                SpaceInfo spaceInfo = new SpaceInfo(Integer.parseInt(raw.getKey()));
                spaceInfo.name = space.get("name").getAsString();
                spaceInfo.icon = Material.getMaterial(space.get("icon").getAsString());
                if (spaceInfo.icon == null) spaceInfo.icon = Material.PAPER;
                spaceInfo.owner = UUID.fromString(space.get("owner").getAsString());
                spaceInfo.developers = new HashSet<>();
                spaceInfo.builders = new HashSet<>();
                for (JsonElement dev : space.has("contributors") ? space.getAsJsonArray("contributors") : space.get("developers").getAsJsonArray()) {
                    spaceInfo.developers.add(UUID.fromString(dev.getAsString()));
                }
                if (space.has("builders")) {
                    for (JsonElement builder : space.getAsJsonArray("builders")) {
                        spaceInfo.builders.add(UUID.fromString(builder.getAsString()));
                    }
                }
                info.put(spaceInfo.id, spaceInfo);
            }
        } catch (IOException e) {
            FireFlow.logger.log(Level.WARNING, "Failed to load spaces.json!", e);
        }
    }

    public static Space getOrLoadSpace(SpaceInfo info) {
       Space space = spaces.get(info.id);
        if (space == null) {
            FireFlow.logger.info("Loading space " + info.id);
            space = new Space(info);
            spaces.put(info.id, space);
        }
        return space;
    }

    public static Space getSpaceForPlayer(Player player) {
        return getSpaceForWorld(player.getWorld());
    }

    public static Space getSpaceForWorld(World world) {
        return world2space.computeIfAbsent(world, w -> {
            for (Space s : getLoadedSpaces()) {
                if (s.ownsWorld(w)) return s;
            }
            return null;
        });
    }

    public static List<Space> activeSpaces() {
        List<Space> out = new ArrayList<>();
        for (Space s : spaces.values()) {
            if (!s.getPlayers().isEmpty()) out.add(s);
        }
        return out;
    }

    public static List<SpaceInfo> getOwnedSpaces(Player player) {
        List<SpaceInfo> out = new ArrayList<>();
        for (SpaceInfo i : info.values()) {
            if (i.owner.equals(player.getUniqueId())) out.add(i);
        }
        return out;
    }

    public static Space getIfLoaded(SpaceInfo info) {
        return spaces.get(info.id);
    }

    public static SpaceInfo getInfo(int id) {
        return info.get(id);
    }

    public static List<Space> getLoadedSpaces() {
        return new ArrayList<>(spaces.values());
    }

    public static void delete(Space space) {
        unloadSpace(space);
        try {
            FileUtils.deleteDirectory(space.path().toFile());

            FileUtils.deleteDirectory(new File("play-" + space.info.id));
            FileUtils.deleteDirectory(new File("code-" + space.info.id));

            info.remove(space.info.id);
        } catch (IOException e) {
            FireFlow.logger.log(Level.WARNING, "Failed to delete space " + space.info.id, e);
        }
    }
}
