package fr.kainovaii.obsidian.core.web.middleware;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class MiddlewareManager
{
    private static final Logger logger = LoggerFactory.getLogger(MiddlewareManager.class);
    private static final Map<Class<? extends Middleware>, Middleware> instances = new HashMap<>();

    public static void executeBefore(Class<? extends Middleware>[] middlewareClasses, Request req, Response res) throws Exception
    {
        for (Class<? extends Middleware> middlewareClass : middlewareClasses) {
            Middleware middleware = getInstance(middlewareClass);
            logger.debug("Executing before middleware: {}", middlewareClass.getSimpleName());
            middleware.handle(req, res);
        }
    }

    public static void executeAfter(Class<? extends Middleware>[] middlewareClasses, Request req, Response res) throws Exception
    {
        for (Class<? extends Middleware> middlewareClass : middlewareClasses) {
            Middleware middleware = getInstance(middlewareClass);
            logger.debug("Executing after middleware: {}", middlewareClass.getSimpleName());
            middleware.handle(req, res);
        }
    }

    private static Middleware getInstance(Class<? extends Middleware> middlewareClass)
    {
        return instances.computeIfAbsent(middlewareClass, cls -> {
            try {
                return cls.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                logger.error("Failed to instantiate middleware: {}", cls.getName(), e);
                throw new RuntimeException("Could not instantiate middleware: " + cls.getName(), e);
            }
        });
    }
}
