package gg.galaxygaming.janetgmod.auth;

import gg.galaxygaming.janetgmod.JanetGMod;
import gg.galaxygaming.janetgmod.config.Config;
import gg.galaxygaming.janetgmod.http.HttpServerGetHandler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.*;

public class Auth implements Runnable {
    private final String server, idFileLoc;
    private final int refreshTime;

    private boolean running = true;

    public Auth(String server) {
        Config config = JanetGMod.getConfig();
        this.server = server.trim();
        this.idFileLoc = config.getEntryAsStringArray("STEAMID_LIST_FILE_LOCS")[JanetGMod.getServerIndex(server)];
        this.refreshTime = config.getEntry("REFRESH_TIME", Integer.class);
    }

    public void stop() {
        this.running = false;
    }

    @Override
    public void run() {
        JanetGMod.consoleLog("Started authentication thread.");
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
        try {
            checkRanks();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void checkRanks() throws InterruptedException {
        while (this.running) {
            StringBuilder onlinePlayersSB = new StringBuilder();
            try (BufferedReader r = new BufferedReader(this.idFileLoc.startsWith("http") ? new InputStreamReader(new URL(this.idFileLoc).openStream()) :
                    new FileReader(this.idFileLoc))) {
                String line;
                while ((line = r.readLine()) != null)
                    onlinePlayersSB.append(line).append(System.lineSeparator());
            } catch (IOException e) {
                e.printStackTrace();
            }

            String onlinePlayersStr = onlinePlayersSB.toString().replaceAll(";$", "");
            String[] onlinePlayers = onlinePlayersStr.split(";");

            for (String p : onlinePlayers) {
                p = p.trim();
                String rank = getRank(this.server, p);
                if (rank == null)
                    HttpServerGetHandler.getHandlerByID(this.server).QueuedCommands.add("removeuserid " + p);
                else if (!rank.isEmpty())
                    HttpServerGetHandler.getHandlerByID(this.server).QueuedCommands.add("adduserid " + p + ' ' + rank);
            }

            Thread.sleep(this.refreshTime * 1000);
        }
    }

    /*private void checkRank(String steamid) {
        String rank = getRank(server, steamid);
        if (rank != null && rank.trim().length() > 0 && rank.toLowerCase() != "null") {
            Exec.QueuedCommands.add("adduserid " + steamid + " " + rank);
        } else if (rank == null) {
            Exec.QueuedCommands.add("removeuserid " + steamid);
        }
    }*/

    private String getRank(String server, String steamid) {
        String rank = null;
        try (Connection conn = DriverManager.getConnection(JanetGMod.getSQLURL(), JanetGMod.getProperties())) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM gmod_ranks WHERE steamid = \"" + steamid + '"');
            if (rs.next()) {
                rank = rs.getString(server + "_rank").trim();
                if (rank.equals("NULL"))
                    rank = null;
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            rank = "";
            e.printStackTrace();
        }
        return rank;
    }
}