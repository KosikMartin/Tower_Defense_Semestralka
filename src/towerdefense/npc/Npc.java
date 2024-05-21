package towerdefense.npc;

import towerdefense.gamelogic.CollisionDetector;
import towerdefense.gamelogic.SoundPlayer;
import towerdefense.gui.Drawable;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Abstraktná trieda pre nepriateľov v hre Tower Defense.
 *
 * Autor: Martin Košík
 * Dátum: 19.05.2024
 */
public abstract class Npc implements Drawable {

    private int speed;
    private int x;
    private int y;
    private Direction direction;
    private double hp;
    private double maxHP;
    private Color color;
    private SoundPlayer soundPlayer;
    private boolean reachedDestination = false;

    /**
     * Konštruktor pre triedu Npc.
     *
     * @param speed rýchlosť
     * @param x počiatočná súradnica X
     * @param y počiatočná súradnica Y
     * @param direction smer pohybu
     * @param hp zdravie
     * @param color farba
     */
    public Npc(int speed, int x, int y, Direction direction, double hp, Color color) {
        this.speed = speed;
        this.x = x;
        this.color = color;
        this.soundPlayer = new SoundPlayer();
        this.y = y;
        this.direction = direction;
        this.hp = hp;
        this.maxHP = hp;
    }

    /**
     * Prázdny konštruktor pre triedu Npc.
     */
    public Npc() {

    }

    /**
     * Kontroluje, či NPC dosiahlo cieľ.
     *
     * @return true, ak NPC dosiahlo cieľ, inak false
     */
    public boolean hasReachedDestination() {
        return this.reachedDestination;
    }

    /**
     * Vráti objekt SoundPlayer.
     *
     * @return objekt SoundPlayer
     */
    public SoundPlayer getSoundPlayer() {
        return this.soundPlayer;
    }

    @Override
    public void draw(Graphics2D g2d) {
        // Vykreslí NPC
        g2d.setColor(this.color);
        g2d.fillOval(this.x, this.y, 30, 30);

        // Vykreslí zdravotný panel nad NPC
        int healthBarWidth = 30;
        int healthBarHeight = 5;
        int healthBarX = this.x;
        int healthBarY = this.y - 20;

        double hpRatio = this.hp / this.maxHP;
        int currentHealthBarWidth = (int)(healthBarWidth * hpRatio);

        g2d.setColor(Color.RED);
        g2d.fillRect(healthBarX, healthBarY, healthBarWidth, healthBarHeight);

        g2d.setColor(Color.GREEN);
        g2d.fillRect(healthBarX, healthBarY, currentHealthBarWidth, healthBarHeight);

        g2d.setColor(Color.BLACK);
        g2d.drawRect(healthBarX, healthBarY, healthBarWidth, healthBarHeight);
    }

    /**
     * Skontroluje, či je NPC v určitej oblasti.
     *
     * @param centerX súradnica X stredu oblasti
     * @param centerY súradnica Y stredu oblasti
     * @param radius polomer oblasti
     * @return true, ak je NPC v oblasti, inak false
     */
    public boolean isInArea(int centerX, int centerY, int radius) {
        double distance = Math.sqrt(Math.pow(centerX - this.x, 2) + Math.pow(centerY - this.y, 2));
        return distance <= radius;
    }

    /**
     * Nastaví farbu NPC.
     *
     * @param color farba
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Vráti odmenu za zabitie NPC.
     *
     * @return odmena za zabitie NPC
     */
    public abstract int getReward();

    /**
     * Pohyb NPC podľa detektoru kolízií.
     *
     * @param collisionDetector detektor kolízií
     */
    public void move(CollisionDetector collisionDetector) {
        if (!collisionDetector.isNextTilePath(this.x, this.y, this.direction.getRepresentation())) {
            System.out.println("Blocked or need to change direction");
            this.direction = this.calculateNewDirection(collisionDetector);
        }

        switch (this.direction) {
            case NORTH:
                this.y -= this.speed;
                break;
            case EAST:
                this.x += this.speed;
                break;
            case SOUTH:
                this.y += this.speed;
                break;
            case WEST:
                this.x -= this.speed;
                break;
        }
        if (collisionDetector.isAtTileId(this.x, this.y, "33")) {
            this.reachedDestination = true;
        }
    }

    /**
     * Vypočíta nový smer pohybu NPC podľa detektoru kolízií.
     *
     * @param collisionDetector detektor kolízií
     * @return nový smer pohybu
     */
    public Direction calculateNewDirection(CollisionDetector collisionDetector) {
        int[] dx = {0, 5, 0, -5}; // East, West
        int[] dy = {-5, 0, 5, 0}; // North, South

        int leftDirection = (this.direction.getRepresentation() + 3) % 4;  // 90 stupňov doľava
        int rightDirection = (this.direction.getRepresentation() + 1) % 4; // 90 stupňov doprava
        int forwardDirection = this.direction.getRepresentation();         // Priamy smer

        int[] directionsToCheck = {leftDirection, forwardDirection, rightDirection};

        for (int dir : directionsToCheck) {
            int newX = this.x + dx[dir];
            int newY = this.y + dy[dir];

            if (collisionDetector.isNextTilePath(newX, newY, dir)) {
                return Direction.values()[dir];  // Vráti prvý platný smer
            }
        }

        return this.direction;
    }

    /**
     * Vráti súradnicu X NPC.
     *
     * @return súradnica X
     */
    public int getX() {
        return this.x;
    }

    /**
     * Vráti súradnicu Y NPC.
     *
     * @return súradnica Y
     */
    public int getY() {
        return this.y;
    }

    /**
     * Vráti aktuálne zdravie NPC.
     *
     * @return zdravie NPC
     */
    public double getHp() {
        return this.hp;
    }

    /**
     * Odoberie zdravie NPC podľa prijatej škody.
     *
     * @param damage škoda
     */
    public void takeDamage(double damage) {
        this.hp -= damage;
        if (this.hp <= 0) {
            this.onDeath();
        }
    }

    /**
     * Vypočíta škodu spôsobenú NPC.
     *
     * @param damage škoda
     */
    public abstract void calculateDmg(double damage);

    /**
     * Akcia vykonaná po smrti NPC.
     */
    public abstract void onDeath();
}
