package fr.gestionevenements.serialisation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fr.gestionevenements.modele.Concert;
import fr.gestionevenements.modele.Conference;
import fr.gestionevenements.modele.Evenement;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//Implémentation de la stratégie de sérialisation en JSON.
public class JSONSerializationStrategy implements SerializationStrategy {
    private final ObjectMapper objectMapper;
    
    //Constructeur de la stratégie de sérialisation JSON
    public JSONSerializationStrategy() {
        objectMapper = new ObjectMapper();
        // Configurer Jackson pour gérer les dates et les types polymorphiques
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        // Enregistrer les sous-types pour la désérialisation polymorphique
        objectMapper.registerSubtypes(
            new NamedType(Conference.class, "conference"),
            new NamedType(Concert.class, "concert")
        );
    }
    
    /**
     * Sérialise une map d'événements dans un fichier JSON
     * @param evenements La map d'événements à sérialiser
     * @param fichier Le nom du fichier JSON où sauvegarder les données
     */
    @Override
    public void serialiser(Map<String, Evenement> evenements, String fichier) {
        try {
            objectMapper.writeValue(new File(fichier), evenements);
            System.out.println("Données sérialisées avec succès dans " + fichier);
        } catch (IOException e) {
            System.err.println("Erreur lors de la sérialisation JSON: " + e.getMessage());
        }
    }
    
    //Désérialise une map d'événements à partir d'un fichier JSON

    @Override
    public Map<String, Evenement> deserialiser(String fichier) {
        try {
            File file = new File(fichier);
            if (!file.exists()) {
                System.out.println("Le fichier " + fichier + " n'existe pas.");
                return new HashMap<>();
            }
            
            Map<String, Evenement> evenements = objectMapper.readValue(
                file, 
                objectMapper.getTypeFactory().constructMapType(
                    HashMap.class, String.class, Evenement.class
                )
            );
            
            System.out.println("Données désérialisées avec succès depuis " + fichier);
            return evenements;
        } catch (IOException e) {
            System.err.println("Erreur lors de la désérialisation JSON: " + e.getMessage());
            return new HashMap<>();
        }
    }
}