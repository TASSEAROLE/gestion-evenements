package fr.gestionevenements.modele;

import java.util.UUID;

/**
 * Classe représentant un participant à un événement.
 * Implémente l'interface ParticipantObserver pour recevoir des notifications.
 */
public class Participant implements ParticipantObserver {
    protected String id;
    protected String nom;
    protected String email;
    
    /**
     * Constructeur d'un participant
     * @param nom Nom du participant
     * @param email Adresse email du participant
     */
    public Participant(String nom, String email) {
        this.id = UUID.randomUUID().toString();
        this.nom = nom;
        this.email = email;
    }
    
    /**
     * Méthode appelée lorsqu'un événement auquel le participant est inscrit est mis à jour
     * @param message Le message de notification
     */
    @Override
    public void mettreAJour(String message) {
        // Dans une implémentation réelle, on pourrait envoyer un email
        System.out.println("Notification à " + nom + " (" + email + "): " + message);
    }
    
    // Getters et Setters
    public String getId() {
        return id;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Participant that = (Participant) obj;
        return id.equals(that.id);
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }
}