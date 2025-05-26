package fr.gestionevenements.modele;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@JsonTypeInfo(use=JsonTypeInfo.Id.DEDUCTION)

@JsonSubTypes({
        @JsonSubTypes.Type(value= Conference.class),
        @JsonSubTypes.Type(value= Concert.class)
} )

public abstract class Evenement implements EvenementObservable {
    protected String id;
    protected String nom;
    protected LocalDateTime date;
    protected String lieu;
    protected int capaciteMax;
    protected List<Participant> participants;
    protected List<ParticipantObserver> observers;
    
    /**
     * Constructeur d'un événement
     * @param nom Nom de l'événement
     * @param date Date et heure de l'événement
     * @param lieu Lieu de l'événement
     * @param capaciteMax Capacité maximale de participants
     */
    public Evenement(String nom, LocalDateTime date, String lieu, int capaciteMax) {
        this.id = UUID.randomUUID().toString();
        this.nom = nom;
        this.date = date;
        this.lieu = lieu;
        this.capaciteMax = capaciteMax;
        this.participants = new ArrayList<>();
        this.observers = new ArrayList<>();
    }

    public Evenement() {


    }

    
    /**
     * Ajoute un participant à l'événement si la capacité maximale n'est pas atteinte
     * @param participant Le participant à ajouter
     * @throws CapaciteMaxAtteinteException Si la capacité maximale est atteinte
     */
    public void ajouterParticipant(Participant participant) throws CapaciteMaxAtteinteException {
        if (participants.size() >= capaciteMax) {
            throw new CapaciteMaxAtteinteException("La capacité maximale de l'événement est atteinte");
        }
        
        if (participants.contains(participant)) {
            throw new ParticipantDejaInscritException("Le participant est déjà inscrit à cet événement");
        }
        
        participants.add(participant);
        notifierObservateurs("Le participant " + participant.getNom() + " a été ajouté à l'événement " + this.nom);
    }
    
    /**
     * Annule l'événement et notifie tous les participants
     */
    public void annuler() {
        notifierObservateurs("L'événement " + this.nom + " a été annulé");
    }
    
    /**
     * Affiche les détails de l'événement
     * @return Une chaîne de caractères contenant les détails de l'événement
     */
    public String afficherDetails() {
        StringBuilder details = new StringBuilder();
        details.append("Événement: ").append(nom).append("\n");
        details.append("Date: ").append(date).append("\n");
        details.append("Lieu: ").append(lieu).append("\n");
        details.append("Capacité maximale: ").append(capaciteMax).append("\n");
        details.append("Nombre de participants: ").append(participants.size()).append("\n");
        
        return details.toString();
    }
    
    // Implémentation du pattern Observer
    @Override
    public void ajouterObservateur(ParticipantObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }
    
    @Override
    public void supprimerObservateur(ParticipantObserver observer) {
        observers.remove(observer);
    }
    
    @Override
    public void notifierObservateurs(String message) {
        for (ParticipantObserver observer : observers) {
            observer.mettreAJour(message);
        }
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
        notifierObservateurs("Le nom de l'événement a été modifié: " + nom);
    }
    
    public LocalDateTime getDate() {
        return date;
    }
    
    public void setDate(LocalDateTime date) {
        this.date = date;
        notifierObservateurs("La date de l'événement a été modifiée: " + date);
    }
    
    public String getLieu() {
        return lieu;
    }
    
    public void setLieu(String lieu) {
        this.lieu = lieu;
        notifierObservateurs("Le lieu de l'événement a été modifié: " + lieu);
    }
    
    public int getCapaciteMax() {
        return capaciteMax;
    }
    
    public List<Participant> getParticipants() {
        return new ArrayList<>(participants);
    }
}