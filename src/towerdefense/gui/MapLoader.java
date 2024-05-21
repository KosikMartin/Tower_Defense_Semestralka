package towerdefense.gui;

import towerdefense.tiles.Tile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Trieda pre načítanie mapy v hre Tower Defense.
 *
 * Autor: Martin Košík
 * Dátum: 19.05.2024
 */
public class MapLoader {
    private Tile[][] tiles;
    private Map<String, Boolean> collisionMap = new HashMap<>();

    /**
     * Konštruktor triedy MapLoader.
     *
     * @param mapFilename názov súboru s mapou
     * @param tileDataFilename názov súboru s údajmi o dlaždiciach
     */
    public MapLoader(String mapFilename, String tileDataFilename) {
        this.tiles = new Tile[50][50];
        this.loadCollisionData(tileDataFilename);
        this.loadTiles(mapFilename);
    }

    /**
     * Načíta údaje o kolíziách zo súboru.
     *
     * @param filename názov súboru
     */
    private void loadCollisionData(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String imageFile;
            while ((imageFile = reader.readLine()) != null) {
                String collisionData = reader.readLine(); // Číta údaj o kolízii
                boolean collision = Boolean.parseBoolean(collisionData);
                this.collisionMap.put(imageFile.trim(), collision);
                // DEBUG PRINTOUT System.out.println("Loaded collision data: " + imageFile.trim() + " -> " + collision);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Načíta dlaždice zo súboru.
     *
     * @param filename názov súboru s mapou
     */
    private void loadTiles(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int row = 0;
            while ((line = reader.readLine()) != null) {
                StringTokenizer tokenizer = new StringTokenizer(line);
                int col = 0;
                while (tokenizer.hasMoreTokens()) {
                    String tileId = tokenizer.nextToken();
                    String imagePath = String.format("/towerdefense/Resources/tileimages/%03d.png", Integer.parseInt(tileId)).trim();
                    String collisionKey = String.format("%03d.png", Integer.parseInt(tileId)).trim(); // Normalizuje cestu pre mapu kolízií
                    // DEBUG PRINTOUT System.out.println("Generated image path: " + imagePath);
                    boolean collision = this.collisionMap.getOrDefault(collisionKey, false);
                    // DEBUG PRINTOUT System.out.println("Collision status for " + collisionKey + ": " + collision);
                    this.tiles[row][col] = new Tile(imagePath, collision, tileId);
                    // DEBUG PRINTOUT System.out.println("Created tile: " + imagePath + " with collision: " + collision);
                    // DEBUG PRINTOUT System.out.println("Position: " + row + ", " + col);
                    col++;
                }
                row++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Vráti dvojrozmerné pole dlaždíc, resp jeho kópiu.
     *
     * @return dvojrozmerné pole dlaždíc
     */
    public Tile[][] getTiles() {
        Tile[][] tilesCopy = new Tile[50][50];
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 50; j++) {
                if (this.tiles[i][j] != null) {
                    tilesCopy[i][j] = new Tile(this.tiles[i][j].getImagePath(), this.tiles[i][j].hasCollision(), this.tiles[i][j].getId());
                } else {
                    tilesCopy[i][j] = null;
                }
            }
        }
        return tilesCopy;
    }
}
