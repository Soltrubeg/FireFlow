package de.blazemcworld.fireflow.code;

import org.bukkit.Material;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class CodeChunkGenerator extends ChunkGenerator {

    @Override
    public void generateBedrock(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkData chunkData) {
        if (chunkZ != 1) return;

        chunkData.setRegion(0, chunkData.getMinHeight(), 0, 16, chunkData.getMaxHeight(), 1, Material.POLISHED_BLACKSTONE);
    }

}
