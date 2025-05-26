package fr.gestionevenements.tests;

import fr.gestionevenements.modele.Concert;
import fr.gestionevenements.modele.Evenement;
import fr.gestionevenements.modele.Organisateur;
import fr.gestionevenements.modele.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour le pattern Observer.
 */
public class TestObserver {
    
    private Evenement evenement;
    private Participant participant;
    private Organisateur organisateur;
    private List<String> notifications;

    @BeforeEach
    public void setUp() {
        evenement = new Concert("Concert Test Observer", 
                LocalDateTime.now().plusDays(7), 
                "Salle de test", 
                50, 
                "Artiste Test", 
                "Jazz");
        
        participant = new Participant("Test Participant", "participant@test.com") {
            @Override
            public void mettreAJour(String message) {
                notifications.add("Participant: " + message);
            }
        };
        
        organisateur = new Organisateur("Test Organisateur", "organisateur@test.com") {
            @Override
            public void mettreAJour(String message) {
                notifications.add("Organisateur: " + message);
            }
        };
        
        notifications = new ArrayList<>();
    }

    @Test
    public void testAjoutObservateursEtNotification() {
        // Ajouter les observateurs
        evenement.ajouterObservateur(participant);
        evenement.ajouterObservateur(organisateur);
        
        // Déclencher une notification
        evenement.setNom("Nouveau nom de concert");
        
        // Vérifier que les deux observateurs ont été notifiés
        assertEquals(2, notifications.size());
        assertTrue(notifications.get(0).contains("Le nom de l'événement a été modifié"));
        assertTrue(notifications.get(1).contains("Le nom de l'événement a été modifié"));
    }

    @Test
    public void testSuppressionObservateur() {
        // Ajouter les observateurs
        evenement.ajouterObservateur(participant);
        evenement.ajouterObservateur(organisateur);
        
        // Supprimer un observateur
        evenement.supprimerObservateur(participant);
        
        // Déclencher une notification
        evenement.setLieu("Nouveau lieu");
        
        // Vérifier que seul l'organisateur a été notifié
        assertEquals(1, notifications.size());
        assertTrue(notifications.get(0).startsWith("Organisateur:"));
    }

    @Test
    public void testNotificationAjoutParticipant() {
        // Ajouter l'organisateur comme observateur
        evenement.ajouterObservateur(organisateur);
        
        // Ajouter un participant
        Participant nouveauParticipant = new Participant("Nouveau", "nouveau@test.com");
        evenement.ajouterParticipant(nouveauParticipant);
        
        // Vérifier que l'organisateur a été notifié
        assertEquals(1, notifications.size());
        assertTrue(notifications.get(0).contains("a été ajouté à l'événement"));
    }

    @Test
    public void testNotificationAnnulationEvenement() {
        // Ajouter les observateurs
        evenement.ajouterObservateur(participant);
        evenement.ajouterObservateur(organisateur);
        
        // Annuler l'événement
        evenement.annuler();
        
        // Vérifier que les deux observateurs ont été notifiés
        assertEquals(2, notifications.size());
        assertTrue(notifications.get(0).contains("a été annulé"));
        assertTrue(notifications.get(1).contains("a été annulé"));
    }

    @Test
    public void testNotificationSpecifiqueConcert() {
        // Ajouter l'observateur
        evenement.ajouterObservateur(participant);
        
        // Modifier un attribut spécifique au concert
        ((Concert) evenement).setArtiste("Nouvel Artiste");
        
        // Vérifier la notification
        assertEquals(1, notifications.size());
        assertTrue(notifications.get(0).contains("L'artiste du concert a été modifié"));
    }

    @Test
    public void testAjoutObservateurMultiple() {
        // Ajouter le même observateur plusieurs fois
        evenement.ajouterObservateur(participant);
        evenement.ajouterObservateur(participant);
        evenement.ajouterObservateur(participant);
        
        // Déclencher une notification
        evenement.setNom("Test multiple");
        
        // Vérifier qu'il n'y a qu'une seule notification
        assertEquals(1, notifications.size());
    }
}