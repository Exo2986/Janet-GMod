package gg.galaxygaming.janetgmod.commands;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;

public class Noose implements Command {
    @Override
    public String getCallback() {
        return "noose";
    }

    @Override
    public String getSyntax() {
        return CommandUtil.constructSyntax("noose");
    }

    @Override
    public String getDescription() {
        return "Provides a complimentary noose.";
    }

    @Override
    public boolean getSplitArgs() {
        return true;
    }

    @Override
    public boolean getHidden() {
        return false;
    }

    @Override
    public boolean getIgnoreAllowCommands() {
        return false;
    }

    @Override
    public void onRun(Message message, String... args) {
        message.getChannel().sendMessage("https://i.imgur.com/xk0IfxD.png");
    }

    @Override
    public boolean validateChannel(TextChannel channel) {
        return true;
    }
}