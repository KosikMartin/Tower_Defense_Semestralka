package towerdefense.gamelogic;

import towerdefense.tiles.Tile;

/**
 * Trieda na detekciu kolízií v hre Tower Defense.
 *
 * Autor: Martin Košík
 * Dátum: 19.05.2024
 */
public class CollisionDetector {
    private final Tile[][] tiles;
    private final double tileHeight = 14.02;
    private final double tileWidth = 15.02;

    /**
     * Konštruktor triedy CollisionDetector.
     *
     * @param tiles dvojrozmerné pole dlaždíc
     */
    public CollisionDetector(Tile[][] tiles) {
        this.tiles = tiles;
    }

    /**
     * Skontroluje, či je možné umiestniť vežu na dané súradnice.
     *
     * @param pixelX súradnica X v pixeloch
     * @param pixelY súradnica Y v pixeloch
     * @return true, ak je možné umiestnenie, inak false
     */
    public boolean isValidPlacement(int pixelX, int pixelY) {
        int gridX = (int)(pixelX / this.tileWidth);
        int gridY = (int)(pixelY / this.tileHeight);

        System.out.println("ValidPlacementCoords: " + gridX + ", " + gridY);

        if (gridX < 0 || gridX >= this.tiles[0].length || gridY < 0 || gridY >= this.tiles.length) {
            return false;
        }

        Tile tile = this.tiles[gridY][gridX];

        // Out of bounds
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // North, South, West, East
        for (int[] dir : directions) {
            int newY = gridY + dir[0];
            int newX = gridX + dir[1];
            if (newX >= 0 && newX < this.tiles[0].length && newY >= 0 && newY < this.tiles.length) {
                Tile neighborTile = this.tiles[newY][newX];
                if (neighborTile != null && neighborTile.hasCollision()) {
                    System.out.println("Neighbor tile has collision at: " + newX + ", " + newY);
                    return false;
                }
            }
        }

        if (tile != null && !tile.hasCollision()) {
            return true;
        }

        System.out.println("Tile has collision at: " + gridX + ", " + gridY);
        return false;
    }

    /**
     * Skontroluje, či je nasledujúca dlaždica cesta.
     *
     * @param currentX aktuálna súradnica X
     * @param currentY aktuálna súradnica Y
     * @param direction smer pohybu
     * @return true, ak je nasledujúca dlaždica cesta, inak false
     */
    public boolean isNextTilePath(int currentX, int currentY, int direction) {
        int[] dx = {0, 40, 0, -40}; // East, South, West, North
        int[] dy = {-7, 0, 40, 0}; // North, East, South, West

        int nextX = (int)((currentX + dx[direction]) / this.tileWidth);
        int nextY = (int)((currentY + dy[direction]) / this.tileHeight);

        if (nextX < 0 || nextX >= this.tiles[0].length || nextY < 0 || nextY >= this.tiles.length) {
            //System.out.println("TILE OUT OF BOUNDS");
            return false;  // Next tile is out of bounds
        }

        Tile nextTile = this.tiles[nextY][nextX];
        if (nextTile != null && nextTile.getId().equals("17")) {
            return true;
        }

        return false;
    }

    /**
     * Skontroluje, či je na danej dlaždici konkrétne ID dlaždice.
     *
     * @param x súradnica X
     * @param y súradnica Y
     * @param tileId ID dlaždice
     * @return true, ak dlaždica obsahuje dané ID, inak false
     */
    public boolean isAtTileId(int x, int y, String tileId) {
        int gridX = (int)(x / this.tileWidth);
        int gridY = (int)(y / this.tileHeight);

        if (gridX < 0 || gridX >= this.tiles[0].length || gridY < 0 || gridY >= this.tiles.length) {
            return false;
        }

        Tile tile = this.tiles[gridY][gridX];
        return tile != null && tile.getId().equals(tileId);
    }
}
