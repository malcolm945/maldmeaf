package cn.Judgment.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebUtils {

    public static String get(String url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputline;
        StringBuilder response = new StringBuilder();

        while((inputline = in.readLine()) != null) {
            response.append(inputline);
            response.append("\n");
        }
        in.close();
        return response.toString();
    }
}
