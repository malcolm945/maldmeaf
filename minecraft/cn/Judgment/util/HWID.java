package cn.Judgment.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HWID {
	
	public static String getHWID() throws NoSuchAlgorithmException,UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		String main = System.getenv("COMPUTERNAME");
		byte[] bytes = main.getBytes("UTF-8");
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] md5 = md.digest(bytes);
		int i = 0;
		for (byte b : md5) {
			sb.append(Integer.toHexString((b & 0xFF) | 0x300), 0, 3);
			if (i != md5.length - 1) {
				sb.append("-");
			}
			i++;
		}
		return sb.toString();
	}
}
