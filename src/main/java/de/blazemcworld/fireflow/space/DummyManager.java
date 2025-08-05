package de.blazemcworld.fireflow.space;

import de.blazemcworld.fireflow.util.DummyPlayer;
import de.blazemcworld.fireflow.util.Statistics;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.List;

public class DummyManager {

    public final Space space;
    private final DummyPlayer[] dummies = new DummyPlayer[5];

    public DummyManager(Space space) {
        this.space = space;
    }

    public static void unlistDummies(Player normal) {
        for (Player other : Bukkit.getServer().getOnlinePlayers()) {
            if (isDummy(other)) normal.unlistPlayer(other);
        }
    }

    public DummyPlayer getDummy(int id) {
        return dummies[id - 1];
    }

    public void spawnDummy(int id) {
        if (dummies[id - 1] != null) return;
        DummyPlayer dummy = new DummyPlayer(space, id);
        dummy.setPos(new Vec3(0, 1, 0));
        Player bukkit = dummy.getBukkitEntity();
        for (Player other : Bukkit.getServer().getOnlinePlayers()) {
            other.unlistPlayer(bukkit);
            ServerPlayer handle = ((CraftPlayer) other).getHandle();
            handle.connection.send(ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(List.of(dummy), handle));
        }

        ((CraftWorld) space.playWorld).getHandle().addFreshEntity(dummy);
        dummies[id - 1] = dummy;
        Statistics.reset(bukkit);
        space.evaluator.onJoin(bukkit);
    }

    public void forgetDummy(int dummyId) {
        dummies[dummyId - 1] = null;
    }

    public void reset() {
        for (int i = 0; i < dummies.length; i++) {
            DummyPlayer dummy = dummies[i];
            if (dummy == null) continue;
            dummy.discard();
            dummies[i] = null;
        }
    }

    public static boolean isDummy(Player player) {
        return getDummy(player) != null;
    }

    public static DummyPlayer getDummy(Player player) {
        if (player instanceof CraftPlayer c && c.getHandle() instanceof DummyPlayer d) return d;
        return null;
    }
}
