package gg.galaxygaming.janetgmod.commands;

import gg.galaxygaming.janetgmod.JanetGMod;

public class CommandUtil {
    public static String constructSyntax(String commandName) {
        return JanetGMod.getConfig().getEntry("COMMAND_PREFIX", String.class) + commandName;
    }
}