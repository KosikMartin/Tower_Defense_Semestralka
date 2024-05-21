package towerdefense.gamelogic;

import towerdefense.abilities.Ability;
import towerdefense.defendingobjects.DefenseObject;
import towerdefense.defendingobjects.AreaTurret;
import towerdefense.defendingobjects.MagicTurret;
import towerdefense.defendingobjects.ArrowTurret;
import towerdefense.gui.MapPanel;
import towerdefense.gamelogic.exceptions.NotEnoughGoldException;
import towerdefense.gamelogic.exceptions.TurretNotSelectedException;
import towerdefense.gamelogic.exceptions.UnknownTurretTypeException;
import towerdefense.npc.Npc;

import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.awt.Color;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Herný engine pre Tower Defense hru.
 *
 * Autor: Martin Košík
 * Dátum: 19.05.2024
 */
public class GameEngine implements Runnable {
    private MapPanel mapPanel;
    private Ability selectedAbility;
    private boolean isUsingAbility = false;
    private volatile boolean running = true;
    private final int fps = 60;
    private boolean playerWon = false;
    private final int frameTime = 1000 / this.fps;
    private ArrayList<DefenseObject> turrets = new ArrayList<>();
    private ArrayList<Npc> npcs = new ArrayList<>();
    private String selectedTurretType = null;
    private boolean isPlacing = false;
    private Player player;
    private CollisionDetector collisionDetector;
    private WaveManager waveManager;
    private SoundPlayer soundPlayer;
    private int level = 0;
    private boolean gameOver = false;
    private boolean gameWon = false;
    private int totalGoldEarned = 0;
    private int totalGoldSpent = 0;

    /**
     * Konštruktor triedy GameEngine.
     *
     * @param lvl úroveň hry
     */
    public GameEngine(int lvl) {
        this.level = lvl;
        this.player = new Player();
        this.soundPlayer = new SoundPlayer();
        this.waveManager = new WaveManager(this);
        this.waveManager.scheduleNextWave(5000);
    }

    /**
     * Vyberie schopnosť.
     *
     * @param ability vybraná schopnosť
     */
    public void selectAbility(Ability ability) {
        this.selectedAbility = ability;
        this.isUsingAbility = true;
        System.out.println("Selected ability: " + ability.getClass().getSimpleName());
    }

    /**
     * Vráti objekt SoundPlayer.
     *
     * @return objekt SoundPlayer
     */
    public SoundPlayer getSoundPlayer() {
        return this.soundPlayer;
    }

    /**
     * Vráti vybranú schopnosť.
     *
     * @return vybraná schopnosť
     */
    public Ability getSelectedAbility() {
        return this.selectedAbility;
    }

    /**
     * Vráti objekt MapPanel.
     *
     * @return objekt MapPanel
     */
    public MapPanel getMapPanel() {
        return this.mapPanel;
    }

    /**
     * Použije schopnosť na daných súradniciach.
     *
     * @param x súradnica X
     * @param y súradnica Y
     * @return true, ak bola schopnosť úspešne použitá, inak false
     */
    public boolean useAbility(int x, int y) {
        if (this.selectedAbility == null) {
            System.out.println("No ability selected.");
            return false;
        }
        if (this.player.getMoney() >= this.selectedAbility.getPrice()) {
            this.totalGoldSpent += this.selectedAbility.getPrice();
            System.out.println("Ability Gold added: " + this.selectedAbility.getPrice());
            boolean success = this.selectedAbility.execute(this, x, y);
            if (success) {
                this.isUsingAbility = false;
                this.selectedAbility = null;
            }
            return success;
        } else {
            return false;
        }
    }

    /**
     * Skontroluje, či sa používa schopnosť.
     *
     * @return true, ak sa používa schopnosť, inak false
     */
    public boolean isUsingAbility() {
        return this.isUsingAbility;
    }

    /**
     * Vráti objekt Player.
     *
     * @return objekt Player
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Vráti úroveň hry.
     *
     * @return úroveň hry
     */
    public int getLevel() {
        System.out.println("GameEngine level: " + this.level);
        return this.level;
    }

