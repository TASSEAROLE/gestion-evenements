package fr.gestionevenements.modele;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// Classe Evenement (classe de base)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property= "@class"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Concert.class, name = "fr.gestionevenements.modele.Concert"),
        @JsonSubTypes.Type(value = Conference.class, name = "fr.gestionevenements.modele.Conference"),
        @JsonSubTypes.Type(value = Participant.class, name = "fr.gestionevenements.modele.Participant")
})
//@JsonIgnoreProperties(ignoreUnknown = true)

public abstract class Evenement implements EvenementObservable {
    private String id;
    @JsonProperty("Nom")
    private String nom;
    @JsonProperty("date")
    private LocalDateTime date;
    @JsonProperty("lieu")
    private String lieu;
    private int capaciteMax;
    @JsonProperty(value = "participants")
    private List<Participant> participants;
    @JsonProperty(value = "participantObserver")
    private List<ParticipantObserver> observers;

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
        this.participants = new ArrayList<>();
    }

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

    public void annuler() {
        notifierObservateurs("L'événement " + this.nom + " a été annulé");
    }

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
            observers.add(observer);
    }

    @Override
    public void supprimerObservateur(ParticipantObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifierObservateurs(String message) {
        if (this.observers == null) {
            this.observers = new ArrayList<>();
        }
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