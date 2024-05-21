package towerdefense.gamelogic;

/**
 * Trieda na reprezentáciu pravdepodobnosti výskytu NPC v hre Tower Defense.
 *
 * Autor: Martin Košík
 * Dátum: 19.05.2024
 */
import towerdefense.npc.Npc;

public class NpcProbability {
    private Npc npc;
    private int weight;

    /**
     * Konštruktor triedy NpcProbability.
     *
     * @param npc NPC postava
     * @param weight váha pravdepodobnosti
     */
    public NpcProbability(Npc npc, int weight) {
        this.npc = npc;
        this.weight = weight;
    }

    /**
     * Vráti NPC postavu.
     *
     * @return NPC postava
     */
    public Npc getNpc() {
        return this.npc;
    }

    /**
     * Vráti váhu pravdepodobnosti.
     *
     * @return váha pravdepodobnosti
     */
    public int getWeight() {
        return this.weight;
    }
}
