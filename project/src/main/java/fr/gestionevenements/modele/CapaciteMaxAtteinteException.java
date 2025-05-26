package fr.gestionevenements.modele;

/**
 * Exception personnalisée lancée lorsque la capacité maximale d'un événement est atteinte.
 */
public class CapaciteMaxAtteinteException extends RuntimeException {
    /**
     * Constructeur avec message d'erreur
     * @param message Le message d'erreur
     */
    public CapaciteMaxAtteinteException(String message) {
        super(message);
    }
}