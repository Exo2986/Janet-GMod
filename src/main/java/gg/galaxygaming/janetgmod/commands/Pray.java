package gg.galaxygaming.janetgmod.commands;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;

public class Pray implements Command {
    @Override
    public String getCallback() {
        return "pray";
    }

    @Override
    public String getSyntax() {
        return CommandUtil.constructSyntax("pray <target>");
    }

    @Override
    public String getDescription() {
        return "Prays something away.";
    }

    @Override
    public boolean getSplitArgs() {
        return false;
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
        if (args.length == 0)
            message.getChannel().sendMessage(":pray: pray away nothing in particular because your retarded ass didn't use the command properly :pray:");
        else if (args[0].contains("411549864095121409") || args[0].toLowerCase().contains("janet"))
            message.getChannel().sendMessage("fuck you");
        else {
            String m = ":pray: pray ";
            if (!args[0].toLowerCase().startsWith("the")) {
                m += "the ";
            }
            m += args[0].trim();
            if (!args[0].toLowerCase().endsWith("away")) {
                m += " away";
            }
            message.getChannel().sendMessage(m + " :pray:");
        }
    }

    @Override
    public boolean validateChannel(TextChannel channel) {
        return true;
    }
}