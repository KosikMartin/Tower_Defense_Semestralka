package towerdefense.defendingobjects;

import towerdefense.gamelogic.AnimationHandler;
import towerdefense.gamelogic.SoundPlayer;
import towerdefense.npc.Npc;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Trieda reprezentujúca vežu s lukostreľbou v hre Tower Defense.
 *
 * Autor: Martin Košík
 * Dátum: 19.05.2024
 */
public class ArrowTurret extends DefenseObject {

    private BufferedImage base;
    private BufferedImage turret;
    private final AnimationHandler baseHandler;
    private final AnimationHandler turretHandler;
    private double lastShotTime;
    private Npc target;
    private final SoundPlayer soundPlayer;

    /**
     * Konštruktor triedy ArrowTurret.
     * Inicializuje vežu s preddefinovanými hodnotami.
     */
    public ArrowTurret() {
        super(100, 150, 1.0, 30);
        this.soundPlayer = new SoundPlayer();
        this.baseHandler = new AnimationHandler(1, ".png", "src/towerdefense/resources/images/TowerLVL1Base");
        this.turretHandler = new AnimationHandler(1, ".png", "src/towerdefense/resources/images/Arrow");

        this.lastShotTime = 0;
        this.target = null;
    }

    /**
     * Vráti základňu veže.
     *
     * @return základný AnimationHandler veže
     */
    @Override
    public AnimationHandler getBase() {
        return this.baseHandler;
    }

    /**
     * Vykreslí vežu na obrazovke.
     *
     * @param g grafický kontext
     */
    @Override
    public void draw(Graphics2D g) {
        Graphics2D g2d = (Graphics2D)g;
        this.baseHandler.draw(g, super.getX(), super.getY());
        this.turretHandler.draw(g, super.getX() - 23, super.getY() - 14);
        int radius = super.getRange();
        int diameter = radius * 2;
        g2d.setColor(Color.BLACK);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
        g2d.drawOval(super.getX() - radius + 23, super.getY() - radius + 54, diameter, diameter);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }

    /**
     * Zameria nepriateľa. (Metóda zatiaľ neimplementovaná)
     *
     * @param npc nepriateľský NPC
     */
    @Override
    public void targetEnemy(Npc npc) {
        //NOT USED
    }

    /**
     * Skontroluje, či je cieľ v dosahu.
     *
     * @return true, ak je cieľ v dosahu, inak false
     */
    private boolean isTargetInRange() {
        if (this.target == null) {
            return false;
        }
        double distance = Math.sqrt(Math.pow(this.target.getX() - this.getX(), 2) + Math.pow(this.target.getY() - this.getY(), 2));
        // Check if the target is within the shooting range and is alive
        return distance <= super.getRange() && this.target.getHp() > 0;
    }

    /**
     * Vystrelí na cieľ.
     */
    @Override
    public void shoot() {
        double currentTime = System.currentTimeMillis() / 1000.0;
        if (this.target != null && (currentTime - this.lastShotTime >= 1.0 / super.getRateOfFire())) {
            this.target.calculateDmg(super.getDamage());
            this.soundPlayer.play("src/towerdefense/resources/sounds/Hit.wav");
            this.lastShotTime = currentTime;
        }
    }

    /**
     * Aktualizuje stav veže a kontroluje nepriateľov v dosahu.
     *
     * @param npcs zoznam nepriateľov
     */
    @Override
    public void update(ArrayList<Npc> npcs) {
        if (this.target == null || !this.isTargetInRange() || this.target.getHp() <= 0) {
            this.target = this.npcInRange(npcs);
        }
        if (this.target != null && this.target.getHp() > 0) {
            this.shoot();
        } else {
            this.target = null;
        }
    }

    /**
     * Vráti nepriateľa v dosahu veže.
     *
     * @param npcs zoznam nepriateľov
     * @return najbližší nepriateľský NPC
     */
    @Override
    public Npc npcInRange(ArrayList<Npc> npcs) {
        Npc closestTarget = null;
        double minDistance = Double.MAX_VALUE;

        for (Npc npc : npcs) {
            if (npc.getHp() > 0) {
                double distance = Math.sqrt(Math.pow(npc.getX() - this.getX(), 2) + Math.pow(npc.getY() - this.getY(), 2));
                if (distance < minDistance && distance <= super.getRange()) {
                    minDistance = distance;
                    closestTarget = npc;
                }
            }
        }
        return closestTarget;
    }
}
