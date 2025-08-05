package de.blazemcworld.fireflow.code.node.impl.event.meta;

import de.blazemcworld.fireflow.code.CodeThread;
import de.blazemcworld.fireflow.code.EventContext;
import de.blazemcworld.fireflow.code.node.EventNode;
import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.SignalType;
import de.blazemcworld.fireflow.code.type.StringType;
import org.bukkit.Material;

public class DebugEventNode extends Node implements EventNode {

    private final Input<String> id;
    private final Output<Void> signal;

    public DebugEventNode() {
        super("debug_event", "Debug Event", "An event that can be triggered manually using /debug.", Material.STRUCTURE_VOID);

        id = new Input<>("id", "ID", StringType.INSTANCE);
        signal = new Output<>("signal", "Signal", SignalType.INSTANCE);
    }

    @Override
    public Node copy() {
        return new DebugEventNode();
    }

    @Override
    public void handleEvent(EventContext context) {
        if (!(context.customEvent instanceof DebugEvent event)) return;
        CodeThread debugThread = context.newCodeThread();
        debugThread.markDebug();
        if (!this.id.getValue(debugThread).equals(event.id)) return;
        debugThread.sendSignal(signal);
        debugThread.clearQueue();
    }

    public static final class DebugEvent {
        public final String id;
        public boolean found;

        public DebugEvent(String id) {
            this.id = id;
        }
    }
}
