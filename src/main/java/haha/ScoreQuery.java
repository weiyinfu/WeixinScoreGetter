package haha;

import spider.ScoreGetter;
import spider.Util;

import java.util.concurrent.ConcurrentHashMap;

import static haha.MyClient.getClient;

public class ScoreQuery {

    static ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();

    public static boolean valid(String username, String password) {
        try {
            return Util.login(username, password, MyClient.getClient());
        } catch (Exception e) {
            return false;
        }
    }

    public static String getScore(String openid, boolean forceUpdate) {
        User user = users.get(openid);
        if (forceUpdate || System.currentTimeMillis() - user.getLastUpdateTime() > 3600 * 1000 * 1 || user.getResult() == null) {
            user.setLastUpdateTime(System.currentTimeMillis());
            user.setResult(ScoreGetter.get(user.getUsername(), user.getPassword(), getClient()));
        }
        return user.getResult();
    }
}
