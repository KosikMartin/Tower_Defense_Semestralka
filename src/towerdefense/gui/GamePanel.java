package towerdefense.gui;

import towerdefense.abilities.BOMB;
import towerdefense.abilities.SPEEDUP;
import towerdefense.gamelogic.GameEngine;

import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 * Trieda pre vykresľovanie herného panelu v hre Tower Defense.
 *
 * Autor: Martin Košík
 * Dátum: 19.05.2024
 */
public class GamePanel {
    private JButton turretButton1;
    private JButton turretButton2;
    private JButton turretButton3;
    private JButton bombButton;
    private JButton speedUpButton;
    private MapPanel mapPanel;
    private JPanel buttonPanel;
    private JPanel mainPanel;
    private GameEngine gameEngine;
    private JFrame frame;

    /**
     * Konštruktor triedy GamePanel.
     *
     * @param level úroveň hry
     */
    public GamePanel(int level) {
        this.frame = new JFrame();
        this.mapPanel = new MapPanel("src/towerdefense/resources/mapdata/Map1.txt",
                "src/towerdefense/resources/mapdata/Map1Collision.txt");
        this.gameEngine = new GameEngine(level);
        this.mapPanel.setGameEngine(this.gameEngine);
        this.initializeComponents();
        this.configureAndWrapButtons();
        this.setLayouts();
        this.addComponentsToPanel();
        this.finalizeFrame();
        this.gameEngine.setMapPanel(this.mapPanel);
        System.out.println("Game Engine level in Game panel " + this.gameEngine.getLevel());
        new Thread(this.gameEngine).start();
        System.out.println("x, y mappanel");
        System.out.println(this.mapPanel.getPanel().getWidth() + ", " + this.mapPanel.getPanel().getHeight());
    }

    /**
     * Inicializuje komponenty herného panelu.
     */
    private void initializeComponents() {
        this.speedUpButton = new JButton("Speed Up");
        this.bombButton = new JButton("Bomb");
        this.turretButton1 = new JButton("Turret 1");
        this.turretButton2 = new JButton("Turret 2");
        this.turretButton3 = new JButton("Turret 3");
        this.buttonPanel = new JPanel();
        this.mainPanel = new JPanel();
    }

    /**
     * Nastaví rozloženie panelov.
     */
    private void setLayouts() {
        this.mainPanel.setLayout(new BorderLayout());
        this.buttonPanel.setLayout(new GridBagLayout());
    }

    /**
     * Pridá komponenty do panelu.
     */
    private void addComponentsToPanel() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);

        this.buttonPanel.add(this.wrapButton(this.turretButton1), gbc);
        this.buttonPanel.add(this.wrapButton(this.turretButton2), gbc);
        this.buttonPanel.add(this.wrapButton(this.turretButton3), gbc);
        this.buttonPanel.add(this.wrapButton(this.speedUpButton), gbc);
        this.buttonPanel.add(this.wrapButton(this.bombButton), gbc);

        this.mainPanel.add(this.mapPanel.getPanel(), BorderLayout.CENTER);
        this.mainPanel.add(this.buttonPanel, BorderLayout.WEST);

        this.frame.add(this.mainPanel);
    }

    /**
     * Dokončí konfiguráciu okna hry.
     */
    private void finalizeFrame() {
        this.frame.setTitle("Kosiks Tower Defense Game");
        this.frame.setSize(845, 738);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setPreferredSize(new Dimension(845, 738));
        this.frame.setVisible(true);
        this.frame.setResizable(false);
    }

    /**
     * Zabalí tlačidlo do panela.
     *
     * @param button tlačidlo
     * @return zabalené tlačidlo v paneli
     */
    private JPanel wrapButton(JButton button) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.setOpaque(false);
        button.setPreferredSize(new Dimension(70, 70));
        panel.add(button);
        return panel;
    }

    /**
     * Konfiguruje tlačidlo s ikonami.
     *
     * @param button tlačidlo
     * @param normalIconPath cesta k normálnej ikone
     */
    private void configureButton(JButton button, String normalIconPath) {
        ImageIcon normalIcon = new ImageIcon(getClass().getResource(normalIconPath));
        button.setIcon(normalIcon);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(70, 70));
        button.setContentAreaFilled(false);
        button.setOpaque(false);
    }

    /**
     * Konfiguruje tlačidlá a zabalí ich do panelov.
     */
    private void configureAndWrapButtons() {
        this.configureButton(this.speedUpButton, "/towerdefense/resources/images/speedbutton.png");
        this.configureButton(this.bombButton, "/towerdefense/resources/images/bombbutton.png");
        this.configureButton(this.turretButton1, "/towerdefense/resources/images/sampleTurret.png");
        this.configureButton(this.turretButton2, "/towerdefense/resources/images/sampleTurret.png");
        this.configureButton(this.turretButton3, "/towerdefense/resources/images/sampleTurret.png");

        this.bombButton.addActionListener(e -> this.gameEngine.selectAbility(new BOMB()));
        this.speedUpButton.addActionListener(e -> new SPEEDUP().execute(this.gameEngine, 0, 0));

        this.gameEngine.setPlacingMode(true);
        this.turretButton1.addActionListener(e -> this.gameEngine.selectTurret("ArrowTurret"));
        this.turretButton2.addActionListener(e -> this.gameEngine.selectTurret("MagicTurret"));
        this.turretButton3.addActionListener(e -> this.gameEngine.selectTurret("AoETurret"));
    }
}
