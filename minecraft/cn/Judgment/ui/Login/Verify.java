package cn.Judgment.ui.Login;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.ResourceLocation;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import cn.Judgment.util.RenderUtil;

public class Verify {

    public static boolean AddVerify(List var0, String var1) throws IOException {
        CloseableHttpClient var2 = null;
        CloseableHttpResponse var3 = null;

        try {
            BasicCookieStore var4 = new BasicCookieStore();
            var2 = HttpClients.custom().setDefaultCookieStore(var4).build();
            String var5 = "https://judgment.top/?thread-create-0.htm";
            HttpPost var6 = new HttpPost(var5);
            var6.setHeader("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0");
            ArrayList var7 = new ArrayList();
            var7.add(new BasicNameValuePair("doctype", "1"));
            var7.add(new BasicNameValuePair("return_html", "1"));
            var7.add(new BasicNameValuePair("quotepid", "0"));
            var7.add(new BasicNameValuePair("message", var1));
            UrlEncodedFormEntity var8 = new UrlEncodedFormEntity(var7, "UTF-8");
            var6.setEntity(var8);

            for (int var9 = 0; var9 < var0.size(); ++var9) {
                var4.addCookie((Cookie) var0.get(var9));
            }

            var3 = var2.execute(var6);
            boolean var11 = var4.toString().contains("bbs_token");
            return var11;
        } catch (Exception var14) {
            throw var14;
        } finally {
            var2.close();
            var3.close();
        }
    }

    public static List Login(String var0, String var1) throws Throwable {
        CloseableHttpClient var2 = null;
        CloseableHttpResponse var3 = null;

        List var12;
        try {
            BasicCookieStore var4 = new BasicCookieStore();
            var2 = HttpClients.custom().setDefaultCookieStore(var4).build();
            String var5 = "https://judgment.top/?user-login.htm"; // µÇÂ¼
            HttpPost var6 = new HttpPost(var5);
            var6.setHeader("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0");
            ArrayList var7 = new ArrayList();
            var7.add(new BasicNameValuePair("email", var0));
            var7.add(new BasicNameValuePair("password", DigestUtils.md5Hex(var1)));
            UrlEncodedFormEntity var8 = new UrlEncodedFormEntity(var7, "UTF-8");
            var6.setEntity(var8);
            var3 = var2.execute(var6);
            HttpEntity var9 = var3.getEntity();
            List var10 = null;
            if (var9 != null) {
                var10 = var4.getCookies();
            }
            var12 = var10;
        } catch (Exception var15) {
            throw var15;
        } finally {
            var2.close();
            var3.close();
        }

        System.out.println(new Verify().getClass().getProtectionDomain().getCodeSource().getLocation().getPath());

        return var12;
    }

}
