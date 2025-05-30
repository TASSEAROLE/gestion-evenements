package fr.gestionevenements.modele;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Conference extends Evenement {
    @JsonProperty("theme")
    private String theme;
    @JsonProperty("intervenants")
        private List<String> intervenants;

    public Conference(String nom, LocalDateTime date, String lieu, int capaciteMax, String theme) {
        super(nom, date, lieu, capaciteMax);
        this.theme = theme;
        this.intervenants = new ArrayList<>();
    }
    public Conference() {
        super();
        this.intervenants = new ArrayList<>();

    }

    public void ajouterIntervenant(String intervenant) {
        intervenants.add(intervenant);
        notifierObservateurs("Un nouvel intervenant a été ajouté à la conférence " +  ": " + intervenant);
    }
    

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