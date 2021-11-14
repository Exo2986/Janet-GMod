package gg.galaxygaming.janetgmod.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class ConfigEntries extends ArrayList {
    private Map<String, ConfigEntry> entryDict;

    public boolean entryExists(String name) {
        return entryDict.containsKey(name);
    }

    public ConfigEntry getFromEntryName(String name) {
        return entryDict.get(name);
    }

    @Override
    public boolean add(Object e) {
        if (e.getClass() == ConfigEntry.class) {
            boolean b = super.add(e);
            ConfigEntry entry = (ConfigEntry) e;
            entryDict.put(entry.getEntryName(), entry);
            return b;
        } else
            return false;
    }

    public ConfigEntries() {
        super();
        entryDict = new HashMap<>();
    }
}