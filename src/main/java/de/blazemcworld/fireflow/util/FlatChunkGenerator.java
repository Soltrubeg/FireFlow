package de.blazemcworld.fireflow.util;

import org.bukkit.Material;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class FlatChunkGenerator extends ChunkGenerator {

    @Override
    public void generateBedrock(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkData chunkData) {
        if (chunkX >= 32 || chunkX < -32 || chunkZ >= 32 || chunkZ < -32) return;
        chunkData.setRegion(0, -2, 0, 16, -1, 16, Material.GRASS_BLOCK);
        chunkData.setRegion(0, -5, 0, 16, -2, 16, Material.DIRT);
        chunkData.setRegion(0, -6, 0, 16, -5, 16, Material.BEDROCK);
    }
}
