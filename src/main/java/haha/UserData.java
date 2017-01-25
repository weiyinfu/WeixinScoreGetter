package haha;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.concurrent.ConcurrentHashMap;

class UserData {
    static {
        UserData.load();
    }

    static synchronized void load() {
        try {
            if (Files.exists(Config.userData)) {
                ScoreQuery.users = (ConcurrentHashMap<String, User>) new ObjectInputStream(Files.newInputStream(Config.userData)).readObject();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    static synchronized void save() {
        try {
            new ObjectOutputStream(Files.newOutputStream(Config.userData)).writeObject(ScoreQuery.users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
