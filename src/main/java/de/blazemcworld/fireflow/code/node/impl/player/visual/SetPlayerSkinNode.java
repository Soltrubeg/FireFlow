package de.blazemcworld.fireflow.code.node.impl.player.visual;

import com.destroystokyo.paper.profile.PlayerProfile;
import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.PlayerType;
import de.blazemcworld.fireflow.code.type.SignalType;
import de.blazemcworld.fireflow.code.type.StringType;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.profile.PlayerTextures;

import java.util.UUID;
import java.util.WeakHashMap;

public class SetPlayerSkinNode extends Node {

    private static final WeakHashMap<Player, PlayerTextures> realTextures = new WeakHashMap<>();

    public SetPlayerSkinNode() {
        super("set_player_skin", "Set Player Skin", "Changes the displayed skin of a player", Material.LEATHER_HELMET);

        Input<Void> signal = new Input<>("signal", "Signal", SignalType.INSTANCE);
        Input<PlayerValue> player = new Input<>("player", "Player", PlayerType.INSTANCE);
        Input<String> skin = new Input<>("skin", "Skin", StringType.INSTANCE);
        Input<String> mode = new Input<>("mode", "Mode", StringType.INSTANCE)
                .options("name", "uuid", "reset");
        Output<Void> next = new Output<>("next", "Next", SignalType.INSTANCE);

        signal.onSignal((ctx) -> {
            String m = mode.getValue(ctx);
            switch (m) {
                case "name":
                    player.getValue(ctx).tryUse(ctx, p -> {
                        PlayerProfile prof = p.getPlayerProfile();
                        realTextures.computeIfAbsent(p, u -> prof.update().join().getTextures());
                        OfflinePlayer offline = Bukkit.getOfflinePlayer(skin.getValue(ctx));
                        if (offline.getPlayer() == null) {
                            prof.setTextures(offline.getPlayerProfile().update().join().getTextures());
                        } else {
                            prof.setTextures(realTextures.computeIfAbsent(offline.getPlayer(), u -> offline.getPlayerProfile().update().join().getTextures()));
                        }
                        p.setPlayerProfile(prof);
                    });
                case "uuid":
                    player.getValue(ctx).tryUse(ctx, p -> {
                        try {
                            PlayerProfile prof = p.getPlayerProfile();
                            realTextures.computeIfAbsent(p, u -> prof.update().join().getTextures());
                            OfflinePlayer offline = Bukkit.getOfflinePlayer(UUID.fromString(skin.getValue(ctx)));
                            if (offline.getPlayer() == null) {
                                prof.setTextures(offline.getPlayerProfile().update().join().getTextures());
                            } else {
                                prof.setTextures(realTextures.computeIfAbsent(offline.getPlayer(), u -> offline.getPlayerProfile().update().join().getTextures()));
                            }
                            p.setPlayerProfile(prof);
                        } catch (IllegalArgumentException ignore) {
                        }
                    });
                    break;

                case "reset":
                    player.getValue(ctx).tryUse(ctx, SetPlayerSkinNode::reset);
                    break;
            }
            ctx.sendSignal(next);
        });
    }

    @Override
    public Node copy() {
        return new SetPlayerSkinNode();
    }

    public static void reset(Player p) {
        PlayerProfile prof = p.getPlayerProfile();
        realTextures.computeIfAbsent(p, u -> prof.update().join().getTextures());
        prof.setTextures(realTextures.computeIfAbsent(p, u -> p.getPlayerProfile().update().join().getTextures()));
        p.setPlayerProfile(prof);
        p.setPlayerProfile(prof);
    }
}
