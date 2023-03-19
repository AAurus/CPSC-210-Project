package ui.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TitlePanel extends JPanel {
    /// A panel that presents the title bar.
    private static final int IMAGE_WIDTH = 1050;
    private static final int IMAGE_HEIGHT = 200;
    private static final String IMAGE_FILE_PATH = "data/gui/title.png";

    private BufferedImage image;

    public TitlePanel() {
        super();
        setMinimumSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
        setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
        try {
            image = ImageIO.read(new File(IMAGE_FILE_PATH));
        } catch (IOException e) {
            throw new RuntimeException(e); //stub
        }
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, Color.WHITE, null);
        repaint();
    }
}
