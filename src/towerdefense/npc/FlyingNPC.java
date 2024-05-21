package towerdefense.npc;

import java.awt.Color;
import java.util.Random;

/**
 * Trieda pre lietajúcich nepriateľov v hre Tower Defense.
 *
 * Autor: Martin Košík
 * Dátum: 19.05.2024
 */
public class FlyingNPC extends Npc {
    private int dodgeChance; // Šanca na úhyb v percentách
    private boolean hasDodge; // Či má NPC schopnosť úhybu

    /**
     * Konštruktor pre triedu FlyingNPC.
     *
     * @param speed rýchlosť
     * @param startX počiatočná súradnica X
     * @param startY počiatočná súradnica Y
     * @param direction smer pohybu
     * @param hp zdravie
     */
    public FlyingNPC(int speed, int startX, int startY, Direction direction, double hp) {
        super(speed * 2, startX, startY, direction, hp, Color.YELLOW);
        this.dodgeChance = this.generateDodgeChance();
        this.hasDodge = this.generateHasDodge();
    }

    /**
     * Generuje šancu na úhyb.
     *
     * @return šanca na úhyb v percentách
     */
    private int generateDodgeChance() {
        Random random = new Random();
        return random.nextInt(41) + 10;
    }

    /**
     * Generuje, či má NPC schopnosť úhybu.
     *
     * @return true, ak má NPC schopnosť úhybu, inak false
     */
    private boolean generateHasDodge() {
        Random random = new Random();
        return random.nextBoolean();
    }

    @Override
    public int getReward() {
        if (this.hasDodge && this.dodgeChance > 40) {
            return 525 + this.dodgeChance;
        } else if (this.hasDodge) {
            return 300 + this.dodgeChance;
        } else {
            return 250;
        }
    }

    /**
     * Skontroluje, či by sa NPC malo vyhnúť útoku.
     *
     * @return true, ak sa NPC vyhne útoku, inak false
     */
    private boolean shouldDodge() {
        Random random = new Random();
        int roll = random.nextInt(100); // Generuje číslo medzi 0 a 99
        return roll < this.dodgeChance;
    }

    @Override
    public void calculateDmg(double damage) {
        if (this.hasDodge) {
            System.out.println("FlyingNPC dodged the attack!");
            this.hasDodge = false; // Resetuje schopnosť úhybu
        } else if (this.shouldDodge()) {
            System.out.println("FlyingNPC dodged the attack!");
        } else {
            super.takeDamage(damage);
            System.out.println("FlyingNPC took " + damage + " damage. Remaining HP: " + super.getHp());
        }
    }

    @Override
    public void onDeath() {
        super.getSoundPlayer().play("src/TowerDefense/Resources/Sounds/NpcDeath.wav");
    }
}
