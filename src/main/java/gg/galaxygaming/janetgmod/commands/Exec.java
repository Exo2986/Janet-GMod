package gg.galaxygaming.janetgmod.commands;

import gg.galaxygaming.janetgmod.JanetGMod;
import gg.galaxygaming.janetgmod.http.HttpServerGetHandler;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;

import java.util.Arrays;

public class Exec implements Command {
    @Override
    public String getCallback() {
        return "exec";
    }

    @Override
    public String getSyntax() {
        return CommandUtil.constructSyntax("exec <command>");
    }

    @Override
    public String getDescription() {
        return "Executes a command.";
    }

    @Override
    public boolean getSplitArgs() {
        return false;
    }

    @Override
    public boolean getHidden() {
        return true;
    }

    @Override
    public boolean getIgnoreAllowCommands() {
        return true;
    }

    @Override
    public void onRun(Message message, String... args) {
        if (!args[0].trim().isEmpty())
            Arrays.stream(JanetGMod.getExecChannels()).filter(chnl -> message.getChannel().getIdAsString().equals(chnl.trim())).findFirst().ifPresent(serverID ->
                    HttpServerGetHandler.getHandlerByID(JanetGMod.getServers()[JanetGMod.getExecChannelIndex(serverID)]).QueuedCommands.add(args[0]));
    }

    @Override
    public boolean validateChannel(TextChannel channel) {
        return channel != null && Arrays.stream(JanetGMod.getExecChannels()).anyMatch(chnl -> channel.getIdAsString().equals(chnl.trim()));
    }
}