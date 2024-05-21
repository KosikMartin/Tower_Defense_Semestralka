package towerdefense.npc;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

/**
 * Trieda pre tvrdých nepriateľov v hre Tower Defense.
 *
 * Autor: Martin Košík
 * Dátum: 19.05.2024
 */
public class HardenedNPC extends Npc {
    private boolean isArmored;
    private double shield;
    private double maxShield = 250.0;
    private boolean isBossNpc = false;
    private Color[] bossColors = {Color.RED, Color.GREEN, Color.YELLOW, Color.MAGENTA, Color.BLACK};
    private int colorIndex = 0;

    /**
     * Konštruktor pre triedu HardenedNPC.
     *
     * @param speed rýchlosť
     * @param x počiatočná súradnica X
     * @param y počiatočná súradnica Y
     * @param direction smer pohybu
     * @param hp zdravie
     */
    public HardenedNPC(int speed, int x, int y, Direction direction, double hp) {
        super(speed, x, y, direction, hp, Color.red);
        Random r = new Random();

        this.isArmored = r.nextDouble() < 0.45;
        this.shield = 100.0 + r.nextDouble() * (this.maxShield - 100.0);
        this.isBossNpc = r.nextDouble() < 0.07;

        if (this.isBossNpc) {
            for (int i = 0; i < 5; i++) {
                System.out.println("==BOSS NPC IS NEAR==");
            }
            this.maxShield = 1000;
            this.shield = 1000;
        }
    }

    /**
     * Prázdny konštruktor pre triedu HardenedNPC.
     */
    public HardenedNPC() {
        super();
    }
    /**
     * Vypočíta zisk na základe toho aké silné je npc
     */
    @Override
    public int getReward() {
        if (this.isBossNpc) {
            return 3500;
        }
        if (this.isArmored && this.shield > 100) {
            return 500;
        } else if (this.isArmored) {
            return 400;
        }
        return 300;
    }
    /**
     * Vykresluje NPC a jeho health a shield bar
     */
    @Override
    public void draw(Graphics2D g2d) {
        super.draw(g2d);

        if (this.isBossNpc) {
            this.setColor(this.bossColors[this.colorIndex]);
            this.colorIndex = (this.colorIndex + 1) % this.bossColors.length;
        }
        int offset = this.getY() - 20;
        if (this.shield > 0) {
            int shieldBarWidth = 30;
            int shieldBarHeight = 5;
            int shieldBarX = this.getX();
            int shieldBarY = offset - 10;
            double shieldRatio = this.shield / this.maxShield;
            int currentShieldBarWidth = (int)(shieldBarWidth * shieldRatio);

            g2d.setColor(Color.DARK_GRAY);
            g2d.fillRect(shieldBarX, shieldBarY, shieldBarWidth, shieldBarHeight);

            g2d.setColor(Color.CYAN);
            g2d.fillRect(shieldBarX, shieldBarY, currentShieldBarWidth, shieldBarHeight);

            g2d.setColor(Color.BLACK);
            g2d.drawRect(shieldBarX, shieldBarY, shieldBarWidth, shieldBarHeight);
        }
    }

    /**
     * Vypočítavam damage na základe toho aké silné je npc.
     */

    @Override
    public void calculateDmg(double damage) {
        double remainingDamage;
        if (this.isBossNpc) {
            damage *= 0.40;
        }

        if (this.isArmored) {
            System.out.println("ARMORED NPC INCOMMING");
            double damageToShield = damage * 0.70;

            if (this.shield >= damageToShield) {
                this.shield -= damageToShield;
                System.out.println("Remaining shield: " + this.shield);
            } else {
                remainingDamage = damage - this.shield * (4.0 / 3.0);
                this.shield = 0.0;
                System.out.println("Shield depleted");
                super.takeDamage(remainingDamage * 0.75);
                System.out.println("Damage taken: " + remainingDamage * 0.75);
            }
        } else {
            if (this.shield >= damage) {
                this.shield -= damage;
            } else {
                remainingDamage = damage - this.shield;
                this.shield = 0.0;
                super.takeDamage(remainingDamage);
            }
        }
    }

    @Override
    public void onDeath() {
        super.getSoundPlayer().play("src/TowerDefense/Resources/Sounds/ArmoredDeath.wav");
    }
}
