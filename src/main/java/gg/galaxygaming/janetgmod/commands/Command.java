package gg.galaxygaming.janetgmod.commands;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;

public interface Command {
    String getCallback();

    String getSyntax();

    String getDescription();

    boolean getHidden();

    boolean getSplitArgs();

    boolean getIgnoreAllowCommands();

    void onRun(Message message, String... args);

    boolean validateChannel(TextChannel channel);
}