package gg.galaxygaming.janetgmod.http.events;

import java.util.ArrayList;
import java.util.List;

public class EventHandler {
    private List<PlayerJoinListener> playerJoinListeners = new ArrayList<>();
    private List<MapChangeListener> mapChangeListeners = new ArrayList<>();
    //If there becomes a large number of listeners, this should be replaced with Map<? extends Listener, List<? extends Listener>>
    //Syntax of that may be off, but it conveys the general idea

    public void addPlayerJoinListener(PlayerJoinListener listener) {
        this.playerJoinListeners.add(listener);
    }

    public boolean firePlayerJoin(PlayerJoinEvent event) {
        boolean returnValue = false;
        for (PlayerJoinListener listener : playerJoinListeners) {
            listener.onPlayerJoin(event);
            returnValue = true;
        }
        return returnValue;
    }

    public void addMapChangeListener(MapChangeListener listener) {
        this.mapChangeListeners.add(listener);
    }

    public boolean fireMapChange(MapChangeEvent event) {
        boolean returnValue = false;
        for (MapChangeListener listener : mapChangeListeners) {
            listener.onMapChange(event);
            returnValue = true;
        }
        return returnValue;
    }
}