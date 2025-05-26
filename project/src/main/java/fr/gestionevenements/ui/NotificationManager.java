package fr.gestionevenements.ui;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

//Gestionnaire de notifications pour l'interface utilisateur.
public class NotificationManager {
    // Liste des notifications pour historique
    private static final List<String> historyNotifications = new ArrayList<>();
    
    // Formatage de la date pour l'horodatage
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    

    public static void afficherNotificationTemporaire(Scene parent, String message, double dureeSecondes, boolean isError) {
        Platform.runLater(() -> {
            Stage stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setAlwaysOnTop(true);
            
            // Créer un conteneur pour la notification
            StackPane container = new StackPane();
            container.setAlignment(Pos.CENTER);
            container.setStyle(
                "-fx-background-color: " + (isError ? "#e74c3c" : "#2ecc71") + ";" +
                "-fx-padding: 15;" +
                "-fx-background-radius: 5;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 5);"
            );
            
            // Créer le texte de la notification
            Label label = new Label(message);
            label.setTextFill(Color.WHITE);
            label.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            container.getChildren().add(label);
            
            // Configurer la scène
            Scene scene = new Scene(container);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            
            // Positionner la notification
            Stage parentStage = (Stage) parent.getWindow();
            stage.setX(parentStage.getX() + parentStage.getWidth() / 2 - container.getPrefWidth() / 2);
            stage.setY(parentStage.getY() + 50);
            
            // Appliquer une animation d'entrée
            AnimationUtils.applyFadeInEffect(container);
            
            // Afficher la notification
            stage.show();
            
            // Configurer la fermeture automatique après la durée spécifiée
            PauseTransition delay = new PauseTransition(Duration.seconds(dureeSecondes));
            delay.setOnFinished(event -> {
                AnimationUtils.applyFadeOutEffect(container);
                PauseTransition fadeOut = new PauseTransition(Duration.seconds(0.5));
                fadeOut.setOnFinished(e -> stage.close());
                fadeOut.play();
            });
            delay.play();
            
            // Ajouter à l'historique
            ajouterNotificationHistorique(message, isError);
        });
    }
    
    //Affiche une notification de type Popup près d'un composant spécifique.
    public static void afficherPopupNotification(Scene parent, String message, double x, double y, double dureeSecondes) {
        Platform.runLater(() -> {
            Popup popup = new Popup();
            
            // Créer un conteneur pour la notification
            StackPane container = new StackPane();
            container.setStyle(
                "-fx-background-color: rgba(52, 152, 219, 0.9);" +
                "-fx-padding: 10;" +
                "-fx-background-radius: 5;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 5);"
            );
            
            // Créer le texte de la notification
            Label label = new Label(message);
            label.setTextFill(Color.WHITE);
            container.getChildren().add(label);
            
            // Configurer le popup
            popup.getContent().add(container);
            popup.setAutoHide(true);
            
            // Afficher le popup
            popup.show(parent.getWindow(), x, y);
            
            // Fermer automatiquement après la durée spécifiée
            PauseTransition delay = new PauseTransition(Duration.seconds(dureeSecondes));
            delay.setOnFinished(event -> popup.hide());
            delay.play();
            
            // Ajouter à l'historique
            ajouterNotificationHistorique(message, false);
        });
    }

    public static void ajouterNotificationHistorique(String message, boolean isError) {
        String horodatage = LocalDateTime.now().format(formatter);
        String typePrefix = isError ? "[ERREUR] " : "[INFO] ";
        String notification = "[" + horodatage + "] " + typePrefix + message;
        historyNotifications.add(notification);
    }

    public static List<String> getHistoryNotifications() {
        return new ArrayList<>(historyNotifications);
    }
    

    public static void clearHistory() {
        historyNotifications.clear();
    }

    public static String getHistoryAsText() {
        StringBuilder builder = new StringBuilder();
        for (String notification : historyNotifications) {
            builder.append(notification).append("\n");
        }
        return builder.toString();
    }
}