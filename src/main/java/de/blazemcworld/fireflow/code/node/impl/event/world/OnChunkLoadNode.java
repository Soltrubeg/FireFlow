package de.blazemcworld.fireflow.code.node.impl.event.world;

import de.blazemcworld.fireflow.code.CodeThread;
import de.blazemcworld.fireflow.code.EventContext;
import de.blazemcworld.fireflow.code.node.EventNode;
import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.NumberType;
import de.blazemcworld.fireflow.code.type.SignalType;
import org.bukkit.Material;
import org.bukkit.event.world.ChunkLoadEvent;

public class OnChunkLoadNode extends Node implements EventNode  {

    private final Output<Void> signal;
    private final Output<Double> x;
    private final Output<Double> z;

    public OnChunkLoadNode() {
        super("on_chunk_load", "On Chunk Load", "Emits a signal when a chunk is loaded.", Material.GRASS_BLOCK);

        signal = new Output<>("signal", "Signal", SignalType.INSTANCE);
        x = new Output<>("x", "X", NumberType.INSTANCE);
        z = new Output<>("z", "Z", NumberType.INSTANCE);
        x.valueFromScope();
        z.valueFromScope();
    }

    @Override
    public void handleEvent(EventContext context) {
        if (!(context.event instanceof ChunkLoadEvent event)) return;
        if (event.getChunk().getX() < -32 || event.getChunk().getX() >= 32 || event.getChunk().getZ() < -32 || event.getChunk().getZ() >= 32) return;

        CodeThread thread = context.newCodeThread();
        thread.setScopeValue(this.x, event.getChunk().getX() * 16.0);
        thread.setScopeValue(this.z, event.getChunk().getZ() * 16.0);
        thread.sendSignal(signal);
        thread.clearQueue();
    }

    @Override
    public Node copy() {
        return new OnChunkLoadNode();
    }

}
