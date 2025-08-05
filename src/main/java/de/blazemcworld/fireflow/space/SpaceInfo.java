package de.blazemcworld.fireflow.space;

import org.bukkit.Material;

import java.util.Set;
import java.util.UUID;

public class SpaceInfo {

    public final int id;
    public String name;
    public Material icon;
    public UUID owner;
    public Set<UUID> developers;
    public Set<UUID> builders;

    public SpaceInfo(int id) {
        this.id = id;
    }

    public boolean isOwnerOrDeveloper(UUID uuid) {
        return owner.equals(uuid) || developers.contains(uuid);
    }

    public boolean isOwnerOrBuilder(UUID uuid) {
        return owner.equals(uuid) || builders.contains(uuid);
    }

    public int playerCount() {
        Space space = SpaceManager.getIfLoaded(this);
        if (space == null) return 0;
        return space.getPlayers().size();
    }
}
