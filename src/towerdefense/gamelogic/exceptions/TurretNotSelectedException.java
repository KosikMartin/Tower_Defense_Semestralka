package towerdefense.gamelogic.exceptions;

/**
 * Výnimka pre nevybranú vežu.
 *
 * Autor: Martin Košík
 * Dátum: 19.05.2024
 */
public class TurretNotSelectedException extends Exception {

    /**
     * Konštruktor výnimky TurretNotSelectedException.
     *
     * @param message správa výnimky
     */
    public TurretNotSelectedException(String message) {
        super(message);
    }

    /**
     * Vráti textovú reprezentáciu výnimky.
     *
     * @return textová reprezentácia výnimky
     */
    @Override
    public String toString() {
        return "TurretNotSelectedException: " + super.getMessage();
    }
}
