package gg.galaxygaming.janetgmod.serverstatus;

import gg.galaxygaming.janetgmod.JanetGMod;
import gg.galaxygaming.janetgmod.http.events.MapChangeEvent;
import gg.galaxygaming.janetgmod.http.events.MapChangeListener;
import gg.galaxygaming.janetgmod.http.events.PlayerJoinEvent;
import gg.galaxygaming.janetgmod.http.events.PlayerJoinListener;

import java.util.HashMap;
import java.util.Map;

public class SSEventListeners implements PlayerJoinListener, MapChangeListener {
    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {
        Map<String, String> updates = new HashMap<>();
        updates.put("plnames", event.getPlayerNames());
        updates.put("players", event.getPlayers());
        updates.put("server", event.getServerID());
        JanetGMod.getServerStatus().performUpdate(updates);
    }

    @Override
    public void onMapChange(MapChangeEvent event) {
        Map<String, String> updates = new HashMap<>();
        updates.put("map", event.getMap());
        updates.put("server", event.getServerID());
        JanetGMod.getServerStatus().performUpdate(updates);
    }
}