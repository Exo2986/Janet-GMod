package gg.galaxygaming.janetgmod.commands;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;

public class Wiggle implements Command {
    @Override
    public String getCallback() {
        return "wiggle";
    }

    @Override
    public String getSyntax() {
        return CommandUtil.constructSyntax("wiggle");
    }

    @Override
    public String getDescription() {
        return "Wiggle.";
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
        return false;
    }

    @Override
    public void onRun(Message message, String... args) {
        message.getUserAuthor().ifPresent(u -> {
            if (u.getId() == 321318248077328384L)
                message.getChannel().sendMessage("#LeaveJanetAlone");
        });
    }

    @Override
    public boolean validateChannel(TextChannel channel) {
        return true;
    }
}
