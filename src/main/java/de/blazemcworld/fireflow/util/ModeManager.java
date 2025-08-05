package de.blazemcworld.fireflow.util;

import de.blazemcworld.fireflow.code.EditOrigin;
import de.blazemcworld.fireflow.space.Lobby;
import de.blazemcworld.fireflow.space.Space;
import de.blazemcworld.fireflow.space.SpaceManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.WeakHashMap;

public class ModeManager {

    private static final WeakHashMap<Player, Mode> modes = new WeakHashMap<>();

    public static Mode getFor(Player player) {
        synchronized (modes) {
            return modes.getOrDefault(player, Mode.LOBBY);
        }
    }

    private static void setMode(Player player, Mode mode) {
        synchronized (modes) {
            modes.put(player, mode);
        }
    }

    public static void move(Player player, Mode modeArg, Space spaceArg) {
        handleExit(player);

        Statistics.reset(player);

        Space space = spaceArg;
        Mode mode = modeArg;

        space = space != null ? space : SpaceManager.getSpaceForPlayer(player);
        if (space == null) mode = Mode.LOBBY;

        if (mode == Mode.LOBBY) {
            player.teleport(new Location(Lobby.world, 0, 0, 0));
            setMode(player, Mode.LOBBY);
            Lobby.onSpawn(player);
            return;
        }

        if (mode == Mode.CODE) {
            player.teleport(new Location(space.codeWorld, 0, 0, 0));
            setMode(player, Mode.CODE);
            space.editor.enterCode(EditOrigin.ofPlayer(player));
            return;
        }

        if (mode == Mode.BUILD) {
            player.teleport(new Location(space.playWorld, 0, 0, 0));
            setMode(player, Mode.BUILD);
            space.enterBuild(player);
            PermissionHelper.giveWorldEdit(player, space.playWorld);
            return;
        }

        Space lambdaSpace = space;
        player.teleport(new Location(space.playWorld, 0, 0, 0));
        setMode(player, Mode.PLAY);
        lambdaSpace.enterPlay(player);
    }

    public static void handleExit(Player player) {
        Space space = SpaceManager.getSpaceForPlayer(player);
        if (space == null) return;
        Mode mode = getFor(player);

        if (mode == Mode.BUILD) {
            PermissionHelper.takeWorldEdit(player, space.playWorld);
        }
        if (mode == Mode.CODE) {
            space.editor.exitCode(EditOrigin.ofPlayer(player));
        }
        if (mode == Mode.PLAY) {
            space.evaluator.exitPlay(player);
        }
    }

    public static void onJoinedServer(Player player) {
        setMode(player, Mode.LOBBY);
    }

    public enum Mode {
        PLAY,
        BUILD,
        CODE,
        LOBBY
    }

}
