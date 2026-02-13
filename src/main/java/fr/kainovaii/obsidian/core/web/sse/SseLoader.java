package fr.kainovaii.obsidian.core.web.sse;

import fr.kainovaii.obsidian.core.web.route.Route;
import fr.kainovaii.obsidian.core.web.route.methods.SSE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.lang.reflect.Method;
import java.util.List;

import static spark.Spark.get;

public class SseLoader
{
    private static final Logger logger = LoggerFactory.getLogger(SseLoader.class);

    public static void registerSseRoutes(List<Object> controllers)
    {
        int sseCount = 0;

        for (Object controller : controllers) {
            sseCount += registerControllerSseRoutes(controller);
        }

        logger.info("Loaded {} SSE routes", sseCount);
    }

    private static int registerControllerSseRoutes(Object controller)
    {
        int count = 0;

        for (Method method : controller.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(SSE.class)) {
                registerSseRoute(controller, method);
                count++;
            }
        }

        return count;
    }

    private static void registerSseRoute(Object controller, Method method)
    {
        SSE annotation = method.getAnnotation(SSE.class);
        String path = annotation.value();
        String name = annotation.name();

        Route.registerNamedRoute(name, path);

        get(path, (req, res) -> {
            configureSseResponse(res);
            return invokeMethod(controller, method, req, res);
        });

        logger.debug("Registered SSE route: {} -> {}", name, path);
    }

    private static void configureSseResponse(Response res)
    {
        res.type("text/event-stream; charset=UTF-8");  // ← AJOUTER charset=UTF-8
        res.header("Cache-Control", "no-cache");
        res.header("Connection", "keep-alive");
        res.header("X-Accel-Buffering", "no"); // Disable nginx buffering
    }

    private static Object invokeMethod(Object controller, Method method, Request req, Response res)
            throws Exception
    {
        method.setAccessible(true);

        // Pour SSE, on ne passe que Request et Response
        Class<?>[] paramTypes = method.getParameterTypes();
        Object[] args = new Object[paramTypes.length];

        for (int i = 0; i < paramTypes.length; i++) {
            if (paramTypes[i] == Request.class) {
                args[i] = req;
            } else if (paramTypes[i] == Response.class) {
                args[i] = res;
            }
        }

        return method.invoke(controller, args);
    }
}