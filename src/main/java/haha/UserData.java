package haha;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;

public class UserData {
    final static Path file = Paths.get(System.getProperty("user.home")).resolve("wxuser.txt");

    static synchronized void load() {
        try {
            if (Files.exists(file)) {
                MyClient.users = (ConcurrentHashMap<String, User>) new ObjectInputStream(Files.newInputStream(file)).readObject();

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    static synchronized void save() {
        try {
            new ObjectOutputStream(Files.newOutputStream(file)).writeObject(MyClient.users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
