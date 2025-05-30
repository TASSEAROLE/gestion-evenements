package fr.gestionevenements.modele;

import java.util.ArrayList;
import java.util.List;

public class Organisateur extends Participant {
    private List<Evenement> evenementsOrganises;
    
    //Constructeur d'un organisateur
    public Organisateur(String nom, String email) {
        super(nom, email);
        this.evenementsOrganises = new ArrayList<>();
    }
    public void ajouterEvenementOrganise(Evenement evenement) {
        if (!evenementsOrganises.contains(evenement)) {
            evenementsOrganises.add(evenement);
            // S'abonner aux notifications de cet événement
            evenement.ajouterObservateur(this);
        }
    }

    public void supprimerEvenementOrganise(Evenement evenement) {
        if (evenementsOrganises.contains(evenement)) {
            evenementsOrganises.remove(evenement);
            // Se désabonner des notifications de cet événement
            evenement.supprimerObservateur(this);
        }
    }

    public List<Evenement> getEvenementsOrganises() {
        return new ArrayList<>(evenementsOrganises);
    }

    @Override
    public void mettreAJour(String message) {
        System.out.println("Notification PRIORITAIRE à l'organisateur " + nom + 
                           " (" + email + "): " + message);
    }
}