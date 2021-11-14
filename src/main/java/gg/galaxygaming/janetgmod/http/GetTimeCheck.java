package gg.galaxygaming.janetgmod.http;

import gg.galaxygaming.janetgmod.JanetGMod;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class GetTimeCheck implements Runnable {
    private HttpServerGetHandler handler;
    private String server;

    private boolean running = true;

    public GetTimeCheck(HttpServerGetHandler handler, String server) {
        this.handler = handler;
        this.server = server;
    }

    public void stop() {
        running = false;
    }

    public void run() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
        while (running) {
            try {
                long currTime = Duration.between(ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS), ZonedDateTime.now()).getSeconds();
                long timeDif = currTime - handler.lastGetTime;
                if (timeDif >= 10)
                    JanetGMod.consoleLog("There have been no GET requests for " + timeDif + " seconds on " + server + ". Either the server is restarting or something has gone horribly wrong.");
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}