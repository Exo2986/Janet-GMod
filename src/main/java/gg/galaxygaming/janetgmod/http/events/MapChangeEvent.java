package gg.galaxygaming.janetgmod.http.events;

public class MapChangeEvent extends ServerSpecificEvent {
    private String map;

    public MapChangeEvent(String serverID, String newMap) {
        super(serverID);
        this.map = newMap;
    }

    public String getMap() {
        return this.map;
    }
}