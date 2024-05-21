package towerdefense.gamelogic;

import towerdefense.gui.Drawable;

import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.Color;

/**
 * Trieda pre hráča v hre Tower Defense.
 * Jednoduchá implementácia, ide iba o základné hodnoty a vykreslenie stavu.
 * Autor: Martin Košík
 * Dátum: 19.05.2024
 */
public class Player implements Drawable {

    private int money;
    private int score;
    private int health;

    /**
     * Konštruktor pre triedu Player.
     */
    public Player() {
        this.money = 600;
        this.score = 0;
        this.health = 20;
    }

    @Override
    public void draw(Graphics2D g2d) {
        if (this.health < 0) {
            String gameover = "GAME OVER";
            g2d.setColor(Color.RED);
            Font font = new Font("Arial", Font.BOLD, 72);
            g2d.setFont(font);
            g2d.drawString(gameover, 400 - g2d.getFontMetrics().stringWidth(gameover) / 2, 300);
        }
        String goldText = "Gold: " + this.money;
        g2d.setColor(Color.YELLOW);
        Font font = new Font("Arial", Font.BOLD, 24);
        g2d.setFont(font);
        g2d.drawString(goldText, 10, 30);

        // Vykreslí zdravotný panel
        String healthText = "HP: " + this.health;
        g2d.setColor(Color.RED);
        g2d.drawString(healthText, 300 - g2d.getFontMetrics().stringWidth(healthText) - 10, 30);
    }

    /**
     * Odoberie peniaze hráčovi.
     *
     * @param amount množstvo
     */
    public void subtractMoney(int amount) {
        this.money -= amount;
    }

    /**
     * Pridá peniaze hráčovi.
     *
     * @param amount množstvo
     */
    public void addMoney(int amount) {
        this.money += amount;
    }

    /**
     * Vráti zdravie hráča.
     *
     * @return zdravie
     */
    public int getHealth() {
        return this.health;
    }

    /**
     * Nastaví peniaze hráčovi na predvolenú hodnotu.
     */
    public void defaultMoney() {
        this.money = 800;
    }

    /**
     * Nastaví zdravie hráčovi na predvolenú hodnotu.
     */
    public void defaultHealth() {
        this.health = 20;
    }

    /**
     * Odoberie zdravie hráčovi.
     *
     * @param amount množstvo
     */
    public void subtractHealth(int amount) {
        this.health -= amount;
    }

    /**
     * Vráti peniaze hráča.
     *
     * @return peniaze
     */
    public int getMoney() {
        return this.money;
    }
}
