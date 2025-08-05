package de.blazemcworld.fireflow.code;

import de.blazemcworld.fireflow.FireFlow;
import de.blazemcworld.fireflow.space.Space;
import de.blazemcworld.fireflow.space.SpaceManager;
import de.blazemcworld.fireflow.util.ModeManager;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.EntitiesLoadEvent;

public class CodeEditorListener implements Listener {

    @EventHandler
    public void onSwapHands(PlayerSwapHandItemsEvent event) {
        if (handle(event.getPlayer(), CodeInteraction.Type.SWAP_HANDS, null)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onChat(AsyncChatEvent event) {
        if (event.isAsynchronous()) {
            event.setCancelled(true);
            Bukkit.getScheduler().runTask(FireFlow.instance, () -> event.getPlayer().chat(event.signedMessage().message()));
            return;
        }

        if (handle(event.getPlayer(), CodeInteraction.Type.CHAT, event.signedMessage().message())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
        if (handle(event.getPlayer(), CodeInteraction.Type.RIGHT_CLICK, null)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player && handle(player, CodeInteraction.Type.LEFT_CLICK, null)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        Space space = SpaceManager.getSpaceForWorld(event.getWorld());
        if (space == null || space.codeWorld != event.getWorld()) return;
        space.editor.onChunkLoad();
    }

    private boolean handle(Player player, CodeInteraction.Type type, String info) {
        Space space = SpaceManager.getSpaceForPlayer(player);
        if (space == null || player.getWorld() != space.codeWorld) return false;
        if (ModeManager.getFor(player) != ModeManager.Mode.CODE) return false;
        return space.editor.handleInteraction(EditOrigin.ofPlayer(player), type, info);
    }

    @EventHandler
    public void onEntitiesLoad(EntitiesLoadEvent event) {
        Space space = SpaceManager.getSpaceForWorld(event.getWorld());
        if (space == null || space.codeWorld != event.getWorld()) return;
        for (Entity entity : event.getEntities()) {
            if (entity instanceof Player) return;
            entity.remove();
        }
    }

}
