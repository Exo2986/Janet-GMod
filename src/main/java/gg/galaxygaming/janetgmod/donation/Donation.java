package gg.galaxygaming.janetgmod.donation;

import gg.galaxygaming.janetgmod.JanetGMod;
import gg.galaxygaming.janetgmod.config.Config;
import gg.galaxygaming.janetgmod.http.HttpServerGetHandler;

import java.sql.*;

public class Donation implements Runnable {
    private final String server;
    private final int refreshTime;

    private boolean running = true;

    public Donation(String server) {
        Config config = JanetGMod.getConfig();
        this.server = server.trim();
        this.refreshTime = config.getEntry("REFRESH_TIME", Integer.class);
    }

    public void stop() {
        running = false;
    }

    public void run() {
        JanetGMod.consoleLog("Started donation thread.");
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
        try {
            checkPS2();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void checkPS2() throws InterruptedException {
        while (running) {
            try (Connection conn = DriverManager.getConnection(JanetGMod.getSQLURL(), JanetGMod.getProperties())) {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM donated_points");
                while (rs.next()) {
                    int points = rs.getInt("points");
                    if (points == 0) {
                        Statement stmt2 = conn.createStatement();
                        stmt2.execute("DELETE FROM donated_points WHERE id = " + rs.getInt("id"));//Delete the row after it is done
                        stmt2.close();
                        continue;
                    }
                    String s = rs.getString("server");
                    if (s.trim().equalsIgnoreCase(server))
                        HttpServerGetHandler.getHandlerByID(server).QueuedCommands.add(".addpoints." + rs.getString("steamid") + ':' + points + ':' + rs.getInt("id"));
                }
                rs.close();
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            Thread.sleep(this.refreshTime * 1000);
        }
    }
}