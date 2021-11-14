package gg.galaxygaming.janetgmod.http.post;

import gg.galaxygaming.janetgmod.JanetGMod;
import gg.galaxygaming.janetgmod.http.events.MapChangeEvent;
import gg.galaxygaming.janetgmod.http.events.PlayerJoinEvent;

import java.util.Map;

public class Event implements PostParamSet {
    public String getIdentifier() {
        return "event";
    }

    public void onRun(Map<String, Object> params) {
        switch ((String) params.get("event")) {
            case "playerJoin":
                JanetGMod.getEventHandler().firePlayerJoin(new PlayerJoinEvent((String) params.get("server"), (String) params.get("plnames"), (String) params.get("players")));
                break;
            case "mapChange":
                JanetGMod.getEventHandler().fireMapChange(new MapChangeEvent((String) params.get("server"), (String) params.get("map")));
                break;
        }
    }
}