package gg.galaxygaming.janetgmod.commands;

import gg.galaxygaming.janetgmod.JanetGMod;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;

public class Help implements Command {
    @Override
    public String getCallback() {
        return "help";
    }

    @Override
    public String getSyntax() {
        return CommandUtil.constructSyntax("help");
    }

    @Override
    public String getDescription() {
        return "Displays a list of commands.";
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
        MessageBuilder mb = new MessageBuilder();
        mb.append("__**Commands**__\n\n");
        for (Command cmd : JanetGMod.getCommandHandler().getCommands())
            if (!cmd.getHidden())
                mb.append("__" + cmd.getSyntax() + "__\n").append("```css\n" + cmd.getDescription() + "```\n");
        mb.send(message.getChannel());
    }

    @Override
    public boolean validateChannel(TextChannel channel) {
        return true;
    }
}