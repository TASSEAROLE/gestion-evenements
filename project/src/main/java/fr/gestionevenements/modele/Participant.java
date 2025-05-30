package fr.gestionevenements.modele;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Participant implements ParticipantObserver {
    protected String id;
    @JsonProperty("nom")
    protected String nom;
    @JsonProperty("email")
    protected String email;

    public Participant(String nom, String email) {
        this.id = UUID.randomUUID().toString();
        this.nom = nom;
        this.email = email;
    }

    public Participant() {}

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