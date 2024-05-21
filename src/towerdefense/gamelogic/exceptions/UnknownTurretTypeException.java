package towerdefense.gamelogic.exceptions;

/**
 * Výnimka pre neznámy typ veže.
 *
 * Autor: Martin Košík
 * Dátum: 19.05.2024
 */
public class UnknownTurretTypeException extends Exception {

    /**
     * Konštruktor výnimky UnknownTurretTypeException.
     *
     * @param message správa výnimky
     */
    public UnknownTurretTypeException(String message) {
        super(message);
    }

    /**
     * Vráti textovú reprezentáciu výnimky.
     *
     * @return textová reprezentácia výnimky
     */
    @Override
    public String toString() {
        return "UnknownTurretTypeException: " + super.getMessage();
    }
}
