package towerdefense.gamelogic;

import towerdefense.npc.Direction;
import towerdefense.npc.HardenedNPC;
import towerdefense.npc.Npc;
import towerdefense.npc.StandardNPC;
import towerdefense.npc.FlyingNPC;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Random;

/**
 * Trieda pre správu vĺn nepriateľov v hre Tower Defense.
 *
 * Autor: Martin Košík
 * Dátum: 19.05.2024
 */
public class WaveManager {
    private ArrayList<Npc> npcs;
    private final GameEngine gameEngine;
    private int waveNumber;
    private final int maxWaves;
    private AtomicInteger npcsSpawned;
    private int numNpcs;
    private final int level;
    private final Random random;
    private boolean allWavesCompleted;
    private Queue<Long> spawnQueue;

    /**
     * Konštruktor triedy WaveManager.
     *
     * @param gameEngine herný engine
     */
    public WaveManager(GameEngine gameEngine) {
        this.random = new Random();
        this.npcs = new ArrayList<>();
        this.level = gameEngine.getLevel();
        this.gameEngine = gameEngine;
        System.out.println("Wave Manager Level: " + this.level);
        this.maxWaves = this.level + this.random.nextInt(this.level + 1);
        this.waveNumber = 0;
        this.npcsSpawned = new AtomicInteger(0);
        this.numNpcs = 0;
        this.allWavesCompleted = false;
        this.spawnQueue = new LinkedList<>();
    }

    /**
     * Začne ďalšiu vlnu nepriateľov.
     */
    public void startNextWave() {
        if (this.allWavesCompleted) {
            return;
        }

        if (this.waveNumber >= this.maxWaves) {
            System.out.println("All waves completed!");
            this.allWavesCompleted = true;
            return;
        }

        this.waveNumber++;
        System.out.println("Starting wave " + this.waveNumber);

        this.numNpcs = this.waveNumber * 3;
        this.npcsSpawned.set(0);

        // 1 sekunda medzi nepriateľmi
        for (int i = 0; i < this.numNpcs; i++) {
            this.spawnQueue.offer(System.currentTimeMillis() + (i * 1000));
        }

        this.spawnNextNpcFromQueue();
    }

    /**
     * Spustí vytváranie ďalšieho nepriateľa z fronty.
     */
    private void spawnNextNpcFromQueue() {
        if (this.allWavesCompleted || this.spawnQueue.isEmpty()) {
            return;
        }

        long currentTime = System.currentTimeMillis();
        long spawnTime = this.spawnQueue.peek();

        if (currentTime >= spawnTime) {
            this.spawnQueue.poll(); // Odstráni čas vytvorenia z fronty
            this.spawnNpc();

            if (!this.spawnQueue.isEmpty()) {
                this.spawnNextNpcFromQueue(); // Spustí vytváranie ďalšieho nepriateľa, ak je čas
            } else if (this.npcsSpawned.get() == this.numNpcs) {
                // Naplánuje ďalšiu vlnu po meškaní, keď sú všetci nepriatelia vo vlne vytvorení
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        WaveManager.this.startNextWave();
                    }
                }, 5000); // Meškanie pre ďalšiu vlnu v milisekundách
            }
        } else {
            // Naplánuje kontrolu fronty po potrebnom meškaní
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    WaveManager.this.spawnNextNpcFromQueue();
                }
            }, spawnTime - currentTime);
        }
    }

    /**
     * Vytvorí nepriateľa.
     */
    private synchronized void spawnNpc() {
        if (this.allWavesCompleted) {
            return;
        }

        int speed = 1;
        int startX = 0;
        int startY = 89;
        double hp = 100.0 + (this.waveNumber * 5) + (this.waveNumber * this.level) + this.level;
        Direction direction = Direction.EAST; // Začiatočný smer

        Npc npc = this.getRandomNpc(speed, startX, startY, direction, hp);
        this.npcs.add(npc);
        this.gameEngine.addNpc(npc); // Informácia o novom nepriateľovi
        System.out.println("Spawned NPC at (" + startX + ", " + startY + ") with HP: " + hp);

        this.npcsSpawned.incrementAndGet();
    }

    /**
     * Vráti náhodného nepriateľa na základe pravdepodobností.
     *
     * @param speed rýchlosť nepriateľa
     * @param startX počiatočná súradnica X
     * @param startY počiatočná súradnica Y
     * @param direction smer pohybu nepriateľa
     * @param hp zdravie nepriateľa
     * @return náhodný nepriateľ
     */
    private Npc getRandomNpc(int speed, int startX, int startY, Direction direction, double hp) {
        List<NpcProbability> npcProbabilities = new ArrayList<>();

        if (this.level >= 5) {
            npcProbabilities.add(new NpcProbability(new HardenedNPC(speed, startX, startY, direction, hp), 50));
            npcProbabilities.add(new NpcProbability(new FlyingNPC(speed, startX, startY, direction, hp), 35));
            npcProbabilities.add(new NpcProbability(new StandardNPC(speed, startX, startY, direction, hp), 15));
        } else {
            npcProbabilities.add(new NpcProbability(new HardenedNPC(speed, startX, startY, direction, hp), 10));
            npcProbabilities.add(new NpcProbability(new FlyingNPC(speed, startX, startY, direction, hp), 25));
            npcProbabilities.add(new NpcProbability(new StandardNPC(speed, startX, startY, direction, hp), 65));
        }

        int totalWeight = npcProbabilities.stream().mapToInt(NpcProbability::getWeight).sum();
        int randomWeight = this.random.nextInt(totalWeight);

        int currentWeight = 0;
        for (NpcProbability npcProbability : npcProbabilities) {
            currentWeight += npcProbability.getWeight();
            if (randomWeight < currentWeight) {
                return npcProbability.getNpc();
            }
        }

        return new StandardNPC(speed, startX, startY, direction, hp);
    }

    /**
     * Naplánuje ďalšiu vlnu po danom meškaní.
     *
     * @param delay meškanie v milisekundách
     */
    public void scheduleNextWave(long delay) {
        if (this.allWavesCompleted) {
            return;
        }

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                WaveManager.this.startNextWave();
            }
        }, delay);
    }

    /**
     * Skontroluje, či sú všetky vlny dokončené.
     *
     * @return true, ak sú všetky vlny dokončené, inak false
     */
    public boolean areAllWavesCompleted() {
        return this.allWavesCompleted;
    }

    /**
     * Vráti zoznam nepriateľov.
     *
     * @return zoznam nepriateľov
     */
    public ArrayList<Npc> getNpcs() {
        return new ArrayList<>(this.npcs);
    }

    /**
     * Vráti číslo aktuálnej vlny.
     *
     * @return číslo vlny
     */
    public int getWaveNumber() {
        return this.waveNumber;
    }
}
