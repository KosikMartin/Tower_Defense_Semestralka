package towerdefense.abilities;

import towerdefense.gamelogic.GameEngine;
import towerdefense.defendingobjects.DefenseObject;

import javax.swing.ImageIcon;
import java.util.ArrayList;

/**
 * Trieda reprezentujúca schopnosť SPEEDUP v hre Tower Defense.
 *
 * Autor: Martin Košík
 * Dátum: 19.05.2024
 */
public class SPEEDUP implements Ability {
    private ImageIcon icon = new ImageIcon("src/TowerDefense/Resources/Images/speedbutton.png");
    private int duration = 10; // Effect duration in seconds

    /**
     * Vykoná schopnosť zrýchlenia na daných súradniciach.
     *
     * @param gameEngine herný engine
     * @param x súradnica x
     * @param y súradnica y
     * @return true, ak bola schopnosť úspešne vykonaná, inak false
     */
    @Override
    public boolean execute(GameEngine gameEngine, int x, int y) {
        if (gameEngine.getPlayer().getMoney() < this.getPrice()) {
            gameEngine.getSoundPlayer().play("src/TowerDefense/Resources/Sounds/NoMoney.wav");
            return false;
        }
        gameEngine.getPlayer().subtractMoney(this.getPrice());
        ArrayList<DefenseObject> turrets = gameEngine.getTurrets();

        for (DefenseObject turret : turrets) {
            turret.rateAndDamageMultiplier(1.7, this.duration);
        }
        System.out.println("SPEED UP ACTIVATED");
        gameEngine.getSoundPlayer().play("src/TowerDefense/Resources/Sounds/SPEED_UP.wav");
        return true;
    }

    /**
     * Vráti cenu schopnosti.
     *
     * @return cena schopnosti
     */
    @Override
    public int getPrice() {
        return 200;
    }
}
