package de.blazemcworld.fireflow.util;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.session.SessionManager;
import de.blazemcworld.fireflow.FireFlow;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.context.ImmutableContextSet;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.time.Duration;

public class PermissionHelper {

    public static void givePermission(Player player, Node permission) {
        LuckPermsProvider.get().getUserManager().modifyUser(player.getUniqueId(), u -> {
            u.data().add(permission);
        });
    }

    public static void takePermission(Player player, Node permission) {
        LuckPermsProvider.get().getUserManager().modifyUser(player.getUniqueId(), u -> {
            u.data().remove(permission);
        });
    }

    public static void giveWorldEdit(Player player, World world) {
        String group = FireFlow.instance.getConfig().getString("worldedit-group");
        if (group == null) return;
        givePermission(player, InheritanceNode.builder(group)
                .context(ImmutableContextSet.builder()
                        .add("world", world.getName())
                        .build())
                .expiry(Duration.ofDays(1))
                .build());
    }

    public static void takeWorldEdit(Player player, World world) {
        String group = FireFlow.instance.getConfig().getString("worldedit-group");
        if (group == null) return;
        takePermission(player, InheritanceNode.builder(group)
                .context(ImmutableContextSet.builder()
                        .add("world", world.getName())
                        .build())
                .build());
        WorldEdit.getInstance().getSessionManager().get(BukkitAdapter.adapt(player)).clearHistory();
    }

}
