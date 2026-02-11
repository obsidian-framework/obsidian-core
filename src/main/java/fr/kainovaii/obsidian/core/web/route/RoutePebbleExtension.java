package fr.kainovaii.obsidian.core.web.route;

import io.pebbletemplates.pebble.extension.AbstractExtension;
import io.pebbletemplates.pebble.extension.Function;
import io.pebbletemplates.pebble.template.EvaluationContext;
import io.pebbletemplates.pebble.template.PebbleTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoutePebbleExtension extends AbstractExtension
{
    @Override
    public Map<String, Function> getFunctions()
    {
        Map<String, Function> functions = new HashMap<>();
        functions.put("route", new RouteFunction());
        return functions;
    }
}

class RouteFunction implements Function
{
    @Override
    public Object execute(Map<String, Object> args, PebbleTemplate self, EvaluationContext context, int lineNumber)
    {
        String routeName = (String) args.get("name");
        if (routeName == null || routeName.isEmpty()) { return ""; }
        return Route.getPath(routeName);
    }

    @Override
    public List<String> getArgumentNames()
    {
        List<String> names = new ArrayList<>();
        names.add("name");
        return names;
    }
}