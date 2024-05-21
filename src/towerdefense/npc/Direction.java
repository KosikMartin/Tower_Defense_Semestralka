package towerdefense.npc;

/**
 * Enum pre smerovanie pohybu nepriateľov v hre Tower Defense.
 * Používam číselné označenie smerov.
 * Čo mi zaručí kúsok ľahšiu prácu.
 * Autor: Martin Košík
 * Dátum: 19.05.2024
 */
public enum Direction {
    NORTH(0),
    EAST(1),
    SOUTH(2),
    WEST(3);

    private final int representation;

    /**
     * Konštruktor pre Direction.
     *
     * @param representation numerická reprezentácia smeru
     */
    Direction(int representation) {
        this.representation = representation;
    }

    /**
     * Vráti numerickú reprezentáciu smeru.
     *
     * @return numerická reprezentácia smeru
     */
    public int getRepresentation() {
        return this.representation;
    }
}
