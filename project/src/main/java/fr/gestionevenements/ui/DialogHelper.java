package fr.gestionevenements.ui;

import fr.gestionevenements.modele.Evenement;
import fr.gestionevenements.modele.Participant;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;

import java.util.Optional;

/**
 * Classe utilitaire pour la création et l'affichage de boîtes de dialogue.
 * Centralise les méthodes de création des dialogues pour faciliter la maintenance.
 */
public class DialogHelper {
    
    /**
     * Affiche une boîte de dialogue d'information.
     * @param titre Le titre de la boîte de dialogue
     * @param entete L'en-tête de la boîte de dialogue
     * @param message Le message à afficher
     */
    public static void afficherInformation(String titre, String entete, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(entete);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Affiche une boîte de dialogue d'erreur.
     * @param titre Le titre de la boîte de dialogue
     * @param entete L'en-tête de la boîte de dialogue
     * @param message Le message d'erreur à afficher
     */
    public static void afficherErreur(String titre, String entete, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(entete);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Affiche une boîte de dialogue de confirmation et retourne la réponse de l'utilisateur.
     * @param titre Le titre de la boîte de dialogue
     * @param entete L'en-tête de la boîte de dialogue
     * @param message Le message à afficher
     * @return True si l'utilisateur a confirmé, false sinon
     */
    public static boolean afficherConfirmation(String titre, String entete, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titre);
        alert.setHeaderText(entete);
        alert.setContentText(message);
        
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
    
    //Affiche les détails d'un événement dans une boîte de dialogue.
    public static void afficherDetailsEvenement(Evenement evenement) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Détails de l'événement");
        alert.setHeaderText(evenement.getNom());
        
        TextArea textArea = new TextArea(evenement.afficherDetails());
        textArea.setEditable(false);
        textArea.setWrapText(true);
        
        alert.getDialogPane().setContent(textArea);
        alert.getDialogPane().setMinHeight(300);
        alert.getDialogPane().setMinWidth(400);
        
        alert.showAndWait();
    }
    
    /*Affiche les détails d'un participant dans une boîte de dialogue.
     @param participant Le participant dont les détails doivent être affichés*/

    public static void afficherDetailsParticipant(Participant participant) {
        StringBuilder details = new StringBuilder();
        details.append("ID: ").append(participant.getId()).append("\n");
        details.append("Nom: ").append(participant.getNom()).append("\n");
        details.append("Email: ").append(participant.getEmail()).append("\n");
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Détails du participant");
        alert.setHeaderText(participant.getNom());
        alert.setContentText(details.toString());
        
        alert.showAndWait();
    }
}