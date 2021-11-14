package gg.galaxygaming.janetgmod.serverstatus;

import gg.galaxygaming.janetgmod.JanetGMod;
import gg.galaxygaming.janetgmod.Utils;
import gg.galaxygaming.janetgmod.http.HttpServerGetHandler;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.emoji.KnownCustomEmoji;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.Reaction;
import org.javacord.api.entity.message.UncachedMessageUtil;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.listener.message.reaction.ReactionAddListener;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerStatus {
    private final ReactionListener reactionListener;
    private Map<String, Map<String, String>> info = new HashMap<>();
    private KnownCustomEmoji curPlayers;
    private int serverCount;
    private long[] messageIDs;
    private final String[] fileTypes = new String[] {"gif", "jpg", "png"};

    public ServerStatus() {
        reactionListener = new ReactionListener();
        messageIDs = new long[this.serverCount = JanetGMod.getServers().length];
        curPlayers = JanetGMod.getApi().getCustomEmojisByName("current_players").iterator().next();

        JanetGMod.getEventHandler().addPlayerJoinListener(new SSEventListeners());
        JanetGMod.getEventHandler().addMapChangeListener(new SSEventListeners());

        String sch = JanetGMod.getConfig().getEntry("SERVER_STATUS_CHANNEL", String.class);
        for (String id : JanetGMod.getServers())
            HttpServerGetHandler.getHandlerByID(id).QueuedCommands.add(".serverstatus." + sch);
    }

    public long getMessageID(int index) {
        return messageIDs[index];
    }

    public void setMessageID(int index, long id) {
        messageIDs[index] = id;
    }

    public String getServerID(long id) {
        for (int i = 0; i < messageIDs.length; i++)
            if (messageIDs[i] == id)
                return JanetGMod.getServers()[i];
        return null;
    }

    public void registerMessageID(int index, Message m) {
        setMessageID(index, m.getId());
        m.addReaction(curPlayers);
        Thread reactionThread = getRestartListener(m);
        reactionThread.start();
        //TODO properly close the thread when closing janet
    }

    private Thread getRestartListener(Message message) {
        return new Thread(() -> {
            while (true) {
                UncachedMessageUtil uncached = JanetGMod.getApi().getUncachedMessageUtil();
                uncached.removeListener(message.getId(), ReactionAddListener.class, reactionListener);

                List<Reaction> reactions = message.getReactions();
                for (Reaction reaction : reactions) {
                    if (reaction.getEmoji().isCustomEmoji()) {//Handle and respond
                        reaction.getEmoji().asCustomEmoji().ifPresent(e -> {
                            if (e.getName().equals(curPlayers.getName())) {
                                reaction.getUsers().thenAccept(users -> {
                                    String serverID = getServerID(message.getId());
                                    for (User user : users) {
                                        if (!user.isYourself()) {
                                            ReactionListener.reactionResponse(serverID, user);
                                            reaction.removeUser(user);
                                        }
                                    }
                                });
                            } else
                                reaction.remove();
                        });
                    } else
                        reaction.remove();
                }

                uncached.addReactionAddListener(message.getId(), reactionListener);
                try {
                    Thread.sleep(60_000);//1 minute
                } catch (InterruptedException ignored) {//It is fine if this is interrupted
                }
            }
        });
    }

    public String getInfo(String server, String key) {
        return this.info.get(server).get(key);
    }

    public void setInfo(String id, Map<String, String> n) {
        this.info.put(id, n);
    }

    public void performUpdate(Map<String, String> updates) {
        Map<String, String> hs = info.get(updates.get("server"));
        if (updates.containsKey("plnames"))
            hs.replace("plnames", updates.get("plnames"));
        if (updates.containsKey("map"))
            hs.replace("map", updates.get("map"));
        if (updates.containsKey("players"))
            hs.replace("players", updates.get("players"));
        if (updates.containsKey("server"))
            hs.replace("server", updates.get("server"));

        this.info.replace(updates.get("server"), hs);
        sendStatusMessage(hs);
    }

    public EmbedBuilder getEmbed(Map<String, String> params) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setDescription("**Current Map:**\n```" + params.get("map") + "```\n**Players:**\n```" + params.get("players") + "```");
        eb.setColor(new Color(0, 255, 125));
        int index = JanetGMod.getServerIndex(params.get("server"));
        String sID = JanetGMod.getConfig().getEntryAsStringArray("SERVER_STATUS_MAPIMG_IDS")[index];
        String url = ((String) JanetGMod.getConfig().getEntry("SERVER_STATUS_MAPIMG_URL", String.class)).replaceAll("\\$id",
                sID).replaceAll("\\$map", params.get("map"));
        for (String fileType : fileTypes) {
            if (Utils.urlExists(url + '.' + fileType)) {
                eb.setThumbnail(url + '.' + fileType);
                break;
            }
        }
        eb.setTitle(params.get("server") + " Status");
        return eb.setUrl("https://galaxygaming.gg/" + sID);
    }

    private void sendStatusMessage(Map<String, String> params) {
        DiscordApi api = JanetGMod.getApi();
        api.getTextChannelById(params.get("serverstatus")).ifPresent(channel -> channel.getMessages(this.serverCount).thenAccept(messageSet -> {
            String server = params.get("server").trim().toLowerCase();
            for (Message m : messageSet) {
                if (m.getAuthor().isYourself() && m.getEmbeds().get(0).getTitle().orElse("").trim().toLowerCase().contains(server)) {
                    //Does not need to register the message as we already know about it
                    m.edit(getEmbed(params)).thenAccept(a -> JanetGMod.consoleLog("Updated server status."));
                    return;
                }
            }

            channel.sendMessage(getEmbed(params)).thenAccept(m -> {
                registerMessageID(JanetGMod.getServerIndex(server), m);
                JanetGMod.consoleLog("Created server status message.");
            });
        }));
    }
}