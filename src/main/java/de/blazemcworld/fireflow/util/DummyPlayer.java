package de.blazemcworld.fireflow.util;

import com.mojang.authlib.GameProfile;
import de.blazemcworld.fireflow.space.DummyManager;
import de.blazemcworld.fireflow.space.Space;
import io.netty.channel.ChannelFutureListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.event.entity.EntityRemoveEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DummyPlayer extends ServerPlayer {

    private static final DedicatedServer server = ((CraftServer) Bukkit.getServer()).getServer();

    public final int dummyId;
    public final Space space;
    public final DummyManager manager;
    private final List<Runnable> nextTick = new ArrayList<>();
    public boolean exitCalled = false;

    public DummyPlayer(Space space, int id) {
        super(server, ((CraftWorld) space.playWorld).getHandle(), dummyProfile(id, space.info.id), ClientInformation.createDefault());
        connection = new ServerGamePacketListenerImpl(server, new net.minecraft.network.Connection(PacketFlow.CLIENTBOUND), this, CommonListenerCookie.createInitial(dummyProfile(id, space.info.id), false)) {
            @Override
            public void send(@NotNull Packet<?> packet, @Nullable ChannelFutureListener callbacks) {
                if (packet instanceof ClientboundSetEntityMotionPacket velPacket && velPacket.getId() == getId()) {
                    nextTick.add(() -> {
                        setDeltaMovement(velPacket.getXa(), velPacket.getYa(), velPacket.getZa());
                    });
                }
            }
        };
        this.dummyId = id;
        this.space = space;
        this.manager = space.dummyManager;
    }

    @Override
    public void remove(@NotNull RemovalReason reason, @Nullable EntityRemoveEvent.Cause cause) {
        if (!exitCalled) {
            exitCalled = true;
            space.evaluator.exitPlay(this.getBukkitEntity());
        }
        super.remove(reason, cause);
        manager.forgetDummy(dummyId);
        server.getPlayerList().broadcastAll(new ClientboundPlayerInfoRemovePacket(List.of(uuid)));
    }

    @Override
    public void tick() {
        List<Runnable> tasks = new ArrayList<>(nextTick);
        nextTick.clear();
        for (Runnable task : tasks) task.run();
        checkSupportingBlock(true, null);
        setOnGround(mainSupportingBlockPos.isPresent());
        super.tick();
        super.doTick();
    }

    @Override
    public void doTick() {
        // Moved into regular tick
    }

    @Override
    public boolean isClientAuthoritative() {
        return false;
    }

    private static GameProfile dummyProfile(int dummyId, int spaceId) {
        String id = String.valueOf((char) (dummyId + (int) 'a' - 1)) + dummyId;
        String id4 = id.repeat(4) + "-";
        String id2 = id.repeat(2) + "-";
        return new GameProfile(UUID.fromString(id4 + id2 + id2 + id2 + StringUtils.leftPad(String.valueOf(spaceId), 12, '0')), "Dummy-" + dummyId);
    }
}
