package gg.galaxygaming.janetgmod.http.post;

import gg.galaxygaming.janetgmod.JanetGMod;
import gg.galaxygaming.janetgmod.config.Config;
import gg.galaxygaming.janetgmod.serverstatus.ServerStatus;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.message.Message;

import java.util.HashMap;
import java.util.Map;

public class ServerStatusPost implements PostParamSet {
    public String getIdentifier() {
        return "serverstatus";
    }

    private Map<String, String> convertMaps(Map<String, Object> map) {
        Map<String, String> hm = new HashMap<>();
        map.forEach((k, v) -> hm.put(k, String.valueOf(v)));
        return hm;
    }

    public void onRun(Map<String, Object> params) {
        DiscordApi api = JanetGMod.getApi();
        Config config = JanetGMod.getConfig();

        Map<String, String> paramsSS = convertMaps(params);

        ServerStatus serverStatus = JanetGMod.getServerStatus();
        serverStatus.setInfo(params.get("server").toString(), paramsSS);

        api.getTextChannelById(config.getEntry("SERVER_STATUS_CHANNEL", String.class)).ifPresent(channel ->
                channel.getMessages(JanetGMod.getServers().length).thenAccept(messageSet -> {
                    String server = ((String) params.get("server")).trim();

                    for (Message m : messageSet) {
                        if (m.getAuthor().isYourself() && m.getEmbeds().get(0).getTitle().orElse("").trim().toLowerCase().contains(server.toLowerCase())) {
                            serverStatus.registerMessageID(JanetGMod.getServerIndex(server), m);
                            m.edit(serverStatus.getEmbed(convertMaps(params))).thenAccept(a -> JanetGMod.consoleLog("Updated server status."));
                            return;
                        }
                    }
                    channel.sendMessage(serverStatus.getEmbed(paramsSS)).thenAccept(m -> {
                        serverStatus.registerMessageID(JanetGMod.getServerIndex(server), m);
                        JanetGMod.consoleLog("Created server status message.");
                    });
                })
        );
    }
}