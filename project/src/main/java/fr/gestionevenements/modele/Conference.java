package fr.gestionevenements.modele;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant une conférence, qui est un type spécifique d'événement.
 * Hérite de la classe abstraite Evenement.
 */
public class Conference extends Evenement {
    private String theme;
    private List<String> intervenants;
    
    /**
     * Constructeur d'une conférence
     * @param nom Nom de la conférence
     * @param date Date et heure de la conférence
     * @param lieu Lieu de la conférence
     * @param capaciteMax Capacité maximale de participants
     * @param theme Thème de la conférence
     */
    public Conference(String nom, LocalDateTime date, String lieu, int capaciteMax, String theme) {
        super(nom, date, lieu, capaciteMax);
        this.theme = theme;
        this.intervenants = new ArrayList<>();
    }
    public Conference() {
        super();

    }
    
    /**
     * Ajoute un intervenant à la conférence
     * @param intervenant Nom de l'intervenant à ajouter
     */
    public void ajouterIntervenant(String intervenant) {
        intervenants.add(intervenant);
        notifierObservateurs("Un nouvel intervenant a été ajouté à la conférence " + this.nom + ": " + intervenant);
    }
    
    /**
     * Affiche les détails de la conférence, y compris les intervenants
     * @return Une chaîne de caractères contenant les détails de la conférence
     */
    @Override
    public String afficherDetails() {
        StringBuilder details = new StringBuilder(super.afficherDetails());
        details.append("Type: Conférence\n");
        details.append("Thème: ").append(theme).append("\n");
        
        details.append("Intervenants: \n");
        for (String intervenant : intervenants) {
            details.append("- ").append(intervenant).append("\n");
        }
        
        return details.toString();
    }
    
    // Getters et Setters spécifiques à la conférence
    public String getTheme() {
        return theme;
    }
    
    public void setTheme(String theme) {
        this.theme = theme;
        notifierObservateurs("Le thème de la conférence a été modifié: " + theme);
    }
    
    public List<String> getIntervenants() {
        return new ArrayList<>(intervenants);
    }
}