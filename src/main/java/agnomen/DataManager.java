package agnomen;

import java.awt.Point;
import java.io.PrintWriter;
import java.util.*;

/**
 * 数据管理器，负责数据的读写
 */
public class DataManager {
    final static String filePath = "/data.txt";


    public static void save(Map<Integer, List<Point>> ans) {
        try {
            PrintWriter cout = new PrintWriter(DataManager.class.getResource(filePath).getFile());
            for (int i = 1; i < 10; i++) {
                if (ans.get(i) == null) continue;
                cout.print(i + " ");
                for (Point j : ans.get(i)) {
                    cout.print(" " + j.x + " " + j.y);
                }
                cout.println();
            }
            cout.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public static Map<Integer, List<Point>> load() {
        try {
            Map<Integer, List<Point>> ans = new HashMap<>();
            Scanner cin = new Scanner(DataManager.class.getResourceAsStream(filePath));
            while (cin.hasNext()) {
                Scanner line = new Scanner(cin.nextLine());
                int c = Integer.parseInt(line.next());
                List<Point> list = new ArrayList<>();
                while (line.hasNext()) {
                    int x = Integer.parseInt(line.next()), y = Integer.parseInt(line.next());
                    list.add(new Point(x, y));
                }
                ans.put(c, list);
            }
            return ans;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}