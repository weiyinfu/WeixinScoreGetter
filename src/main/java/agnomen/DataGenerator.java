package agnomen;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**数据生成器用来生成data.txt。通过鼠标点击的方式可视化的建立数据
 * */
public class DataGenerator extends JFrame {
    public static void main(String[] args) {
        new DataGenerator();
    }

    JTextField text = new JTextField();
    JPanel panel = new JPanel() {
        @Override
        public void paint(Graphics g) {
            try {
                BufferedImage img = ImageIO.read(files[fileIndex]);
                if (chosen == null)
                    chosen = new boolean[img.getWidth()][img.getHeight()];
                for (int i = 0; i < img.getWidth(); i++) {
                    for (int j = 0; j < img.getHeight(); j++) {
                        if (chosen[i][j]) {
                            img.setRGB(i, j, Color.RED.getRGB());
                        }
                    }
                }
                g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    File[] files = new File("checkcodes").listFiles();
    int fileIndex = 0;
    int interval = 9;
    boolean chosen[][];
    Map<Integer, List<Point>> ans = new HashMap<>();

    DataGenerator() {
        ans = DataManager.load();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());
        add(text, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    fileIndex = (fileIndex + 1) % files.length;
                    chosen = null;
                    panel.repaint();
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    fileIndex = (fileIndex - 1 + files.length) % files.length;
                    chosen = null;
                    panel.repaint();
                } else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_S) {
                    DataManager.save(ans);
                }
            }
        });
        text.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (text.getText().length() != 1) return;
                    int n = Integer.parseInt(text.getText());
                    List<Point> ps = new ArrayList<Point>();
                    for (int i = 0; i < chosen.length; i++) {
                        for (int j = 0; j < chosen[0].length; j++) {
                            if (chosen[i][j]) {
                                if (ps.size() == 0) {
                                    ps.add(new Point(i, j));
                                } else {
                                    ps.add(new Point(i - ps.get(0).x, j - ps.get(0).y));
                                }
                            }
                        }
                    }
                    ans.put(n, ps);
                    chosen = null;
                    text.setText("");
                    panel.repaint();
                }
            }
        });
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                double gridW = panel.getWidth() * 1.0 / chosen.length, gridH = panel.getHeight() * 1.0 / chosen[0].length;
                int x = (int) (e.getX() / gridW), y = (int) (e.getY() / gridH);
                if (e.getButton() == 1) {
                    chosen[x][y] = true;
                    panel.repaint();
                    setTitle(x + " " + y);
                } else if (e.getButton() == 3) {
                    chosen[x][y] = false;
                    panel.repaint();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                panel.requestFocus();
            }
        });
    }

}