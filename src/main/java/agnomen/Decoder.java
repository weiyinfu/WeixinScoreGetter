package agnomen;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

/**解码器
 * @传入参数：BufferedImage
 * @传出参数：String
* */
public class Decoder {
    class Color {
        double r, g, b;

        Color add(Color c) {
            return new Color(r + c.r, g + c.g, b + c.b);
        }

        Color(double d, double e, double f) {
            this.r = d;
            this.g = e;
            this.b = f;
        }

        Color(int x) {
            r = x & 255;
            g = (x >> 8) & 255;
            b = (x >> 16) & 255;
        }

        public Color mul() {
            return new Color(r * r, g * g, b * b);
        }

        public Color sub(Color m) {
            return new Color(r - m.r, g - m.g, b - m.b);
        }

        public Color div(int size) {
            return new Color(r / size, g / size, b / size);
        }

        public double len() {
            return Math.sqrt(r * r + g * g + b * b);
        }
    }

    public String ans;
    int interval = 9;
    final static Map<Integer, List<Point>> data = DataManager.load();

    String go(BufferedImage img) {
        String ans = "";
        for (int i = 0; i < 4; i++) {
            int minC = 1;
            double minDx = Double.MAX_VALUE;
            for (int j = 1; j < 10; j++) {
                List<Point> s = data.get(j);
                Color m = new Color(0), n = new Color(0);
                for (int k = 1; k < s.size(); k++) {
                    int x = s.get(k).x + s.get(0).x + i * interval, y = s.get(k).y + s.get(0).y;
                    m = m.add(new Color(img.getRGB(x, y)));
                    n = n.add(new Color(img.getRGB(x, y)).mul());
                }
                n = n.div(s.size());
                m = m.div(s.size());
                double nowDx = n.sub(m.mul()).len();

                if (nowDx < minDx) {
                    minDx = nowDx;
                    minC = j;
                }
            }
            ans += Integer.toString(minC);
        }
        return ans;
    }

    static Decoder decoder;

    public static String parse(BufferedImage img) {
        if (decoder == null) {
            decoder = new Decoder();
        }
        return decoder.go(img);
    }
}