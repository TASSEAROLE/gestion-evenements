package fr.gestionevenements.service;

/**
 * Factory pour créer différents types de services de notification.
 * Implémente le pattern Factory.
 */
public class NotificationServiceFactory {
    /**
     * Crée un service de notification selon le type spécifié
     * @param type Le type de service de notification ("email" ou "sms")
     * @param expediteur L'expéditeur (email ou numéro de téléphone)
     * @return Le service de notification créé
     */
    public static NotificationService creerNotificationService(String type, String expediteur) {
        if (type.equalsIgnoreCase("email")) {
            return new EmailNotificationService(expediteur);
        } else if (type.equalsIgnoreCase("sms")) {
            return new SMSNotificationService(expediteur);
        } else {
            throw new IllegalArgumentException("Type de service de notification non pris en charge: " + type);
        }
    }
}