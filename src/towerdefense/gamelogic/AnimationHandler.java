package towerdefense.gamelogic;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Trieda pre spracovanie animácií v hre Tower Defense.
 *
 * Autor: Martin Košík
 * Dátum: 19.05.2024
 */
public class AnimationHandler {
    private List<BufferedImage> frames;
    private BufferedImage staticImage;
    private int currentFrame;
    private long lastUpdate;
    private long frameInterval;
    private final String basePath;

    /**
     * Konštruktor triedy AnimationHandler.
     *
     * @param frameCount počet snímok animácie
     * @param extension prípona súborov obrázkov
     * @param path cesta k obrázkom
     */
    public AnimationHandler(int frameCount, String extension, String path) {
        this.basePath = path;
        this.frames = new ArrayList<>();
        this.loadImages(frameCount, extension);
        this.currentFrame = 0;
        this.lastUpdate = System.currentTimeMillis();
        this.frameInterval = 100;
    }

    /**
     * Vráti šírku aktuálneho obrázka.
     *
     * @return šírka obrázka
     */
    public int getImageX() {
        return this.getCurrentFrame().getWidth();
    }

    /**
     * Vráti výšku aktuálneho obrázka.
     *
     * @return výška obrázka
     */
    public int getImageY() {
        return this.getCurrentFrame().getHeight();
    }

    /**
     * Načíta obrázky animácie.
     *
     * @param frameCount počet snímok animácie
     * @param extension prípona súborov obrázkov
     */
    private void loadImages(int frameCount, String extension) {
        try {
            for (int i = 1; i <= frameCount; i++) {
                BufferedImage image = ImageIO.read(new File(this.basePath + i + extension));
                this.frames.add(image);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Vráti aktuálny snímok animácie.
     *
     * @return aktuálny snímok
     */
    public BufferedImage getCurrentFrame() {
        if (!this.frames.isEmpty()) {
            return this.frames.get(this.currentFrame);
        } else if (this.staticImage != null) {
            return this.staticImage;
        }
        return null;
    }

    /**
     * Vráti aktuálny obrázok animácie.
     *
     * @return aktuálny obrázok
     */
    public BufferedImage image() {
        BufferedImage sourceImage = this.getCurrentFrame();
        if (sourceImage == null) {
            return null;
        }
        return sourceImage;
    }

    /**
     * Vykreslí aktuálny obrázok na obrazovku.
     *
     * @param g grafický kontext
     * @param x súradnica x
     * @param y súradnica y
     */
    public void draw(Graphics g, int x, int y) {
        BufferedImage image = this.image();
        if (image != null) {
            Graphics2D g2 = (Graphics2D)g;
            g2.drawImage(image, x, y, null);
        }
    }
}
