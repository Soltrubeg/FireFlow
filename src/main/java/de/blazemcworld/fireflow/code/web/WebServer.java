package de.blazemcworld.fireflow.code.web;

import de.blazemcworld.fireflow.FireFlow;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.websocket.server.ServerWebSocketContainer;

import java.util.logging.Level;

public class WebServer {

    private static Server jetty;

    public static void init() {
        try {
            QueuedThreadPool pool = new QueuedThreadPool();
            pool.setName("WebEditor-Server");
            jetty = new Server(pool);
            ServerConnector connector = new ServerConnector(jetty);
            int port = FireFlow.instance.getConfig().getInt("web-port", 8080);
            connector.setPort(port);
            jetty.addConnector(connector);

            ContextHandler context = new ContextHandler();
            jetty.setHandler(context);
            ServerWebSocketContainer.ensure(jetty, context);

            WebEditor handler = new WebEditor();
            context.setHandler(handler);

            Thread t = new Thread(() -> {
                try {
                    jetty.start();
                } catch (Exception e) {
                    FireFlow.logger.log(Level.WARNING, "Failed to start jetty server for web editor!", e);
                }
            }, "WebEditor-Server");
            t.setDaemon(true);
            t.start();
        } catch (Exception e) {
            FireFlow.logger.log(Level.WARNING, "Failed to initialize jetty server for web editor!", e);
        }
    }

    public static void stop() {
        if (jetty == null) return;
        try {
            jetty.stop();
        } catch (Exception e) {
            FireFlow.logger.log(Level.WARNING, "Failed to stop jetty server for web editor!", e);
        }
    }

}
