package fr.kainovaii.obsidian.core.web.websocket;

import fr.kainovaii.obsidian.core.web.route.methods.WebSocket;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import static spark.Spark.webSocket;

public class WebSocketLoader
{
    private static final Logger logger = LoggerFactory.getLogger(WebSocketLoader.class);

    public static void registerWebSockets()
    {
        logger.info("Scanning for WebSocket handlers in package: fr.kainovaii.obsidian.app.websockets");

        Reflections reflections = new Reflections("fr.kainovaii.obsidian.app.websockets");
        Set<Class<?>> webSocketClasses = reflections.getTypesAnnotatedWith(WebSocket.class);

        logger.info("Found {} WebSocket handler(s)", webSocketClasses.size());

        for (Class<?> wsClass : webSocketClasses) {
            registerWebSocket(wsClass);
        }

        logger.info("Loaded {} WebSocket handlers", webSocketClasses.size());
    }

    private static void registerWebSocket(Class<?> wsClass)
    {
        WebSocket annotation = wsClass.getAnnotation(WebSocket.class);
        String path = annotation.value();

        webSocket(path, wsClass);

        logger.info("✓ Registered WebSocket: {} -> {} (class: {})",
                path, wsClass.getSimpleName(), wsClass.getName());
    }
}