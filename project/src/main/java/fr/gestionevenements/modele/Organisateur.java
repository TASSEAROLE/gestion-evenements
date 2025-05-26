package fr.gestionevenements.modele;

import java.util.ArrayList;
import java.util.List;

//Classe représentant un organisateur d'événements.

public class Organisateur extends Participant {
    private List<Evenement> evenementsOrganises;
    
    //Constructeur d'un organisateur
    public Organisateur(String nom, String email) {
        super(nom, email);
        this.evenementsOrganises = new ArrayList<>();
    }
    
    //Ajoute un événement à la liste des événements organisés
    public void ajouterEvenementOrganise(Evenement evenement) {
        if (!evenementsOrganises.contains(evenement)) {
            evenementsOrganises.add(evenement);
            // S'abonner aux notifications de cet événement
            evenement.ajouterObservateur(this);
        }
    }
    
    //Supprime un événement de la liste des événements organisés

    public void supprimerEvenementOrganise(Evenement evenement) {
        if (evenementsOrganises.contains(evenement)) {
            evenementsOrganises.remove(evenement);
            // Se désabonner des notifications de cet événement
            evenement.supprimerObservateur(this);
        }
    }
    
    //Récupère la liste des événements organisés

    public List<Evenement> getEvenementsOrganises() {
        return new ArrayList<>(evenementsOrganises);
    }
    
    //Surcharge de la méthode mettreAJour pour les organisateurs

    @Override
    public void mettreAJour(String message) {
        // Les organisateurs reçoivent des notifications plus détaillées
        System.out.println("Notification PRIORITAIRE à l'organisateur " + nom + 
                           " (" + email + "): " + message);
    }
}