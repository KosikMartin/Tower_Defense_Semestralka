package towerdefense.npc;

import java.awt.Color;

/**
 * Trieda pre štandardných nepriateľov v hre Tower Defense.
 *
 * Autor: Martin Košík
 * Dátum: 19.05.2024
 */
public class StandardNPC extends Npc {

    /**
     * Konštruktor pre triedu StandardNPC.
     *
     * @param speed rýchlosť
     * @param x počiatočná súradnica X
     * @param y počiatočná súradnica Y
     * @param direction smer pohybu
     * @param hp zdravie
     */
    public StandardNPC(int speed, int x, int y, Direction direction, double hp) {
        super(speed, x, y, direction, hp, Color.DARK_GRAY);
    }

    /**
     * Prázdny konštruktor pre triedu StandardNPC.
     */
    public StandardNPC() {
        super();
    }

    @Override
    public int getReward() {
        return 120;
    }

    @Override
    public void calculateDmg(double damage) {
        super.takeDamage(damage);
    }

    @Override
    public void onDeath() {
        super.getSoundPlayer().play("src/TowerDefense/Resources/Sounds/NpcDeath.wav");
    }
}
