package gg.galaxygaming.janetgmod.http.events;

public class PlayerJoinEvent extends ServerSpecificEvent {
    private String playerNames, players;

    public PlayerJoinEvent(String serverID, String playerNames, String players) {
        super(serverID);
        this.playerNames = playerNames;
        this.players = players;
    }

    public String getPlayerNames() {
        return this.playerNames;
    }

    public String getPlayers() {
        return this.players;
    }
}