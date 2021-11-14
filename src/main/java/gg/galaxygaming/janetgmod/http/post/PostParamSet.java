package gg.galaxygaming.janetgmod.http.post;

import java.util.Map;

public interface PostParamSet {
    void onRun(Map<String, Object> params);

    String getIdentifier();
}