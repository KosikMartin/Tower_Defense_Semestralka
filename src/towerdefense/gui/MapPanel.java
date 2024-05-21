package towerdefense.gui;

import towerdefense.abilities.BOMB;
import towerdefense.defendingobjects.DefenseObject;
import towerdefense.gamelogic.GameEngine;
import towerdefense.npc.Npc;
import towerdefense.tiles.Tile;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

/**
 * Trieda pre vykresľovanie panelu mapy v hre Tower Defense.
 *
 * Autor: Martin Košík
 * Dátum: 19.05.2024
 */
public class MapPanel {
    private Tile[][] tiles;
    private GameEngine gameEngine;
    private MapLoader mapLoader;
    private int mouseX;
    private int mouseY;
    private JPanel panel; // The new JPanel attribute

    /**
     * Konštruktor triedy MapPanel.
     *
     * @param mapFile názov súboru s mapou
     * @param tileDataFile názov súboru s údajmi o dlaždiciach
     */
    public MapPanel(String mapFile, String tileDataFile) {
        this.panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                MapPanel.this.paintMapPanel(g); // Call the paintMapPanel method to handle the custom painting
            }
        };
        this.mapLoader = new MapLoader(mapFile, tileDataFile);
        this.tiles = this.mapLoader.getTiles();

        this.panel.setBackground(Color.BLACK);
        this.setupMouseListeners();
        this.setupMouseMotionListeners();
    }

    /**
     * Vráti objekt MapLoader.
     *
     * @return objekt MapLoader
     */
    public MapLoader getMapLoader() {
        return this.mapLoader;
    }

    /**
     * Vráti objekt JPanel.
     *
     * @return objekt JPanel
     */
    public JPanel getPanel() {
        return this.panel;
    }

    /**
     * Vykreslí mapu na panel.
     *
     * @param g grafický kontext
     */
    public void paintMapPanel(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;

        for (int i = 0; i < this.tiles.length; i++) {
            for (int j = 0; j < this.tiles[i].length; j++) {
                Tile tile = this.tiles[i][j];
                if (tile != null && tile.getImage() != null) {
                    Image img = tile.getImage().getImage();
                    int tileWidth = this.panel.getWidth() / this.tiles[0].length;
                    int tileHeight = this.panel.getHeight() / this.tiles.length;
                    g2d.drawImage(img, j * tileWidth, i * tileHeight, tileWidth, tileHeight, this.panel);
                }
            }
        }

        this.drawDrawableObjects(g2d, this.gameEngine.getTurrets());
        this.drawDrawableObjects(g2d, this.gameEngine.getNpcs());

        for (DefenseObject turret : this.gameEngine.getTurrets()) {
            turret.draw(g2d);
        }

        this.gameEngine.getPlayer().draw(g2d);
        if (this.gameEngine.getSelectedAbility() instanceof BOMB) {
            g2d.setColor(Color.RED);
            int radius = 100; // Bomb radius
            g2d.drawOval(this.mouseX - radius, this.mouseY - radius, 2 * radius, 2 * radius);
        }

        synchronized (this.gameEngine.getNpcs()) {
            for (Npc npc : this.gameEngine.getNpcs()) {
                npc.draw(g2d);
            }
        }
    }

    /**
     * Nastaví herný engine.
     *
     * @param gameEngine herný engine
     */
    public void setGameEngine(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    /**
     * Nastaví poslucháčov myši.
     */
    private void setupMouseListeners() {
        MapPanel.this.panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (MapPanel.this.gameEngine.isUsingAbility()) {
                    if (MapPanel.this.gameEngine.useAbility(e.getX(), e.getY())) {
                        System.out.println("Ability used at: (" + e.getX() + ", " + e.getY() + ")");
                    } else {
                        System.out.println("Failed to use ability.");
                    }
                } else if (MapPanel.this.gameEngine.isPlacing()) {
                    try {
                        if (MapPanel.this.gameEngine.placeTurret(e.getX(), e.getY())) {
                            MapPanel.this.gameEngine.setPlacingMode(false);
                        }
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    System.out.println("Not in placing or ability mode.");
                }
            }
        });
    }

    /**
     * Nastaví poslucháčov pohybu myši.
     */
    private void setupMouseMotionListeners() {
        MapPanel.this.panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                MapPanel.this.mouseX = e.getX();
                MapPanel.this.mouseY = e.getY();
                MapPanel.this.panel.repaint();
            }
        });
    }

    /**
     * Vykreslí objekty implementujúce rozhranie Drawable.
     *
     * @param g2d grafický kontext
     * @param drawables zoznam objektov na vykreslenie
     */
    private void drawDrawableObjects(Graphics2D g2d, ArrayList<? extends Drawable> drawables) {
        for (Drawable drawable : drawables) {
            drawable.draw(g2d);
        }
    }
}
