package towerdefense.gui;

import java.awt.Graphics2D;

/**
 * Rozhranie pre vykresľovanie objektov v hre Tower Defense.
 *
 * Autor: Martin Košík
 * Dátum: 19.05.2024
 */
public interface Drawable {

    /**
     * Vykreslí objekt na obrazovku.
     *
     * @param g2d grafický kontext
     */
    void draw(Graphics2D g2d);
}
