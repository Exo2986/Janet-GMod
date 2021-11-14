package gg.galaxygaming.janetgmod.http.post;

import gg.galaxygaming.janetgmod.JanetGMod;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

public class DonorID implements PostParamSet {
    public String getIdentifier() {
        return "donorid";
    }

    public void onRun(Map<String, Object> params) {
        try (Connection conn = DriverManager.getConnection(JanetGMod.getSQLURL(), JanetGMod.getProperties())) {
            Statement stmt = conn.createStatement();//donorid is the row id
            stmt.execute("DELETE FROM donated_points WHERE id = " + params.get("donorid"));//Delete the row after it is done
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}