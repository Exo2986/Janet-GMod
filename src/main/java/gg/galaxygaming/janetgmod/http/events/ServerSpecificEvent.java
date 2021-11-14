package gg.galaxygaming.janetgmod.http.events;

public abstract class ServerSpecificEvent {
    private String serverID;

    public String getServerID() {
        return this.serverID;
    }

    public ServerSpecificEvent(String server) {
        this.serverID = server;
    }
}