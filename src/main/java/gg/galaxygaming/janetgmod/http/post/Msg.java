package gg.galaxygaming.janetgmod.http.post;

import gg.galaxygaming.janetgmod.JanetGMod;

import java.util.Map;

public class Msg implements PostParamSet {
    public String getIdentifier() {
        return "msg";
    }

    public void onRun(Map<String, Object> params) {
        JanetGMod.getApi().getTextChannelById(JanetGMod.getExecChannels()[JanetGMod.getServerIndex((String) params.get("server"))]).ifPresent(channel ->
                channel.sendMessage(params.get("msg").toString()));
    }
}