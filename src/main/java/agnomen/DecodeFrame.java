package agnomen;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DecodeFrame extends JFrame {
    public static void main(String[] args) {
        new DecodeFrame();
    }

    File[] files = new File("src/main/resources/checkcodes").listFiles();
    int fileIndex = 0;
    JTextField text = new JTextField();
    JPanel panel = new JPanel() {
        @Override
        public void paint(Graphics g) {
            try {
                BufferedImage img = ImageIO.read(files[fileIndex]);
                g.drawImage(img, 0, 0, panel.getWidth(), panel.getHeight(), null);
                setTitle(new Decoder().go(img));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    DecodeFrame() {
        setExtendedState(MAXIMIZED_BOTH);
        setLayout(new BorderLayout());
        add(text, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        text.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                panel.repaint();
            }
        });
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    fileIndex = (fileIndex + 1) % files.length;
                    panel.repaint();
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    fileIndex = (fileIndex - 1 + files.length) % files.length;
                    panel.repaint();
                }
            }
        });
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.requestFocus();
            }
        });
    }
}