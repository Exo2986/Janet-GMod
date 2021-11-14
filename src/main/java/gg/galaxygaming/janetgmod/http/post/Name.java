package gg.galaxygaming.janetgmod.http.post;

import gg.galaxygaming.janetgmod.JanetGMod;

import java.util.Map;

public class Name implements PostParamSet {
    public String getIdentifier() {
        return "name";
    }

    public void onRun(Map<String, Object> params) {
        JanetGMod.getApi().getTextChannelById(JanetGMod.getStaffReqChannels()[JanetGMod.getServerIndex((String) params.get("server"))]).ifPresent(channel ->
                channel.sendMessage("@here " + params.get("name") + " has requested a staff member on " + params.get("server") + '!'));
    }
}