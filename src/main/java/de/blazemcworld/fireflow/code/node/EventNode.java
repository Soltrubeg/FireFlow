package de.blazemcworld.fireflow.code.node;

import de.blazemcworld.fireflow.code.EventContext;

public interface EventNode {

    void handleEvent(EventContext context);

}
