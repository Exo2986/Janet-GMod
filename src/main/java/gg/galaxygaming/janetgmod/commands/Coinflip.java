package gg.galaxygaming.janetgmod.commands;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;

public class Coinflip implements Command {
    @Override
    public String getCallback() {
        return "coinflip";
    }

    @Override
    public String getSyntax() {
        return CommandUtil.constructSyntax("coinflip");
    }

    @Override
    public String getDescription() {
        return "Flips a coin.";
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
        message.getChannel().sendMessage(Math.random() < 0.5 ? "Heads!" : "Tails!");
    }

    @Override
    public boolean validateChannel(TextChannel channel) {
        return true;
    }
}