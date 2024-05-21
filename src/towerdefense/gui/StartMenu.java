package towerdefense.gui;

import towerdefense.gamelogic.SoundPlayer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import java.awt.GridBagLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class StartMenu {

    private JFrame frame;
    private JButton startButton = new JButton("Start");
    private JButton optionsButton = new JButton("Options");
    private JButton exitButton = new JButton("Exit");
    private SoundPlayer soundPlayer;
    private int level;

    public StartMenu() {
        this.soundPlayer = new SoundPlayer();
        this.frame = new JFrame("Kosik's Tower Defense Menu"); // Title moved here
        this.loadPanelSettings();
    }

    /**
     * Načíta nastavenia panela.
     */
    private void loadPanelSettings() {
        this.frame.setSize(1200, 720);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setLayout(new GridBagLayout());

        BackgroundPanel backgroundPanel = new BackgroundPanel("/towerdefense/Resources/images/Background.png");
        this.frame.setContentPane(backgroundPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0); // top, left, bottom, right margins
        this.configureButton(this.startButton, "/towerdefense/Resources/images/Play_Button.png", "/towerdefense/Resources/images/PlayButton_Highlight.png");
        this.configureButton(this.optionsButton, "/towerdefense/Resources/images/OptionsButton.png", "/towerdefense/Resources/images/OptionsButton_HighLight.png");
        this.configureButton(this.exitButton, "/towerdefense/Resources/images/Exit_Button.png", "/towerdefense/Resources/images/ExitButton_HighLight.png");

        // Add the buttons to the frame
        this.frame.add(this.wrapButton(this.startButton), gbc);
        this.frame.add(this.wrapButton(this.optionsButton), gbc);
        this.frame.add(this.wrapButton(this.exitButton), gbc);


        StartMenu.this.addActionButtonListeners();

        this.frame.setVisible(true);
    }

    private void addActionButtonListeners() {
        this.startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the action for start button
                StartMenu.this.startGame();
            }
        });

        this.optionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the action for options button
                StartMenu.this.openLevelSelectionDialog();
            }
        });

        this.exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the action for exit button
                System.exit(0);
            }
        });
    }

    private void startGame() {
        if (this.level == 0) {
            JOptionPane.showMessageDialog(this.frame, "Please select a difficulty level in Options.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        System.out.println("Main Panel: " + this.level);
        this.frame.setVisible(false);
        this.soundPlayer.play("src/towerdefense/Resources/Sounds/Play.wav");
        GamePanel panel = new GamePanel(this.level); // Assuming you manage this panel display
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
        button.setPreferredSize(new Dimension(210, 70));
        panel.add(button);
        return panel;
    }



    /**
     * Otvorí dialóg pre výber úrovne obtiažnosti.
     */
    private void openLevelSelectionDialog() {
        JDialog dialog = new JDialog(this.frame, "Choose Difficulty Level", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();

        panel.setLayout(new GridBagLayout());
        panel.setPreferredSize(new Dimension(300, 150));

        JLabel label = new JLabel("Enter level (1-10):");
        JTextField textField = new JTextField(10);
        JButton submitButton = new JButton("Submit");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(label, gbc);

        gbc.gridx = 1;
        panel.add(textField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(submitButton, gbc);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int lev = Integer.parseInt(textField.getText());
                    if (lev < 1 || lev > 10) {
                        JOptionPane.showMessageDialog(dialog, "Please enter a level between 1 and 10.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Level " + lev + " selected.", "Level Selected", JOptionPane.INFORMATION_MESSAGE);
                        StartMenu.this.level = lev;
                        dialog.dispose();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Please enter a valid number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        dialog.add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this.frame);
        dialog.setVisible(true);
    }

    /**
     * Konfiguruje tlačidlo s ikonami pre normálny a hover stav.
     *
     * @param button tlačidlo
     * @param normalIconPath cesta k normálnej ikone
     * @param hoverIconPath cesta k hover ikone
     */
    private void configureButton(JButton button, String normalIconPath, String hoverIconPath) {
        ImageIcon normalIcon = new ImageIcon(getClass().getResource(normalIconPath));
        ImageIcon hoverOverIcon = new ImageIcon(getClass().getResource(hoverIconPath));

        button.setIcon(normalIcon);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setRolloverIcon(hoverOverIcon);
        button.setPreferredSize(new Dimension(200, 70));

        button.setContentAreaFilled(false);
        button.setOpaque(false);
    }
}
