package web;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static web.ScoreQuery.users;

class UserData {
    static void load() {
        if (Files.exists(Config.userData)) {
            try {
                JSON.parseArray(Files.readAllLines(Config.userData).stream().collect(Collectors.joining()), User.class).forEach((x) -> {
                    users.put(x.getOpenid(), x);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static synchronized void save() {
        try (PrintWriter cout = new PrintWriter(Files.newBufferedWriter(Config.userData))) {
            String json = JSON.toJSONString(users.values(), true);
            cout.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        load();
        ConcurrentHashMap<String, User> users = ScoreQuery.users;
        users.values().forEach(x -> {
            System.out.println(JSON.toJSONString(x, true));
        });
    }
}
