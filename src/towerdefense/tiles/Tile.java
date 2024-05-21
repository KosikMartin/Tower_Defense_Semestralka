package towerdefense.tiles;

import javax.swing.ImageIcon;

/**
 * Trieda pre dlaždice mapy v hre Tower Defense.
 *
 * Autor: Martin Košík
 * Dátum: 19.05.2024
 */
public class Tile {
    private static final int TILE_WIDTH = 16;
    private static final int TILE_HEIGHT = 16;
    private ImageIcon image;
    private boolean collision;
    private String imagePath;
    private String tileIdentifier;

    /**
     * Konštruktor pre triedu Tile.
     *
     * @param imagePath cesta k obrázku dlaždice
     * @param collision či má dlaždica kolíziu
     * @param id identifikátor dlaždice
     */
    public Tile(String imagePath, boolean collision, String id) {
        this.imagePath = imagePath;
        this.tileIdentifier = id;
        try {
            this.image = new ImageIcon(getClass().getResource(imagePath));
        } catch (Exception e) {
            System.err.println("Error loading image: " + imagePath);
            this.image = null;
        }
        this.collision = collision;
    }

    /**
     * Vráti identifikátor dlaždice.
     *
     * @return identifikátor dlaždice
     */
    public String getId() {
        return this.tileIdentifier;
    }

    /**
     * Vráti obrázok dlaždice.
     *
     * @return obrázok dlaždice
     */
    public ImageIcon getImage() {
        return this.image;
    }

    /**
     * Kontroluje, či má dlaždica kolíziu.
     *
     * @return true, ak má dlaždica kolíziu, inak false
     */
    public boolean hasCollision() {
        return this.collision;
    }

    /**
     * Vráti cestu k obrázku dlaždice.
     *
     * @return cesta k obrázku dlaždice
     */
    public String getImagePath() {
        return this.imagePath;
    }
}
