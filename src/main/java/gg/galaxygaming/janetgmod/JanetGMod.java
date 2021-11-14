package gg.galaxygaming.janetgmod;

import com.sun.net.httpserver.HttpServer;
import gg.galaxygaming.janetgmod.auth.Auth;
import gg.galaxygaming.janetgmod.commands.CommandHandler;
import gg.galaxygaming.janetgmod.config.Config;
import gg.galaxygaming.janetgmod.donation.Donation;
import gg.galaxygaming.janetgmod.http.GetTimeCheck;
import gg.galaxygaming.janetgmod.http.HttpServerGetHandler;
import gg.galaxygaming.janetgmod.http.HttpServerHandler;
import gg.galaxygaming.janetgmod.http.HttpServerPostHandler;
import gg.galaxygaming.janetgmod.http.events.EventHandler;
import gg.galaxygaming.janetgmod.logging.Logging;
import gg.galaxygaming.janetgmod.serverstatus.ServerStatus;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.activity.ActivityType;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class JanetGMod {
    private static JanetGMod INSTANCE;
    private CommandHandler cmdHandler;
    private EventHandler eventHandler;
    private Properties properties;
    private String sqlURL;
    private Config config;
    public final Logging logging;
    private ServerStatus serverStatus;
    private DiscordApi api;

    private String[] servers, execChannels, staffReqChannels;

    public static void main(String[] args) throws Exception {
        new JanetGMod();
    }

    private JanetGMod() throws IOException {
        INSTANCE = this;
        this.logging = new Logging();
        this.config = new Config("Config.txt");
        if (this.config.getHasUpdatedConfigFile()) {
            consoleLog("Config.txt has been created or updated with missing values. Please input the proper values of each new entry before running this bot again.");
            System.exit(1);
        }

        this.eventHandler = new EventHandler();

        this.sqlURL = "jdbc:mysql://" + this.config.getEntry("DB_HOST", String.class) + '/' + this.config.getEntry("GMOD_DB_NAME", String.class);

        this.properties = new Properties();
        this.properties.setProperty("user", this.config.getEntry("GMOD_DB_USER", String.class));
        this.properties.setProperty("password", this.config.getEntry("GMOD_DB_PASSWORD", String.class));
        this.properties.setProperty("useSSL", "false");
        this.properties.setProperty("autoReconnect", "true");
        this.properties.setProperty("useLegacyDatetimeCode", "false");
        this.properties.setProperty("serverTimezone", "EST");

        this.servers = this.config.getEntryAsStringArray("SERVER_IDS");
        this.execChannels = this.config.getEntryAsStringArray("EXEC_CHANNELS");
        this.staffReqChannels = this.config.getEntryAsStringArray("STAFF_REQUEST_CHANNELS");

        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", config.getEntry("HTTP_SERVER_PORT", Integer.class)), 128);
        SyncListener syncListener = new SyncListener();

        new DiscordApiBuilder().setToken(this.config.getEntry("DISCORD_TOKEN", String.class)).login().thenAccept(a -> {
            this.api = a;
            this.api.updateActivity(ActivityType.PLAYING, getConfig().getEntry("COMMAND_PREFIX", String.class) + "help");
            this.api.addServerMemberJoinListener(syncListener);//Make sure user cache is up to date

            //Do this afterwards because the thread that gets started below requires api to be set
            server.createContext("/", new HttpServerHandler());
            for (String s : this.servers) {
                HttpServerGetHandler h = new HttpServerGetHandler(s);
                server.createContext('/' + s, h);
                new Thread(new GetTimeCheck(h, s)).start();
            }

            server.createContext("/post", new HttpServerPostHandler());
            server.setExecutor(null); // creates a default executor
            server.start();
            consoleLog("Successfully connected to Discord.");

            this.cmdHandler = new CommandHandler();
            if (this.config.getEntry("ENABLE_OPTIONALS", Boolean.class)) {
                for (String s : this.servers) {
                    new Thread(new Auth(s)).start();
                    new Thread(new Donation(s)).start();
                }
            }
            this.serverStatus = new ServerStatus();
        }).join();
    }

    public static Config getConfig() {
        return INSTANCE.config;
    }

    public static DiscordApi getApi() {
        return INSTANCE.api;
    }

    public static Properties getProperties() {
        return INSTANCE.properties;
    }

    public static Logging getLogging() {
        return INSTANCE.logging;
    }

    public static String getSQLURL() {
        return INSTANCE.sqlURL;
    }

    public static String[] getServers() {
        return INSTANCE.servers;
    }

    public static String[] getExecChannels() {
        return INSTANCE.execChannels;
    }

    public static String[] getStaffReqChannels() {
        return INSTANCE.staffReqChannels;
    }

    public static EventHandler getEventHandler() {
        return INSTANCE.eventHandler;
    }

    public static ServerStatus getServerStatus() {
        return INSTANCE.serverStatus;
    }

    public static CommandHandler getCommandHandler() {
        return INSTANCE.cmdHandler;
    }

    public static int getServerIndex(String server) {
        if (server == null)
            return -1;
        server = server.trim();
        for (int i = 0; i < INSTANCE.servers.length; i++)
            if (server.equalsIgnoreCase(INSTANCE.servers[i]))
                return i;
        return -1;
    }

    public static int getExecChannelIndex(String channelID) {
        if (channelID == null)
            return -1;
        channelID = channelID.trim();
        for (int i = 0; i < INSTANCE.execChannels.length; i++)
            if (channelID.equalsIgnoreCase(INSTANCE.execChannels[i]))
                return i;
        return -1;
    }

    public static void consoleLog(Object... params) {
        StringBuilder sb = new StringBuilder();
        for (Object o : params)
            sb.append(o).append(' ');
        System.out.println('(' + new SimpleDateFormat("MM/dd/yy HH:mm:ss z").format(new Date()) + ") Janet-GMod: " + sb.toString().trim());
    }

    public static JanetGMod getInstance() {
        return INSTANCE;
    }
}