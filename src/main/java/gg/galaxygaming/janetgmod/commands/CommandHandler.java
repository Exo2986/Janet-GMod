package gg.galaxygaming.janetgmod.commands;

import gg.galaxygaming.janetgmod.JanetGMod;
import org.javacord.api.entity.message.Message;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CommandHandler implements MessageCreateListener {
    private final List<Command> commands;

    public CommandHandler() {
        this.commands = new ArrayList<>();
        String pkg = "gg.galaxygaming.janetgmod.commands";
        Reflections ref = new Reflections(pkg);
        Set<Class<? extends Command>> classSet = ref.getSubTypesOf(Command.class);
        boolean ignoreCommands = !((Boolean) JanetGMod.getConfig().getEntry("ALLOW_COMMANDS", Boolean.class));
        for (Class<? extends Command> c : classSet) {
            try {
                Command cmd = (Command) Command.class.getClassLoader().loadClass(pkg + '.' + c.getSimpleName()).newInstance();
                if (!ignoreCommands || cmd.getIgnoreAllowCommands())
                    this.commands.add(cmd);
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        JanetGMod.getApi().addMessageCreateListener(this);
        JanetGMod.consoleLog("Command system initialized.");
    }

    @Override
    public void onMessageCreate(MessageCreateEvent e) {
        Message message = e.getMessage();
        for (Command command : this.commands) {
            String cmd = CommandUtil.constructSyntax(command.getCallback());
            if (command.validateChannel(message.getChannel()) && message.getContent().startsWith(cmd)) {
                String s = message.getContent().replaceAll('^' + cmd + "\\s*", "");
                if (s.length() == 0)
                    command.onRun(message);
                else if (command.getSplitArgs())
                    command.onRun(message, s.split("\\s+"));
                else
                    command.onRun(message, s);
                break; //Command run do not keep looking to see if there are other commands that match
            }
        }
    }

    public List<Command> getCommands() {
        return this.commands;
    }
}