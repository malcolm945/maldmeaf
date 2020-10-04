package cn.Judgment;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Tiplan {
	public String tiplan = sendGet("https://sslapi.hitokoto.cn/?charset=gbk");
	
	public Tiplan() {
		String hitokoto = "null";
		String from = "";
		boolean success = false;
		int failed = 0;
		while(!success) {
			try {
				if(failed > 5) {
					break;
				}
				JsonParser parser = new JsonParser();
				JsonObject object = (JsonObject) parser.parse(tiplan);
				hitokoto = object.get("hitokoto").getAsString();
				from = object.get("from").getAsString();
				success = true;
			} catch (Exception ex) {}
		}
		if(hitokoto.equalsIgnoreCase("null")) {
			Display.setTitle(Client.CLIENT_name + " v" + Client.CLIENT_VER + " | ERROR:Request Tiplan Failed");
		} else {
			Display.setTitle(Client.CLIENT_name + " v" + Client.CLIENT_VER + " | " + hitokoto + "  ---" + from);
		}
	}
	
	public String sendGet(String url) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url;
			URL realUrl = new URL(urlNameString);
			URLConnection connection = realUrl.openConnection();
			connection.setDoOutput(true);
			connection.setReadTimeout(99781);
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1) ZiMinClient;Chrome 69");
			connection.connect();
			Map<String, List<String>> map = connection.getHeaderFields();
			for (String key : map.keySet()){}
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line + "\n";
			}
		} catch (Exception e) {}
		finally {
			try {
				if(in != null) {
					in.close();
				}
			} catch (Exception e2) {}
		}
		return result;
	}
}
