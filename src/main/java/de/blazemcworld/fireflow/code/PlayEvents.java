package de.blazemcworld.fireflow.code;

import de.blazemcworld.fireflow.FireFlow;
import de.blazemcworld.fireflow.space.Space;
import de.blazemcworld.fireflow.space.SpaceManager;
import io.papermc.paper.event.block.BlockBreakBlockEvent;
import io.papermc.paper.event.player.AsyncChatEvent;
import io.papermc.paper.event.player.PlayerArmSwingEvent;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.WorldEvent;

public class PlayEvents implements Listener {

    private void handle(WorldEvent event, boolean allowCancel) {
        handle(event, event.getWorld(), allowCancel);
    }

    private void handle(EntityEvent event, boolean allowCancel) {
        handle(event, event.getEntity().getWorld(), allowCancel);
    }

    private void handle(PlayerEvent event, boolean allowCancel) {
        handle(event, event.getPlayer().getWorld(), allowCancel);
    }

    private void handle(BlockEvent event, boolean allowCancel) {
        handle(event, event.getBlock().getWorld(), allowCancel);
    }

    private void handle(Event event, World world, boolean allowCancel) {
        Space space = SpaceManager.getSpaceForWorld(world);
        if (space == null || space.playWorld != world) return;

        EventContext ctx = new EventContext(space.evaluator, event);
        if (allowCancel) ctx.allowCancel();
        space.evaluator.emitEvent(ctx);
    }

    @EventHandler(ignoreCancelled = true)
    public void listener(AsyncChatEvent event) {
        if (event.isAsynchronous()) {
            event.setCancelled(true);
            Bukkit.getScheduler().runTask(FireFlow.instance, () -> event.getPlayer().chat(event.signedMessage().message()));
            return;
        }
        handle(event, true);
    }

    @EventHandler
    public void listener(ChunkLoadEvent event) {
        handle(event, false);
    }

    @EventHandler
    public void listener(BlockBreakBlockEvent event) {
        handle(event, true);
    }

    @EventHandler
    public void listener(PlayerDropItemEvent event) {
        handle(event, true);
    }

    @EventHandler
    public void listener(BlockPlaceEvent event) {
        handle(event, true);
    }

    @EventHandler
    public void listener(PlayerInteractEvent event) {
        handle(event, true);
    }

    @EventHandler
    public void listener(PlayerToggleFlightEvent event) {
        handle(event, true);
    }

    @EventHandler
    public void listener(PlayerToggleSprintEvent event) {
        handle(event, true);
    }

    @EventHandler
    public void listener(PlayerToggleSneakEvent event) {
        handle(event, false);
    }

    @EventHandler
    public void listener(PlayerSwapHandItemsEvent event) {
        handle(event, true);
    }

    @EventHandler
    public void listener(PlayerArmSwingEvent event) {
        handle(event, true);
    }

    @EventHandler
    public void listener(EntityDamageEvent event) {
        handle(event, true);
    }
    @EventHandler
    public void listener(EntityDeathEvent event) {
        handle(event, true);
    }

}
