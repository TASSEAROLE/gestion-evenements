package fr.gestionevenements.modele;

import java.time.LocalDateTime;

public class Concert extends Evenement {
    private String artiste;
    private String genreMusical;

    public Concert(String nom, LocalDateTime date, String lieu, int capaciteMax, String artiste, String genreMusical) {
        super(nom, date, lieu, capaciteMax);
        this.artiste = artiste;
        this.genreMusical = genreMusical;
    }
    public Concert() {
        super();

    }

    @Override
    public String afficherDetails() {
        StringBuilder details = new StringBuilder(super.afficherDetails());
        details.append("Type: Concert\n");
        details.append("Artiste: ").append(artiste).append("\n");
        details.append("Genre musical: ").append(genreMusical).append("\n");
        
        return details.toString();
    }
    
    // Getters et Setters spécifiques au concert
    public String getArtiste() {
        return artiste;
    }
    
    public void setArtiste(String artiste) {
        this.artiste = artiste;
        notifierObservateurs("L'artiste du concert a été modifié: " + artiste);
    }
    
    public String getGenreMusical() {
        return genreMusical;
    }
    
    public void setGenreMusical(String genreMusical) {
        this.genreMusical = genreMusical;
        notifierObservateurs("Le genre musical du concert a été modifié: " + genreMusical);
    }
}