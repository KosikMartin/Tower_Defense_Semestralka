package towerdefense.defendingobjects;

import towerdefense.gamelogic.AnimationHandler;
import towerdefense.npc.Npc;
import towerdefense.npc.HardenedNPC;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.awt.AlphaComposite;
import java.awt.Color;

/**
 * Trieda reprezentujúca vežu s oblasťovým účinkom v hre Tower Defense.
 *
 * Autor: Martin Košík
 * Dátum: 19.05.2024
 */
public class AreaTurret extends DefenseObject {
    private AnimationHandler baseHandler;
    private AnimationHandler turretHandler;
    private double lastShotTime;
    private ArrayList<Npc> npcsInRange;
    private long startTime;

    /**
     * Konštruktor triedy AreaTurret.
     * Inicializuje vežu s preddefinovanými hodnotami.
     */
    public AreaTurret() {
        super(450, 100, 10, 53);
        this.baseHandler = new AnimationHandler(1, ".png", "src/towerdefense/resources/images/AreaTurret");
        this.turretHandler = new AnimationHandler(1, ".png", "src/towerdefense/resources/images/ManaCrystal");
        this.lastShotTime = 0; // To shoot immediately upon creation
        this.npcsInRange = new ArrayList<>();
        this.startTime = System.currentTimeMillis();
    }

    /**
     * Zameria nepriateľa. (Metóda zatiaľ neimplementovaná)
     *
     * @param npc nepriateľský NPC
     */
    @Override
    public void targetEnemy(Npc npc) {
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
     * Vystrelí na nepriateľov v dosahu.
     */
    @Override
    public void shoot() {
        for (Npc npc : this.npcsInRange) {
            npc.calculateDmg(super.getDamage() * 0.06); // Apply 10% of the turret's damage
        }
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

        long currentTime = System.currentTimeMillis();
        double elapsedTime = (currentTime - this.startTime) / 1000.0;
        int amplitude = 5;
        int offsetY = (int)(Math.sin(elapsedTime * 2 * Math.PI) * amplitude); //oscilacna formula

        this.turretHandler.draw(g, super.getX() + 12, super.getY() - 10 + offsetY);

        g2d.setColor(new Color(173, 216, 230, 100));

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        int radius = super.getRange();
        int diameter = radius * 2;
        g2d.fillOval(super.getX() - radius + 23, super.getY() - radius + 54, diameter, diameter);

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }

    /**
     * Aktualizuje stav veže a kontroluje nepriateľov v dosahu.
     *
     * @param npcs zoznam nepriateľov
     */
    @Override
    public void update(ArrayList<Npc> npcs) {
        int offset = 41;
        double currentTime = System.currentTimeMillis() / 1000.0;
        this.npcsInRange.clear();
        for (Npc npc : npcs) {
            if (npc.getHp() > 0) {
                double distance = Math.sqrt(Math.pow(npc.getX() - this.getX(), 2) + Math.pow((npc.getY() - offset) - this.getY(), 2));
                if (distance <= super.getRange()) {
                    this.npcsInRange.add(npc);
                }
            }
        }

        if (currentTime - this.lastShotTime >= 1.0 / super.getRateOfFire()) {
            this.shoot();
            this.lastShotTime = currentTime;
        }
    }

    /**
     * Vráti nepriateľa v dosahu veže. (Metóda zatiaľ neimplementovaná)
     *
     * @param npcs zoznam nepriateľov
     * @return nepriateľský NPC
     */
    @Override
    public Npc npcInRange(ArrayList<Npc> npcs) {
        return new HardenedNPC();
    }
}
