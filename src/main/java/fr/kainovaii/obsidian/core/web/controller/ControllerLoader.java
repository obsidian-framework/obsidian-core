package fr.kainovaii.obsidian.core.web.controller;

import fr.kainovaii.obsidian.core.security.role.RoleChecker;
import fr.kainovaii.obsidian.core.web.route.RouteLoader;
import fr.kainovaii.obsidian.core.web.sse.SseLoader;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static spark.Spark.before;

public class ControllerLoader
{
    private static final Logger logger = LoggerFactory.getLogger(ControllerLoader.class);

    public static void loadControllers()
    {
        before("/*", RoleChecker::checkAccess);
        List<Object> controllers = discoverControllers();
        RouteLoader.registerRoutes(controllers);
        SseLoader.registerSseRoutes(controllers);

        logger.info("Loaded {} controllers", controllers.size());
    }

    private static List<Object> discoverControllers()
    {
        Reflections reflections = new Reflections("fr.kainovaii.obsidian.app.controllers");
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

        return controllerClasses.stream()
            .map(ControllerLoader::instantiateController)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    private static Object instantiateController(Class<?> cls)
    {
        try {
            return cls.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            logger.error("Failed to instantiate controller: {}", cls.getName(), e);
            return null;
        }
    }
}