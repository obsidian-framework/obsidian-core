package fr.kainovaii.obsidian.core.web.middleware;

import spark.Request;
import spark.Response;

public interface Middleware {
    void handle(Request req, Response res) throws Exception;
}
