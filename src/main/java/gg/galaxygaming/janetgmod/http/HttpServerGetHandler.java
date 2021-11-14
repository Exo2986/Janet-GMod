package gg.galaxygaming.janetgmod.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpServerGetHandler implements HttpHandler {
    private static Map<String, HttpServerGetHandler> handlers = new HashMap<>();

    public static HttpServerGetHandler getHandlerByID(String id) {
        return handlers.get(id);
    }

    public HttpServerGetHandler(String serverID) {
        handlers.put(serverID, this);
    }

    public final List<String> QueuedCommands = new ArrayList<>();

    public long lastGetTime = Duration.between(ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS), ZonedDateTime.now()).getSeconds(); //in seconds

    public void handle(HttpExchange he) throws IOException {
        // parse request
        Map<String, Object> parameters = new HashMap<>();
        URI requestedUri = he.getRequestURI();
        String query = requestedUri.getRawQuery();
        HttpServerHandler.parseQuery(query, parameters);

        lastGetTime = Duration.between(ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS), ZonedDateTime.now()).getSeconds();

        // send response
        StringBuilder response = new StringBuilder();
        for (String cmd : QueuedCommands)
            response.append(cmd.trim()).append(';');

        QueuedCommands.clear();
        he.sendResponseHeaders(200, response.length());
        OutputStream os = he.getResponseBody();
        os.write(response.toString().getBytes());

        os.close();
    }
}