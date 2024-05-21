package towerdefense.gui;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

/**
 * Trieda pre vykresľovanie panelu s pozadím v hre Tower Defense.
 *
 * Autor: Martin Košík
 * Dátum: 19.05.2024
 * tuto classu som od niekadial čmajzol ale netušim odkial takže hocikde kte to vyjde je to odtial, nevedel som najť source
 */
public class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    /**
     * Konštruktor triedy BackgroundPanel.
     *
     * @param filename názov súboru s pozadím
     */
    public BackgroundPanel(String filename) {
        super(new GridBagLayout());
        this.backgroundImage = new ImageIcon(getClass().getResource(filename)).getImage();
    }

    /**
     * Vykreslí pozadie panelu.
     *
     * @param g grafický kontext
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(this.backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
    }
}
