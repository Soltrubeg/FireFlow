package de.blazemcworld.fireflow.code.value;

import de.blazemcworld.fireflow.code.CodeThread;
import de.blazemcworld.fireflow.util.ModeManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class PlayerValue {
    
    public final UUID uuid;

    public PlayerValue(Player player) {
        uuid = player.getUniqueId();
    }

    public PlayerValue(UUID uuid) {
        this.uuid = uuid;
    }

    public void use(World world, Consumer<Player> cb) {
        Player p = Bukkit.getPlayer(uuid);
        if (p == null) {
            cb.accept(null);
            return;
        }
        if (p.getWorld() == world && ModeManager.getFor(p) == ModeManager.Mode.PLAY) {
            cb.accept(p);
            return;
        }
        cb.accept(null);
    }

    public <T> T apply(World world, Function<Player, T> fn) {
        Player p = Bukkit.getPlayer(uuid);
        if (p == null) {
            return fn.apply(null);
        }
        if (p.getWorld() == world && ModeManager.getFor(p) == ModeManager.Mode.PLAY) {
            return fn.apply(p);
        }
        return fn.apply(null);
    }

    public void tryUse(World world, Consumer<Player> cb) {
        use(world, (p) -> {
            if (p == null) return;
            cb.accept(p);
        });
    }

    public <T> T tryGet(World world, Function<Player, T> fn, T fallback) {
        return apply(world, p -> {
            if (p == null) return fallback;
            return fn.apply(p);
        });
    }

    public <T> T tryGet(CodeThread ctx, Function<Player, T> fn, T fallback) {
        return tryGet(ctx.evaluator.world, fn, fallback);
    }

    public void tryUse(CodeThread ctx, Consumer<Player> cb) {
        tryUse(ctx.evaluator.world, cb);
    }
}