    /**
     * Nastaví režim umiestňovania veže.
     *
     * @param wantsToPlace true, ak sa má umiestniť veža, inak false
     */
    public void setPlacingMode(boolean wantsToPlace) {
        this.isPlacing = wantsToPlace;
    }

    /**
     * Skontroluje, či je vo fáze umiestňovania.
     *
     * @return true, ak je vo fáze umiestňovania, inak false
     */
    public boolean isPlacing() {
        return this.isPlacing;
    }

    /**
     * Vráti zoznam veží.
     *
     * @return zoznam veží
     */
    public ArrayList<DefenseObject> getTurrets() {
        return new ArrayList<>(this.turrets);
    }

    /**
     * Vráti typ veže podľa zadaného názvu.
     *
     * @param type typ veže
     * @return objekt DefenseObject
     * @throws UnknownTurretTypeException neznámy typ veže
     * @throws TurretNotSelectedException veža nebola vybraná
     */
    private DefenseObject returnTurret(String type) throws UnknownTurretTypeException, TurretNotSelectedException {
        this.isPlacing = true;

        if (this.selectedTurretType == null) {
            throw new TurretNotSelectedException("No turret type selected.");
        }

        return switch (type) {
            case "AoETurret" -> new AreaTurret();
            case "MagicTurret" -> new MagicTurret();
            case "ArrowTurret" -> new ArrowTurret();
            default -> throw new UnknownTurretTypeException("Unknown turret type: " + type);
        };
    }

    /**
     * Umiestni vežu na daných súradniciach.
     *
     * @param x súradnica X
     * @param y súradnica Y
     * @return true, ak bola veža úspešne umiestnená, inak false
     * @throws NotEnoughGoldException nedostatok zlata
     * @throws TurretNotSelectedException veža nebola vybraná
     */
    public boolean placeTurret(int x, int y) throws NotEnoughGoldException, TurretNotSelectedException {
        if (this.selectedTurretType == null) {
            throw new TurretNotSelectedException("No turret type selected.");
        }

        DefenseObject turret = null;
        try {
            turret = this.returnTurret(this.selectedTurretType);
        } catch (UnknownTurretTypeException e) {
            throw new RuntimeException(e);
        }

        int xImg = turret.getBase().getImageX() / 2;
        int yImg = turret.getBase().getImageY() / 2;
        int calcX = x - xImg;
        int calcY = y - yImg;

        if (this.collisionDetector.isValidPlacement(x, y)) {
            if (this.player.getMoney() >= turret.getPrice()) {
                turret.setPosition(calcX, calcY);
                this.player.subtractMoney(turret.getPrice());
                this.totalGoldSpent += turret.getPrice();
                this.turrets.add(turret);
                this.mapPanel.getPanel().repaint(); // Update the panel to show the new turret
                return true;
            } else {
                this.soundPlayer.play("src/TowerDefense/Resources/Sounds/NoMoney.wav");
                throw new NotEnoughGoldException("Player does not have enough coins to place the turret.");
            }
        } else {
            System.out.println("Invalid placement position.");
            return false;
        }
    }

    /**
     * Pridá nepriateľa do zoznamu.
     *
     * @param npc nepriateľ
     */
    public void addNpc(Npc npc) {
        synchronized (this.npcs) {
            this.npcs.add(npc);
        }
    }

    /**
     * Nastaví objekt MapPanel.
     *
     * @param mapPanel objekt MapPanel
     */
    public void setMapPanel(MapPanel mapPanel) {
        this.mapPanel = mapPanel;
        this.collisionDetector = new CollisionDetector(this.mapPanel.getMapLoader().getTiles());
    }

    /**
     * Vráti zoznam nepriateľov.
     *
     * @return zoznam nepriateľov
     */
    public ArrayList<Npc> getNpcs() {
        var copy = new ArrayList<>(this.npcs);
        var a = Collections.unmodifiableList(this.npcs);
        return copy;
    }

