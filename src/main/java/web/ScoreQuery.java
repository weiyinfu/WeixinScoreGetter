package web;

import spider.ScoreGetter;
import spider.Util;

import java.util.concurrent.ConcurrentHashMap;

import static web.MyClient.getClient;

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
        if (forceUpdate //强制更新
                || System.currentTimeMillis() - user.getLastUpdateTime() > 3600 * 1000 * 1 //距离上次更新时间太久了
                || user.getResult() == null//没有结果
                || System.currentTimeMillis() - user.getLastAccessTime() < 1000 * 4//距离上次访问时间很短，说明用户很着急
                ) {
            user.setLastUpdateTime(System.currentTimeMillis());
            user.setResult(ScoreGetter.get(user.getUsername(), user.getPassword(), getClient()));
        }
        user.setLastAccessTime(System.currentTimeMillis());
        return user.getResult();
    }
}
