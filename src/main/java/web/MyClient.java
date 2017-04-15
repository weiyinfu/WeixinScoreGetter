package web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class MyClient {
    static String access_token = null;
    static final PoolingHttpClientConnectionManager httpClientConnectionManager = new PoolingHttpClientConnectionManager();

    static HttpClient getClient() {
        return HttpClients.custom().setConnectionManager(httpClientConnectionManager).build();
    }

    private static String getAccessToken() throws IOException {
        String url = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s", Config.appId, Config.appsecret);
        JSONObject json = JSON.parseObject(getWithoutAccessToken(url));
        return json.getString("access_token");
    }

    public static String get(String url) throws IOException {
        return getWithoutAccessToken(url + "?access_token=" + access_token);
    }

    private static String getWithoutAccessToken(String url) throws IOException {
        HttpResponse resp = getClient().execute(new HttpGet(url));
        String s = EntityUtils.toString(resp.getEntity());
        return s;
    }

    public static void send(String url, String data) throws IOException {
        boolean res = post(url, data);
        if (!res) {
            access_token = getAccessToken();
            post(url, data);
        }
    }

    private static boolean post(String url, String data) throws IOException {
        HttpPost post = new HttpPost(url + "?access_token=" + access_token);
        post.setEntity(new StringEntity(data, "utf8"));
        HttpResponse resp = getClient().execute(post);
        JSONObject respJson = JSON.parseObject(EntityUtils.toString(resp.getEntity()));
        return respJson.getInteger("errcode") == 0;
    }

    static void sendTextMessage(String openid, String content) throws IOException {
        String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send";
        JSONObject json = new JSONObject();
        json.put("touser", openid);
        json.put("msgtype", "text");
        JSONObject text = new JSONObject();
        text.put("content", content);
        json.put("text", text);
        send(url, json.toJSONString());
    }
}
