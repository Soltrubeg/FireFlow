package de.blazemcworld.fireflow.util;

import org.bukkit.GameRule;
import org.bukkit.World;

public class WorldUtil {

    public static void init(World world) {
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        world.setGameRule(GameRule.SHOW_DEATH_MESSAGES, false);
        world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        world.setGameRule(GameRule.SPAWN_RADIUS, 0);
        world.setGameRule(GameRule.SPAWN_CHUNK_RADIUS, 0);
        world.getWorldBorder().setSize(1024);
    }

}
