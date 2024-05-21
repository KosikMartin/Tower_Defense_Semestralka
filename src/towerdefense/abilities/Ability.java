package towerdefense.abilities;

import towerdefense.gamelogic.GameEngine;

/**
 * Rozhranie schopnosti v hre Tower Defense.
 * Definuje metódy pre vykonanie schopnosti a získanie ceny schopnosti.
 *
 * Autor: Martin Košík
 * Dátum: 19.05.2024
 */
public interface Ability {

    /**
     * Vykoná schopnosť na daných súradniciach.
     *
     * @param gameEngine herný engine
     * @param x súradnica x
     * @param y súradnica y
     * @return true, ak bola schopnosť úspešne vykonaná, inak false
     */
    boolean execute(GameEngine gameEngine, int x, int y); // Added x, y for abilities like bomb

    /**
     * Vráti cenu schopnosti.
     *
     * @return cena schopnosti
     */
    int getPrice();
}
