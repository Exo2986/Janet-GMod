package gg.galaxygaming.janetgmod;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Utils {
    @SuppressWarnings("unused")
    public static void postURL(URL url, String params) throws IOException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        con.setRequestProperty("Content-Language", "en-US");
        con.setUseCaches(false);
        con.setDoInput(true);
        con.setDoOutput(true);
        params = params.trim();

        if (!params.isEmpty()) {
            //Request
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(params);
            wr.flush();
            wr.close();
        }

        int responseCode = con.getResponseCode();
        JanetGMod.consoleLog("\nSending 'POST' request to URL : " + url);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null)
            response.append(inputLine);
        con.disconnect();
        in.close();

        //print result
        JanetGMod.consoleLog("Response: " + response.toString());
    }

    public static boolean urlExists(String url) {
        try {
            URL u = new URL(url);
            HttpURLConnection huc = (HttpURLConnection) u.openConnection();
            huc.setRequestMethod("GET");
            huc.connect();
            int code = huc.getResponseCode();
            huc.disconnect();
            return code != 404;
        } catch (IOException ignored) {
        }
        return false;
    }
}