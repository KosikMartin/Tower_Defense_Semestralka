package towerdefense.abilities;

import towerdefense.gamelogic.exceptions.NotEnoughGoldException;
import towerdefense.gamelogic.GameEngine;
import towerdefense.npc.Npc;

import javax.swing.ImageIcon;

import java.util.ArrayList;

public class BOMB implements Ability {
    private ImageIcon icon = new ImageIcon("src/TowerDefense/Resources/Images/bombbutton.png");
    private long lastUsed = 0;
    private int x;
    private int y;
    private boolean active = false;

    @Override
    public boolean execute(GameEngine gameEngine, int x, int y) {
        if (gameEngine.getPlayer().getMoney() < this.getPrice()) {
            gameEngine.getSoundPlayer().play("src/TowerDefense/Resources/Sounds/NoMoney.wav");
            try {
                throw new NotEnoughGoldException("Not enough gold!!!");
            } catch (NotEnoughGoldException e) {
                throw new RuntimeException(e);
            }
        }
        this.active = true;
        this.x = x;
        this.y = y;

        ArrayList<Npc> npcs = gameEngine.getNpcs();
        gameEngine.getSoundPlayer().play("src/TowerDefense/Resources/Sounds/Explosion.wav");
        gameEngine.getPlayer().subtractMoney(this.getPrice());
        npcs.forEach(npc -> {
            if (Math.hypot(npc.getX() - this.x, npc.getY() - this.y) < 125) { // Bomb radius 100
                npc.takeDamage(100);
            }
        });

        System.out.println("Bomb activated at: (" + x + ", " + y + ")");
        this.lastUsed = System.currentTimeMillis();

        gameEngine.getMapPanel().getPanel().repaint();
        return true;
    }

    @Override
    public int getPrice() {
        return 150;
    }
}
