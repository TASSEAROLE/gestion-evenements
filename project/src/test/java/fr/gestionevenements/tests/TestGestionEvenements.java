package fr.gestionevenements.tests;

import fr.gestionevenements.gestionnaire.GestionEvenements;
import fr.gestionevenements.modele.*;
import fr.gestionevenements.serialisation.JSONSerializationStrategy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TestGestionEvenements {
    
    private GestionEvenements gestionEvenements;
    private Evenement evenement1;
    private Evenement evenement2;
    private Participant participant1;
    private Participant participant2;
    private final String fichierTest = "evenements_test.json";

    @BeforeEach
    public void setUp() {
        // Récupérer l'instance singleton
        gestionEvenements = GestionEvenements.getInstance();
        
        // Définir la stratégie de sérialisation
        gestionEvenements.setSerializationStrategy(new JSONSerializationStrategy());

        evenement1 = new Concert("Concert de Test", 
                LocalDateTime.now().plusDays(10), 
                "Salle de Test", 
                100, 
                "Artiste Test", 
                "Rock");
        
        evenement2 = new Conference("Conférence de Test", 
                LocalDateTime.now().plusDays(20), 
                "Centre de conférences", 
                50, 
                "Technologie");
        
        // Créer des participants de test
        participant1 = new Participant("Jean Test", "jean@test.com");
        participant2 = new Participant("Marie Test", "marie@test.com");
    }
    
    //Nettoyage après chaque test.
    @AfterEach
    public void tearDown() {
        Map<String, Evenement> tousEvenements = gestionEvenements.getEvenements();
        for (String id : tousEvenements.keySet()) {
            gestionEvenements.supprimerEvenement(id);
        }
        
        // Supprimer le fichier de test s'il existe
        File fichier = new File(fichierTest);
        if (fichier.exists()) {
            fichier.delete();
        }
    }
    

    @Test
    public void testAjouterEvenement() {
        // Ajouter un événement
        gestionEvenements.ajouterEvenement(evenement1);
        
        // Vérifier que l'événement a bien été ajouté
        assertNotNull(gestionEvenements.rechercherEvenement(evenement1.getId()));
        assertEquals(evenement1.getNom(), gestionEvenements.rechercherEvenement(evenement1.getId()).getNom());
    }

    @Test
    public void testRechercherEvenementParNom() {
        // Ajouter des événements
        gestionEvenements.ajouterEvenement(evenement1);
        gestionEvenements.ajouterEvenement(evenement2);
        
        // Rechercher par nom
        Map<String, Evenement> resultats = gestionEvenements.rechercherEvenementParNom("Concert");
        
        // Vérifier les résultats
        assertEquals(1, resultats.size());
        assertTrue(resultats.containsKey(evenement1.getId()));
        assertFalse(resultats.containsKey(evenement2.getId()));
    }

    @Test
    public void testAjouterEvenementDejaExistant() {
        // Ajouter un événement
        gestionEvenements.ajouterEvenement(evenement1);
        
        // Essayer d'ajouter le même événement (même ID)
        assertThrows(EvenementDejaExistantException.class, () -> {
            gestionEvenements.ajouterEvenement(evenement1);
        });
    }
    

    @Test
    public void testAjouterParticipant() {
        // Ajouter un événement
        gestionEvenements.ajouterEvenement(evenement1);
        
        // Ajouter un participant à l'événement
        evenement1.ajouterParticipant(participant1);
        
        // Vérifier que le participant a bien été ajouté
        assertTrue(evenement1.getParticipants().contains(participant1));
        assertEquals(1, evenement1.getParticipants().size());
    }

    @Test
    public void testAjouterParticipantDejaInscrit() {
        // Ajouter un événement
        gestionEvenements.ajouterEvenement(evenement1);
        
        // Ajouter un participant à l'événement
        evenement1.ajouterParticipant(participant1);
        
        // Essayer d'ajouter le même participant une deuxième fois
        assertThrows(ParticipantDejaInscritException.class, () -> {
            evenement1.ajouterParticipant(participant1);
        });
    }

    @Test
    public void testSerialisationDeserialisation() {
        // Ajouter des événements
        gestionEvenements.ajouterEvenement(evenement1);
        gestionEvenements.ajouterEvenement(evenement2);
        
        // Ajouter des participants
        evenement1.ajouterParticipant(participant1);
        evenement2.ajouterParticipant(participant2);
        
        // Sauvegarder dans un fichier
        gestionEvenements.sauvegarderEvenements(fichierTest);
        
        // Supprimer tous les événements
        gestionEvenements.supprimerEvenement(evenement1.getId());
        gestionEvenements.supprimerEvenement(evenement2.getId());
        
        // Vérifier qu'ils ont été supprimés
        assertTrue(gestionEvenements.getEvenements().isEmpty());
        
        // Charger depuis le fichier
        gestionEvenements.chargerEvenements(fichierTest);
        
        // Vérifier que les événements ont été chargés
        assertEquals(2, gestionEvenements.getEvenements().size());
        assertNotNull(gestionEvenements.rechercherEvenement(evenement1.getId()));
        assertNotNull(gestionEvenements.rechercherEvenement(evenement2.getId()));
    }

    @Test
    public void testCapaciteMaxAtteinte() {
        // Créer un événement avec une capacité de 1
        Evenement evenementPetit = new Concert("Petit Concert", 
                LocalDateTime.now().plusDays(5),
                "Petite Salle",
                1, 
                "Artiste Test", 
                "Pop");
        
        // Ajouter l'événement
        gestionEvenements.ajouterEvenement(evenementPetit);
        
        // Ajouter un participant
        evenementPetit.ajouterParticipant(participant1);
        
        // Essayer d'ajouter un deuxième participant
        assertThrows(CapaciteMaxAtteinteException.class, () -> {
            evenementPetit.ajouterParticipant(participant2);
        });
    }
}