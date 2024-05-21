package towerdefense.defendingobjects;

import towerdefense.gamelogic.AnimationHandler;
import towerdefense.gui.Drawable;
import towerdefense.npc.Npc;

import java.util.ArrayList;

/**
 * Abstraktná trieda reprezentujúca obranný objekt v hre Tower Defense.
 *
 * Autor: Martin Košík
 * Dátum: 19.05.2024
 */
public abstract class DefenseObject implements Drawable {

    private int cost;
    private int range;
    private double rateOfFire;
    private double damage;
    private int x;
    private int y;
    private double defaultRateOfFire;
    private double defaultDamage;
    private int defaultRange;

    /**
     * Konštruktor triedy DefenseObject.
     *
     * @param cost náklady na stavbu
     * @param range dosah útoku
     * @param rateOfFire rýchlosť streľby
     * @param damage poškodenie
     */
    public DefenseObject(int cost, int range, double rateOfFire, double damage) {
        this.cost = cost;
        this.range = range;
        this.rateOfFire = rateOfFire;
        this.damage = damage;
        this.defaultDamage = damage;
        this.defaultRateOfFire = rateOfFire;
        this.defaultRange = range;
    }

    /**
     * Vráti cenu veže.
     *
     * @return cena veže
     */
    public int getPrice() {
        return this.cost;
    }

    public abstract void update(ArrayList<Npc> npcs);
    public abstract void targetEnemy(Npc npc);
    public abstract void shoot();
    public abstract Npc npcInRange(ArrayList<Npc> npcs);
    public abstract AnimationHandler getBase();

    /**
     * Vráti poškodenie veže.
     *
     * @return poškodenie
     */
    public double getDamage() {
        return this.damage;
    }

    /**
     * Vráti dosah veže.
     *
     * @return dosah
     */
    public int getRange() {
        return this.range;
    }

    /**
     * Vráti rýchlosť streľby veže.
     *
     * @return rýchlosť streľby
     */
    public double getRateOfFire() {
        return this.rateOfFire;
    }

    /**
     * Vráti súradnicu X veže.
     *
     * @return súradnica X
     */
    public int getX() {
        return this.x;
    }

    /**
     * Vráti súradnicu Y veže.
     *
     * @return súradnica Y
     */
    public int getY() {
        return this.y;
    }

    /**
     * Nastaví pozíciu veže.
     *
     * @param x súradnica X
     * @param y súradnica Y
     */
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Nastaví multiplikátor poškodenia a rýchlosti streľby.
     *
     * @param multiplier multiplikátor
     * @param duration doba trvania v sekundách
     */
    public void rateAndDamageMultiplier(double multiplier, int duration) {
        this.range *= multiplier;
        this.damage *= 1.5;
        this.rateOfFire *= multiplier;

        new java.util.Timer().schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                DefenseObject.this.defaultSettings();
            }
        }, duration * 1000);
    }

    /**
     * Obnoví predvolené nastavenia veže.
     */
    public void defaultSettings() {
        this.rateOfFire = this.defaultRateOfFire;
        this.damage = this.defaultDamage;
        this.range = this.defaultRange;
    }
}
