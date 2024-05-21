package towerdefense.defendingobjects;

import towerdefense.gamelogic.AnimationHandler;
import towerdefense.gamelogic.SoundPlayer;
import towerdefense.npc.Npc;
import towerdefense.npc.HardenedNPC;

import java.awt.Color;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Trieda reprezentujúca magickú vežu v hre Tower Defense.
 *
 * Autor: Martin Košík
 * Dátum: 19.05.2024
 */
public class MagicTurret extends DefenseObject {
    private List<Npc> targets;
    private long lastShotTime;
    private final AnimationHandler baseHandler;
    private long startTime;
    private final SoundPlayer soundPlayer;

    /**
     * Konštruktor triedy MagicTurret.
     * Inicializuje vežu s preddefinovanými hodnotami.
     */
    public MagicTurret() {
        super(600, 150, 0.3, 150);
        this.baseHandler = new AnimationHandler(1, ".png", "src/towerdefense/resources/images/towerMage");
        this.targets = new ArrayList<>();
        this.lastShotTime = System.currentTimeMillis();
        this.startTime = System.currentTimeMillis();
        this.soundPlayer = new SoundPlayer();
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
        this.baseHandler.draw(g, super.getX(), super.getY());
        int radius = super.getRange();
        int diameter = radius * 2;
        g.setColor(Color.BLACK);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
        g.drawOval(super.getX() - radius + 23, super.getY() - radius + 54, diameter, diameter);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }

    /**
     * Aktualizuje stav veže a kontroluje nepriateľov v dosahu.
     *
     * @param npcs zoznam nepriateľov
     */
    @Override
    public void update(ArrayList<Npc> npcs) {
        if (this.targets.isEmpty() || !this.areInRange(this.targets, npcs)) {
            this.targets = this.findTargetsInRange(npcs);
        }
        if (!this.targets.isEmpty()) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - this.lastShotTime >= 1000 / super.getRateOfFire()) {
                this.shoot();
                this.lastShotTime = currentTime;
            }
        }
    }

    /**
     * Zameria nepriateľa.
     *
     * @param npc nepriateľský NPC
     */
    @Override
    public void targetEnemy(Npc npc) {
        if (!this.targets.contains(npc)) {
            this.targets.add(npc);
        }
    }

    /**
     * Vystrelí na ciele.
     */
    @Override
    public void shoot() {
        for (Npc target : this.targets) {
            double damage = super.getDamage();
            this.soundPlayer.play("src/TowerDefense/Resources/Sounds/magicHit.wav");
            if (target instanceof HardenedNPC) {
                damage *= 2.5;
            } else {
                damage *= 0.60;
            }
            target.calculateDmg(damage);
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
        Npc closestNpc = null;
        double closestDistance = Double.MAX_VALUE;
        for (Npc npc : npcs) {
            double distance = Math.hypot(npc.getX() - super.getX(), npc.getY() - super.getY());
            if (distance <= super.getRange() && distance < closestDistance) {
                closestNpc = npc;
                closestDistance = distance;
            }
        }
        return closestNpc;
    }

    /**
     * Skontroluje, či sú ciele v dosahu.
     *
     * @param targets zoznam cieľov
     * @param npcs zoznam nepriateľov
     * @return true, ak sú ciele v dosahu, inak false
     */
    private boolean areInRange(List<Npc> targets, ArrayList<Npc> npcs) {
        for (Npc target : targets) {
            if (!npcs.contains(target) || !this.isInRange(target)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Nájde ciele v dosahu.
     *
     * @param npcs zoznam nepriateľov
     * @return zoznam cieľov v dosahu
     */
    private List<Npc> findTargetsInRange(ArrayList<Npc> npcs) {
        List<Npc> closestNpcs = new ArrayList<>();
        npcs.sort((npc1, npc2) -> Double.compare(
                Math.hypot(npc1.getX() - super.getX(), npc1.getY() - super.getY()),
                Math.hypot(npc2.getX() - super.getX(), npc2.getY() - super.getY())
        ));
        for (Npc npc : npcs) {
            if (closestNpcs.size() >= 2) {
                break;
            }
            if (this.isInRange(npc)) {
                closestNpcs.add(npc);
            }
        }
        return closestNpcs;
    }

    /**
     * Skontroluje, či je NPC v dosahu.
     *
     * @param npc nepriateľský NPC
     * @return true, ak je NPC v dosahu, inak false
     */
    private boolean isInRange(Npc npc) {
        double distance = Math.hypot(npc.getX() - super.getX(), npc.getY() - super.getY());
        return distance <= super.getRange();
    }
}
