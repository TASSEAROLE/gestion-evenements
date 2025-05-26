package fr.gestionevenements;

import fr.gestionevenements.gestionnaire.GestionEvenements;
import fr.gestionevenements.modele.Concert;
import fr.gestionevenements.modele.Conference;
import fr.gestionevenements.modele.Evenement;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.time.LocalDateTime;

public class HelloController {
    private final GestionEvenements gestionEvenements = GestionEvenements.getInstance();

    @FXML
    private TextField nomField;
    
    @FXML
    private TextField lieuField;
    
    @FXML
    private ListView<Evenement> evenementsList;

    @FXML
    protected void onAjouterConcertClick() {
        Concert concert = new Concert(
            nomField.getText(),
            LocalDateTime.now(),
            lieuField.getText(),
            100,
            "Artiste Test",
            "Rock"
        );
        gestionEvenements.ajouterEvenement(concert);
        actualiserListe();
    }

    @FXML
    protected void onAjouterConferenceClick() {
        Conference conference = new Conference(
            nomField.getText(),
            LocalDateTime.now(),
            lieuField.getText(),
            50,
            "Thème Test"
        );
        gestionEvenements.ajouterEvenement(conference);
        actualiserListe();
    }

    private void actualiserListe() {
        evenementsList.getItems().clear();
        evenementsList.getItems().addAll(gestionEvenements.getEvenements().values());
    }
}