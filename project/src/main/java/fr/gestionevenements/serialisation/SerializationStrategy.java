package fr.gestionevenements.serialisation;

import fr.gestionevenements.modele.Evenement;
import java.util.Map;

//Interface pour la stratégie de sérialisation.
public interface SerializationStrategy {
    /*Sérialise une map d'événements dans un fichier
     @param evenements La map d'événements à sérialiser
      @param fichier nom du fichier où sauvegarder les données*/

    void serialiser(Map<String, Evenement> evenements, String fichier);
    
    //Désérialise une map d'événements à partir d'un fichier

    Map<String, Evenement> deserialiser(String fichier);
}