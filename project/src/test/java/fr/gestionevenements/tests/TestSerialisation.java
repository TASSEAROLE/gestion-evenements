package fr.gestionevenements.tests;

import fr.gestionevenements.modele.Concert;
import fr.gestionevenements.modele.Conference;
import fr.gestionevenements.modele.Evenement;
import fr.gestionevenements.modele.Participant;
import fr.gestionevenements.serialisation.JSONSerializationStrategy;
import fr.gestionevenements.serialisation.SerializationStrategy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

//Tests unitaires pour les stratégies de sérialisation.

public class TestSerialisation {
    
    private Map<String, Evenement> evenements;
    private final String fichierTestJSON = "evenements.json";

    @BeforeEach
    public void setUp() {
        evenements = new HashMap<>();
        
        // Créer des événements de test
        Concert concert = new Concert("Concert de Test", 
                LocalDateTime.now().plusDays(15), 
                "Salle de musique", 
                200, 
                "Groupe Test", 
                "Rock");
        
        Conference conference = new Conference("Conférence de Test", 
                LocalDateTime.now().plusDays(30), 
                "Centre de congrès", 
                100, 
                "Informatique");
        
        // Ajouter des participants
        Participant participant1 = new Participant("Pierre Test", "pierre@test.com");
        Participant participant2 = new Participant("Sophie Test", "sophie@test.com");
        
        concert.ajouterParticipant(participant1);
        conference.ajouterParticipant(participant2);
        
        // Ajouter un intervenant à la conférence
        conference.ajouterIntervenant("Intervenant Test");
        
        // Ajouter les événements à la map
        evenements.put(concert.getId(), concert);
        evenements.put(conference.getId(), conference);
    }

    @AfterEach
    public void tearDown() throws URISyntaxException {
        // Supprimer les fichiers de test s'ils existent
        URL resourceUrl = getClass().getClassLoader().getResource("fr/gestionevenements/datas/evenements.json");
        if (resourceUrl != null) {
            File fichierJSON = new File(resourceUrl.toURI());
            if (fichierJSON.exists()) {
                fichierJSON.delete();
            }
        }
    }

    @Test
    public void testJSONSerialization() {
        SerializationStrategy strategy = new JSONSerializationStrategy();
        
        // Sérialiser les événements
        strategy.serialiser(evenements, fichierTestJSON);
        
        // Vérifier que le fichier a été créé
        File fichier = new File(fichierTestJSON);
        assertTrue(fichier.exists());
        
        // Désérialiser les événements
        Map<String, Evenement> evenementsCharges = strategy.deserialiser(fichierTestJSON);
        
        // Vérifier que les événements ont été correctement chargés
        assertNotNull(evenementsCharges);
        assertEquals(evenements.size(), evenementsCharges.size());
        
        // Vérifier que les IDs sont préservés
        for (String id : evenements.keySet()) {
            assertTrue(evenementsCharges.containsKey(id));
        }
        
        // Vérifier que les types sont préservés
        for (String id : evenements.keySet()) {
            Evenement original = evenements.get(id);
            Evenement charge = evenementsCharges.get(id);
            
            assertEquals(original.getClass(), charge.getClass());
            assertEquals(original.getNom(), charge.getNom());
            assertEquals(original.getLieu(), charge.getLieu());
        }
    }

    @Test
    public void testDeserializationFichierInexistant() {
        SerializationStrategy strategyJSON = new JSONSerializationStrategy();
       /* SerializationStrategy strategyXML = new XMLSerializationStrategy();*/
        
        // Tester avec un fichier JSON inexistant
        Map<String, Evenement> resultatJSON = strategyJSON.deserialiser("fichier_inexistant.json");
        assertNotNull(resultatJSON);
        assertTrue(resultatJSON.isEmpty());

    }
}