package de.blazemcworld.fireflow.util;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.util.SideEffectSet;
import de.blazemcworld.fireflow.FireFlow;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.logging.Level;

public class BlockBatch {

    private EditSession session;

    public BlockBatch(World world) {
        session = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(world));
    }

    public void set(int x, int y, int z, Material m) {
        if (!m.isBlock()) return;

        try {
            session.setBlock(BlockVector3.at(x, y, z), BukkitAdapter.adapt(m.createBlockData()));
        } catch (MaxChangedBlocksException e) {
            apply();

            try {
                session.setBlock(BlockVector3.at(x, y, z), BukkitAdapter.adapt(m.createBlockData()));
            } catch (MaxChangedBlocksException ex) {
                FireFlow.logger.log(Level.WARNING, "Unexpected batch limit reached!", ex);
            }
        }
    }

    public void apply() {
        session.setSideEffectApplier(SideEffectSet.none());
        session.setTrackingHistory(false);
        session.close();
        session = WorldEdit.getInstance().newEditSession(session.getWorld());
    }

}
