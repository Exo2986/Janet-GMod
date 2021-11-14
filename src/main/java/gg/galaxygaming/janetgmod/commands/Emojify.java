package gg.galaxygaming.janetgmod.commands;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;

public class Emojify implements Command {
    @Override
    public String getCallback() {
        return "emojify";
    }

    @Override
    public String getSyntax() {
        return CommandUtil.constructSyntax("emojify <text>");
    }

    @Override
    public String getDescription() {
        return "Emojifies a message.";
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
        char[] letters = args[0].trim().toLowerCase().toCharArray();

        MessageBuilder msg = new MessageBuilder();

        for (char c : letters) {
            if (Character.isLetter(c)) {
                msg.append(":regional_indicator_");
                msg.append(Character.toString(c));
                msg.append(": ");
            } else if (Character.isDigit(c)) {
                String[] nums = new String[]{":zero:", ":one:", ":two:", ":three:", ":four:", ":five:", ":six:", ":seven:", ":eight:", ":nine:"};
                msg.append(nums[Integer.parseInt(Character.toString(c))]);
                msg.append(" ");
            } else if (Character.isWhitespace(c)) {
                msg.append("  ");
            }
        }

        msg.send(message.getChannel());
    }

    @Override
    public boolean validateChannel(TextChannel channel) {
        return true;
    }
}