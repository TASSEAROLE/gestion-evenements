package fr.gestionevenements.modele;

/**
 * Exception personnalisée lancée lorsqu'on tente d'ajouter un événement qui existe déjà.
 */
public class EvenementDejaExistantException extends RuntimeException {
    /**
     * Constructeur avec message d'erreur
     * @param message Le message d'erreur
     */
    public EvenementDejaExistantException(String message) {
        super(message);
    }
}