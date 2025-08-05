package de.blazemcworld.fireflow.space;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.blazemcworld.fireflow.FireFlow;
import de.blazemcworld.fireflow.code.CodeChunkGenerator;
import de.blazemcworld.fireflow.code.CodeEditor;
import de.blazemcworld.fireflow.code.CodeEvaluator;
import de.blazemcworld.fireflow.code.VariableStore;
import de.blazemcworld.fireflow.util.FlatChunkGenerator;
import de.blazemcworld.fireflow.util.ModeManager;
import de.blazemcworld.fireflow.util.WorldUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

public class Space {
    public final SpaceInfo info;
    public final World playWorld;
    public final World codeWorld;
    public final CodeEditor editor;
    public final VariableStore savedVariables;
    private int emptyTimer = 0;
    public CodeEvaluator evaluator;
    private boolean unloaded = false;
    public final DummyManager dummyManager;

    public Space(SpaceInfo info) {
        this.info = info;
        playWorld = WorldCreator.name("play-" + info.id)
                .generator(new FlatChunkGenerator())
                .createWorld();
        codeWorld = WorldCreator.name("code-" + info.id)
                .generator(new CodeChunkGenerator())
                .createWorld();
        codeWorld.setAutoSave(false);

        WorldUtil.init(playWorld);
        WorldUtil.init(codeWorld);
        editor = new CodeEditor(this, codeWorld);
        savedVariables = new VariableStore();
        try {
            if (!Files.exists(path())) Files.createDirectories(path());
        } catch (IOException e) {
            FireFlow.logger.log(Level.WARNING, "Failed to create directory for space " + info.id, e);
        }

        if (Files.exists(path().resolve("variables.json"))) {
            try {
                savedVariables.load(JsonParser.parseString(Files.readString(path().resolve("variables.json"))).getAsJsonObject());
            } catch (IOException e) {
                FireFlow.logger.log(Level.WARNING, "Failed to load variables.json for space " + info.id, e);
            }
        }

        editor.load();
        evaluator = new CodeEvaluator(this);
        dummyManager = new DummyManager(this);
    }

    public void save() {
        JsonObject vars = savedVariables.toJson();
        try {
            Files.writeString(path().resolve("variables.json"), vars.toString());
        } catch (IOException e) {
            FireFlow.logger.log(Level.WARNING, "Failed to save variables.json for space " + info.id, e);
        }
        editor.save();
    }

    public boolean isInactive() {
        return emptyTimer > 100;
    }

    protected void unload() {
        dummyManager.reset();
        for (Player player : new ArrayList<>(playWorld.getPlayers())) {
            ModeManager.move(player, ModeManager.Mode.LOBBY, this);
        }
        for (Player player : new ArrayList<>(codeWorld.getPlayers())) {
            ModeManager.move(player, ModeManager.Mode.LOBBY, this);
        }
        editor.close();
        evaluator.stop();

        Bukkit.unloadWorld(playWorld, true);
        Bukkit.unloadWorld(codeWorld, true);
    }

    public Set<Player> getPlayers() {
        HashSet<Player> out = new HashSet<>();
        out.addAll(playWorld.getPlayers());
        out.addAll(codeWorld.getPlayers());
        out.removeIf(DummyManager::isDummy);
        return out;
    }

    public void tick() {
        if (getPlayers().isEmpty()) {
            emptyTimer++;
        } else {
            emptyTimer = 0;
        }
    }

    public void enterPlay(Player player) {
        evaluator.onJoin(player);
    }

    public void enterBuild(Player player) {
        player.setGameMode(GameMode.CREATIVE);
    }

    public Path path() {
        return Path.of("spaces").resolve(String.valueOf(info.id));
    }

    public void reload() {
        dummyManager.reset();
        for (Player player : new ArrayList<>(playWorld.getPlayers())) {
            if (info.isOwnerOrDeveloper(player.getUniqueId())) {
                ModeManager.move(player, ModeManager.Mode.CODE, this);
            } else {
                ModeManager.move(player, ModeManager.Mode.LOBBY, this);
            }
        }
        evaluator.stop();
        for (Chunk c : playWorld.getLoadedChunks()) {
            playWorld.unloadChunk(c.getX(), c.getZ());
        }
        evaluator = new CodeEvaluator(this);
    }

    public boolean ownsWorld(World w) {
        return w == playWorld || w == codeWorld;
    }

    public boolean isUnloaded() {
        return unloaded;
    }
}
