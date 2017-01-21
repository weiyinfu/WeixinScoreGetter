package haha;

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
import spider.ScoreGetter;
import spider.Util;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class MyClient {
    final static String appId = "wx1d9db42ae638e914";
    final static String appsecret = "7f832387735fb2fc78498c01741e2741";
    public final static String token = "xxxxx";
    static String access_token = null;
    static final PoolingHttpClientConnectionManager httpClientConnectionManager = new PoolingHttpClientConnectionManager();
    static ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();

    static {
        UserData.load();
    }

    static HttpClient getClient() {
        return HttpClients.custom().setConnectionManager(httpClientConnectionManager).build();
    }

    static String getAccessToken() throws IOException {
        String url = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s", appId, appsecret);
        HttpResponse resp = getClient().execute(new HttpGet(url));
        String jsonString = EntityUtils.toString(resp.getEntity());
        JSONObject json = JSON.parseObject(jsonString);
        return json.getString("access_token");
    }

    static void sendTextMessage(String openid, String content) throws IOException {
        JSONObject json = new JSONObject();
        json.put("touser", openid);
        json.put("msgtype", "text");
        JSONObject text = new JSONObject();
        text.put("content", content);
        json.put("text", text);
        sendTwice(json.toJSONString());
    }

    static void sendTwice(String json) throws IOException {
        boolean res = send(json);
        if (!res) {
            access_token = getAccessToken();
            send(json);
        }
    }

    static boolean send(String json) throws IOException {
        HttpPost post = new HttpPost("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + access_token);
        post.setEntity(new StringEntity(json, "utf8"));
        HttpResponse resp = getClient().execute(post);
        JSONObject respJson = JSON.parseObject(EntityUtils.toString(resp.getEntity()));
        return respJson.getInteger("errcode") == 0;
    }

    public static boolean valid(String username, String password) {
        try {
            return Util.login(username, password, getClient());
        } catch (Exception e) {
            return false;
        }
    }

    public static String getResult(String openid) {
        User user = users.get(openid);
        if (System.currentTimeMillis() - user.getLastUpdateTime() > 3600 * 1000 * 1 || user.getResult() == null) {
            user.setLastUpdateTime(System.currentTimeMillis());
            user.setResult(ScoreGetter.get(user.getUsername(), user.getPassword(), getClient()));
        }
        return user.getResult();
    }
}
