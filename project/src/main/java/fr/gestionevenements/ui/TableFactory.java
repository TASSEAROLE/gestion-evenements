package fr.gestionevenements.ui;

import fr.gestionevenements.modele.Concert;
import fr.gestionevenements.modele.Conference;
import fr.gestionevenements.modele.Evenement;
import fr.gestionevenements.modele.Organisateur;
import fr.gestionevenements.modele.Participant;
import fr.gestionevenements.utils.DateTimeUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.format.DateTimeFormatter;

/**
 * Fabrique pour créer des tableaux avec des configurations spécifiques.
 * Implémente le pattern Factory pour la création de tableaux.
 */
public class TableFactory {
    
    //Crée un tableau pour afficher les événements.

    public static TableView<Evenement> creerTableauEvenements() {
        TableView<Evenement> tableau = new TableView<>();
        
        // Configuration des colonnes
        TableColumn<Evenement, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cellData -> 
                new SimpleStringProperty(cellData.getValue().getId()));
        colId.setPrefWidth(220);
        
        TableColumn<Evenement, String> colNom = new TableColumn<>("Nom");
        colNom.setCellValueFactory(cellData -> 
                new SimpleStringProperty(cellData.getValue().getNom()));
        colNom.setPrefWidth(150);
        
        TableColumn<Evenement, String> colDate = new TableColumn<>("Date");
        colDate.setCellValueFactory(cellData -> 
                new SimpleStringProperty(DateTimeUtils.formatDateTime(cellData.getValue().getDate())));
        colDate.setPrefWidth(120);
        
        TableColumn<Evenement, String> colLieu = new TableColumn<>("Lieu");
        colLieu.setCellValueFactory(cellData -> 
                new SimpleStringProperty(cellData.getValue().getLieu()));
        colLieu.setPrefWidth(150);
        
        TableColumn<Evenement, String> colType = new TableColumn<>("Type");
        colType.setCellValueFactory(cellData -> {
            Evenement evenement = cellData.getValue();
            if (evenement instanceof Concert) {
                return new SimpleStringProperty("Concert");
            } else if (evenement instanceof Conference) {
                return new SimpleStringProperty("Conférence");
            }
            return new SimpleStringProperty("Inconnu");
        });
        colType.setPrefWidth(100);
        
        TableColumn<Evenement, String> colParticipants = new TableColumn<>("Participants");
        colParticipants.setCellValueFactory(cellData -> 
                new SimpleStringProperty(String.valueOf(cellData.getValue().getParticipants().size())));
        colParticipants.setPrefWidth(100);
        
        tableau.getColumns().addAll(colId, colNom, colDate, colLieu, colType, colParticipants);
        
        // Appliquer un style spécifique
        StylesManager.appliquerStyleTableau(tableau);
        
        return tableau;
    }
    

     //Crée un tableau pour afficher les participants.
    public static TableView<Participant> creerTableauParticipants() {
        TableView<Participant> tableau = new TableView<>();
        
        // Configuration des colonnes
        TableColumn<Participant, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cellData -> 
                new SimpleStringProperty(cellData.getValue().getId()));
        colId.setPrefWidth(220);
        
        TableColumn<Participant, String> colNom = new TableColumn<>("Nom");
        colNom.setCellValueFactory(cellData -> 
                new SimpleStringProperty(cellData.getValue().getNom()));
        colNom.setPrefWidth(150);
        
        TableColumn<Participant, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(cellData -> 
                new SimpleStringProperty(cellData.getValue().getEmail()));
        colEmail.setPrefWidth(200);
        
        TableColumn<Participant, String> colType = new TableColumn<>("Type");
        colType.setCellValueFactory(cellData -> {
            if (cellData.getValue() instanceof Organisateur) {
                return new SimpleStringProperty("Organisateur");
            } else {
                return new SimpleStringProperty("Participant");
            }
        });
        colType.setPrefWidth(100);
        
        // Personnalisation des cellules pour la colonne type
        colType.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                
                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if ("Organisateur".equals(item)) {
                        setStyle("-fx-text-fill: #2ecc71; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: #3498db;");
                    }
                }
            }
        });
        
        tableau.getColumns().addAll(colId, colNom, colEmail, colType);
        
        // Appliquer un style spécifique
        StylesManager.appliquerStyleTableau(tableau);
        
        return tableau;
    }
}