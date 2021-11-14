package gg.galaxygaming.janetgmod;

import org.javacord.api.entity.user.User;
import org.javacord.api.event.server.member.ServerMemberJoinEvent;
import org.javacord.api.listener.server.member.ServerMemberJoinListener;

import java.util.Collection;

public class SyncListener implements ServerMemberJoinListener {
    @Override
    public void onServerMemberJoin(ServerMemberJoinEvent event) {
        Collection<User> cachedUsers = JanetGMod.getApi().getCachedUsers();
        if (!cachedUsers.contains(event.getUser()))
            cachedUsers.add(event.getUser());
    }
}