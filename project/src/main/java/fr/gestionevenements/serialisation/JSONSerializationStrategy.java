package fr.gestionevenements.serialisation;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fr.gestionevenements.modele.Evenement;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Implémentation de la stratégie de sérialisation/désérialisation au format JSON.
 * Utilise Jackson pour gérer la persistance des événements avec prise en charge des types Java 8 (LocalDateTime).
 */
public class JSONSerializationStrategy implements SerializationStrategy {
    private final ObjectMapper objectMapper;

    /**
     * Constructeur initialisant l'ObjectMapper avec le module JavaTimeModule pour gérer LocalDateTime.
     */
    public JSONSerializationStrategy() {
        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
        objectMapper.activateDefaultTyping(
                objectMapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );// Ajout du module pour supporter LocalDateTime
        // Les annotations @JsonTypeInfo et @JsonSubTypes sur Evenement gèrent la polymorphie
    }

    /**
     * Sérialise une map d'événements dans un fichier JSON.
     * @param evenements Map contenant les événements à sérialiser.
     * @param fichier Chemin du fichier de sortie.
     */
    @Override
    public void serialiser(Map<String, Evenement> evenements, String fichier) {
        try {
            File file = new File(fichier);
            File repertoireParent = file.getParentFile();

            // Création du répertoire parent s'il n'existe pas
            if (repertoireParent != null && !repertoireParent.exists()) {
                boolean cree = repertoireParent.mkdirs();
                if (!cree) {
                    System.err.println("⚠ Impossible de créer le répertoire: " + repertoireParent.getAbsolutePath());
                    return; // Arrêt si le répertoire ne peut pas être créé
                }
            }

            // Sérialisation avec gestion des types polymorphiques
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, evenements);
            System.out.println("✓ Sérialisation réussie: " + evenements.size() + " événements sauvegardés dans " + fichier);
            System.out.println("  Taille du fichier: " + file.length() + " bytes");

            // Debug: Affichage du contenu JSON généré
            String jsonContent = objectMapper.writeValueAsString(evenements);
            System.out.println("Contenu JSON généré : " + jsonContent);

        } catch (IOException e) {
            System.err.println("✗ Erreur lors de la sérialisation JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Désérialise un fichier JSON en une map d'événements.
     * @param fichier Chemin du fichier JSON à lire.
     * @return Map contenant les événements désérialisés, ou une map vide en cas d'erreur.
     */
    @Override
    public Map<String, Evenement> deserialiser(String fichier) {
        try {
            File file = new File(fichier);
            if (!file.exists()) {
                System.out.println("⚠ Le fichier " + fichier + " n'existe pas.");
                return new HashMap<>();
            }

            if (file.length() == 0) {
                System.out.println("⚠ Le fichier " + fichier + " est vide.");
                return new HashMap<>();
            }

            System.out.println("📁 Lecture du fichier: " + fichier + " (" + file.length() + " bytes)");

            // Désérialisation avec prise en charge des types polymorphiques
            Map<String, Evenement> evenements = objectMapper.readValue(
                    file,
                    objectMapper.getTypeFactory().constructMapType(
                            Map.class, String.class, Evenement.class
                    )
            );

            System.out.println("✓ Désérialisation réussie: " + evenements.size() + " événements chargés");

            // Affichage des détails pour débogage
            evenements.forEach((key, event) -> {
                System.out.println("  - " + key + ": " + event.getClass().getSimpleName() +
                        " - Nom: " + event.getNom() + ", Date: " + event.getDate() +
                        ", Lieu: " + event.getLieu() +
                        ", Participants: " + (event.getParticipants() != null ? event.getParticipants().size() : 0));
            });

            return evenements;

        } catch (IOException e) {
            System.err.println("✗ Erreur lors de la désérialisation JSON: " + e.getMessage());
            e.printStackTrace();
            return new HashMap<>();
        }
    }
}