    /**
     * Vyberie typ veže.
     *
     * @param turretType typ veže
     */
    public void selectTurret(String turretType) {
        this.isPlacing = true;
        this.selectedTurretType = turretType;
        System.out.println("Placing a " + turretType);
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        final double ns = 1000000000.0 / this.fps;
        double delta = 0;

        while (this.running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            while (delta >= 1) {
                this.update();
                delta--;
            }
            this.mapPanel.getPanel().repaint(); // Rendering

            try {
                Thread.sleep(this.frameTime); // Control FPS
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Aktualizuje stav hry.
     */
    private void update() {
        if (this.gameOver) {
            return;
        }

        List<Npc> npcCopy;
        synchronized (this.npcs) {
            npcCopy = new ArrayList<>(this.npcs);
        }

        for (Npc npc : npcCopy) {
            npc.move(this.collisionDetector);
            if (npc.getHp() <= 0) {
                this.player.addMoney(npc.getReward());
                this.totalGoldEarned += npc.getReward();
                System.out.println("GOLD EARNED: " + this.totalGoldEarned);
                synchronized (this.npcs) {
                    this.npcs.remove(npc);
                }
            } else if (npc.hasReachedDestination()) {
                this.player.subtractHealth(1);
                synchronized (this.npcs) {
                    this.npcs.remove(npc);
                }
            }
        }

        Iterator<DefenseObject> iterator = this.turrets.iterator();
        while (iterator.hasNext()) {
            DefenseObject turret = iterator.next();
            turret.update(this.npcs);
        }

        if (this.npcs.isEmpty() && !this.waveManager.getNpcs().isEmpty()) {
            this.waveManager.scheduleNextWave(5000);
        }

        if (this.npcs.isEmpty() && this.waveManager.areAllWavesCompleted()) {
            this.gameWon = true;
            this.gameOver = true;
            this.playerWon = true;
            this.stop();
            this.showGameState();
        }

        if (this.player.getHealth() <= 0) {
            this.gameOver = true;
            this.playerWon = false;
            this.stop();
            this.showGameState();
        }
    }

    /**
     * Zobrazí stav hry po skončení.
     */
    private void showGameState() {
        if (this.gameOver) {
            Graphics g = this.mapPanel.getPanel().getGraphics();
            if (g != null) {
                g.setColor(this.playerWon ? Color.GREEN : Color.RED);
                g.setFont(new Font("Arial", Font.BOLD, 50));
                FontMetrics fm = g.getFontMetrics();
                String message = this.playerWon ? "YOU WIN!!!" : "GAME OVER";
                int x = (this.mapPanel.getPanel().getWidth() - fm.stringWidth(message)) / 2;
                int y = (this.mapPanel.getPanel().getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g.drawString(message, x, y);
            }

            String dialogMessage = this.playerWon ? "YOU WIN!!! Do you want to save your score?" : "You lost! Do you want to save your score?";
            int response = JOptionPane.showConfirmDialog(null, dialogMessage, "Game Over", JOptionPane.YES_NO_OPTION);

            if (response == JOptionPane.YES_OPTION) {
                String playerName = JOptionPane.showInputDialog("What do you want to be known as?");
                if (playerName != null && !playerName.trim().isEmpty()) {
                    this.saveScore(playerName, this.playerWon);
                }
                this.npcs.clear();
                this.turrets.clear();
            }

            System.exit(0);
        }
    }

    /**
     * Uloží skóre hráča.
     *
     * @param playerName meno hráča
     * @param isWinner true, ak hráč vyhral, inak false
     */
    private void saveScore(String playerName, boolean isWinner) {
        int wave = this.waveManager.getWaveNumber();
        String winTag = isWinner ? " [WINNER]" : "";

        String scoreEntry = String.format(
                "Player \"%s\"%s - Wave %d - Level: %d%nTotal Gold Earned: %d%nTotal Gold Spent: %d%n--------------------------------%n",
                playerName, winTag, wave, this.level, this.totalGoldEarned, this.totalGoldSpent);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("leaderboard.txt", true))) {
            writer.write(scoreEntry);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (this.gameWon) {
            System.exit(0);
        }
    }

    /**
     * Zastaví herný engine.
     */
    public void stop() {
        this.running = false;
    }
}
