package fr.gestionevenements.serialisation;

import fr.gestionevenements.modele.Evenement;
import java.util.Map;

public interface SerializationStrategy {

    void serialiser(Map<String, Evenement> evenements, String fichier);

    Map<String, Evenement> deserialiser(String fichier);
}