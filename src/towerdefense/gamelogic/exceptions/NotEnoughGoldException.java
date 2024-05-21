package towerdefense.gamelogic.exceptions;

/**
 * Výnimka pre nedostatok zlata.
 *
 * Autor: Martin Košík
 * Dátum: 19.05.2024
 */
public class NotEnoughGoldException extends Exception {

    /**
     * Konštruktor výnimky NotEnoughGoldException.
     *
     * @param message správa výnimky
     */
    public NotEnoughGoldException(String message) {
        super(message);
    }

    /**
     * Vráti textovú reprezentáciu výnimky.
     *
     * @return textová reprezentácia výnimky
     */
    @Override
    public String toString() {
        return "NotEnoughGoldException: " + super.getMessage();
    }
}
