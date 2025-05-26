package fr.gestionevenements.service;

//Interface pour les services de notification.
public interface NotificationService {
    //Envoie une notification avec un message spécifique
    void envoyerNotification(String message);
}