package de.blazemcworld.fireflow;

import de.blazemcworld.fireflow.code.CodeEditorListener;
import de.blazemcworld.fireflow.code.PlayEvents;
import de.blazemcworld.fireflow.code.node.NodeList;
import de.blazemcworld.fireflow.code.type.AllTypes;
import de.blazemcworld.fireflow.code.web.WebServer;
import de.blazemcworld.fireflow.command.*;
import de.blazemcworld.fireflow.inventory.InventoryListener;
import de.blazemcworld.fireflow.space.DummyManager;
import de.blazemcworld.fireflow.space.Lobby;
import de.blazemcworld.fireflow.space.SpaceManager;
import de.blazemcworld.fireflow.util.ModeManager;
import de.blazemcworld.fireflow.util.Statistics;
import de.blazemcworld.fireflow.util.TextWidth;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class FireFlow extends JavaPlugin implements Listener {

    public static FireFlow instance;
    public static Logger logger;

    public static final MiniMessage MM = MiniMessage.builder()
            .tags(TagResolver.builder().resolvers(
                    StandardTags.color(),
                    StandardTags.decorations(),
                    StandardTags.font(),
                    StandardTags.gradient(),
                    StandardTags.keybind(),
                    StandardTags.newline(),
                    StandardTags.rainbow(),
                    StandardTags.reset(),
                    StandardTags.transition(),
                    StandardTags.translatable(),
                    StandardTags.hoverEvent()
            ).build()).build();

    @Override
    public void onEnable() {
        instance = this;
        logger = getLogger();

        saveDefaultConfig();

        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> registerCommands(commands.registrar()));

        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new Lobby(), this);
        Bukkit.getPluginManager().registerEvents(new SpaceManager(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryListener(), this);
        Bukkit.getPluginManager().registerEvents(new CodeEditorListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayEvents(), this);
        NodeList.init();

        TextWidth.init();
        SpaceManager.load();
        Lobby.init();
        AllTypes.init();
        WebServer.init();

        logger.info("FireFlow ready!");
    }

    @Override
    public void onDisable() {
        SpaceManager.save(true);
        WebServer.stop();
        getLogger().info("FireFlow stopped!");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        ModeManager.onJoinedServer(event.getPlayer());
        Statistics.reset(event.getPlayer());
        Lobby.onSpawn(event.getPlayer());
        DummyManager.unlistDummies(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        ModeManager.handleExit(event.getPlayer());
        for (Entity e : event.getPlayer().getPassengers()) e.leaveVehicle();
        event.getPlayer().leaveVehicle();
    }

    public void registerCommands(Commands cd) {
        CodeCommand.register(cd);
        PlayCommand.register(cd);
        LobbyCommand.register(cd);
        BuildCommand.register(cd);
        ReloadCommand.register(cd);
        AddNodeCommand.register(cd);
        FunctionCommand.register(cd);
        LocateCommand.register(cd);
        DummyCommand.register(cd);
        AuthWebCommand.register(cd);
        DebugCommand.register(cd);
        SpaceCommand.register(cd);
    }
}
