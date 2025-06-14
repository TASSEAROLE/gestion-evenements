package fr.gestionevenements.gestionnaire;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fr.gestionevenements.modele.*;
import fr.gestionevenements.serialisation.SerializationStrategy;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

//Classe principale de gestion des événements.
public class GestionEvenements {
    private static GestionEvenements instance;
    private Map<String, Evenement> evenements;
    private SerializationStrategy serializationStrategy;

    /*Constructeur privé pour le pattern Singleton*/
    private GestionEvenements() {
        this.evenements = new HashMap<>();


    }

    //Obtient l'instance unique de GestionEvenements
    public static synchronized GestionEvenements getInstance() {
        if (instance == null) {
            instance = new GestionEvenements();
        }
        return instance;
    }

    //Définit la stratégie de sérialisation à utiliser
    public void setSerializationStrategy(SerializationStrategy strategy) {
        this.serializationStrategy = strategy;
    }


    public void ajouterEvenement(Evenement evenement) throws EvenementDejaExistantException {
        if (evenements.containsKey(evenement.getId())) {
            throw new EvenementDejaExistantException("Un événement avec l'ID " + evenement.getId() + " existe déjà");
        }
        evenements.put(evenement.getId(), evenement);
    }

    //Supprime un événement de la liste des événements
    public void supprimerEvenement(String id) {
        evenements.remove(id);
    }

    //Recherche un événement par son ID
    public Evenement rechercherEvenement(String id) {
        return evenements.get(id);
    }

    //Recherche des événements par leur nom (recherche partielle)
    public Map<String, Evenement> rechercherEvenementParNom(String nom) {
        Map<String, Evenement> resultats = new HashMap<>();

        evenements.values().stream()
            .filter(e -> e.getNom().toLowerCase().contains(nom.toLowerCase()))
            .forEach(e -> resultats.put(e.getId(), e));

        return resultats;
    }

    //Sauvegarde la liste des événements en utilisant la stratégie de sérialisation définie
    public void sauvegarderEvenements(String fichier) {
        if (serializationStrategy == null) {
            throw new IllegalStateException("Aucune stratégie de sérialisation n'a été définie");
        }
        serializationStrategy.serialiser(evenements, fichier);
    }
    public void chargerEvenements(String fichier) {
        if (serializationStrategy == null) {
            throw new IllegalStateException("Aucune stratégie de sérialisation n'a été définie");
        }

        Map<String, Evenement> eventsLoaded = serializationStrategy.deserialiser(fichier);
        if (eventsLoaded != null) {
            System.out.println("Nombre d'événements chargés: " + eventsLoaded.size());
            // Afficher les clés pour vérifier
            eventsLoaded.keySet().forEach(key ->
                    System.out.println("Événement chargé: " + key + " -> " + eventsLoaded.get(key).getNom())
            );
            this.evenements = eventsLoaded;
            // Notifier l'interface que les données ont changé
           // notifierObservateurs(); // Si vous utilisez le pattern Observer
        } else {
            System.out.println("Aucun événement chargé");
        }

    }


    public CompletableFuture<Void> envoyerNotificationAsync(Participant participant, String message) {
        return CompletableFuture.runAsync(() -> {
            // Simuler un délai de traitement
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            participant.mettreAJour(message);
        });
    }

    //Obtient tous les événements
    public Map<String, Evenement> getEvenements() {
        return new HashMap<>(evenements);
    }
}