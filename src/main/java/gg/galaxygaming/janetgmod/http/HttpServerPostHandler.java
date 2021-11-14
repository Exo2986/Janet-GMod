package gg.galaxygaming.janetgmod.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import gg.galaxygaming.janetgmod.JanetGMod;
import gg.galaxygaming.janetgmod.http.post.PostParamSet;
import org.reflections.Reflections;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class HttpServerPostHandler implements HttpHandler {
    private List<PostParamSet> paramSets = new ArrayList<>();

    public HttpServerPostHandler() {
        Reflections ref = new Reflections("gg.galaxygaming.janetgmod");
        Set<Class<? extends PostParamSet>> classSet = ref.getSubTypesOf(PostParamSet.class);

        for (Class<? extends PostParamSet> c : classSet) {
            try {
                paramSets.add((PostParamSet) PostParamSet.class.getClassLoader().loadClass("gg.galaxygaming.janetgmod.http.post." + c.getSimpleName()).newInstance());
            } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        JanetGMod.consoleLog("HTTP Post parameter sets initialized.");
    }

    public void handle(HttpExchange he) throws IOException {
        // parse request
        Map<String, Object> parameters = new HashMap<>();
        InputStreamReader isr = new InputStreamReader(he.getRequestBody(), StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        String query = br.readLine();
        HttpServerHandler.parseQuery(query, parameters);

        JanetGMod.consoleLog("Received POST.");

        // send response
        he.sendResponseHeaders(200, 0);

        //for (Map.Entry<String, Object> entry : parameters.entrySet())
        //System.out.println(entry.getKey() + ": " + entry.getValue());

        for (PostParamSet p : paramSets) {
            if (parameters.containsKey(p.getIdentifier())) {
                p.onRun(parameters);
                break;
            }
        }
    }
}