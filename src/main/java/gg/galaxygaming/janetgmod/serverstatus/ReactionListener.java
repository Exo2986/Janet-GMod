package gg.galaxygaming.janetgmod.serverstatus;

import gg.galaxygaming.janetgmod.JanetGMod;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.reaction.ReactionAddEvent;
import org.javacord.api.listener.message.reaction.ReactionAddListener;

public class ReactionListener implements ReactionAddListener {
    @Override
    public void onReactionAdd(ReactionAddEvent event) {
        event.getMessage().ifPresent(m -> {
            String serverID = JanetGMod.getServerStatus().getServerID(m.getId());
            if (serverID == null || event.getUser().isYourself())
                return;

            event.removeReaction();
            event.getEmoji().asCustomEmoji().ifPresent(e -> {
                if (e.getName().equals("current_players"))
                    reactionResponse(serverID, event.getUser());
            });
        });
    }

    public static void reactionResponse(String serverID, User user) {
        String[] names = JanetGMod.getServerStatus().getInfo(serverID, "plnames").split(";");
        if (names.length == 1 && names[0].equals("empty"))
            user.sendMessage("There are currently no players on " + serverID + '!');
        else {
            MessageBuilder mb = new MessageBuilder();
            mb.append("__**Players on ");
            mb.append(serverID).append("**__\n```");
            for (int i = 0; i < names.length; i++)
                mb.append(i + 1).append(". ").append(names[i]).append('\n');
            mb.append("```");
            mb.send(user);
        }
    }
}