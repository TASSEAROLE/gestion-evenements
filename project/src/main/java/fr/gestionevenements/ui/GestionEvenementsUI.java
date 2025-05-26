package fr.gestionevenements.ui;

import fr.gestionevenements.gestionnaire.GestionEvenements;
import fr.gestionevenements.modele.*;
import fr.gestionevenements.serialisation.JSONSerializationStrategy;
import fr.gestionevenements.serialisation.SerializationStrategy;
import fr.gestionevenements.service.NotificationService;
import fr.gestionevenements.service.NotificationServiceFactory;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.nio.file.Paths;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

//Classe principale de l'interface utilisateur JavaFX.
public class GestionEvenementsUI extends Application {


    // Gestionnaire d'événements (Singleton)
    private final GestionEvenements gestionEvenements = GestionEvenements.getInstance();
    
    // Services de notification
    private final NotificationService emailService = 
            NotificationServiceFactory.creerNotificationService("email", "admin@evenements.fr");
    
    // Composants de l'interface
    private TabPane tabPane;
    private TableView<Evenement> tableEvenements;
    private TableView<Participant> tableParticipants;
    private TextArea zoneNotifications;
    
    // Listes observables pour les tableaux
    private ObservableList<Evenement> listeEvenements;
    private ObservableList<Participant> listeParticipants;


    //Méthode principale de démarrage de l'interface JavaFX.
     /*Configure et affiche la fenêtre principale.*/
    @Override
    public void start(Stage primaryStage) {
        // Initialiser le gestionnaire avec la stratégie de sérialisation JSON
        gestionEvenements.setSerializationStrategy(new JSONSerializationStrategy());
        gestionEvenements.chargerEvenements("evenements.json");
        initComponents();
        
        // Création de la scène principale
        BorderPane root = new BorderPane();
        root.setCenter(tabPane);
        
        // Création de la zone de notifications
        VBox bottomBox = new VBox(5);
        Label notifLabel = new Label("Notifications:");
        zoneNotifications = new TextArea();
        zoneNotifications.setEditable(false);
        zoneNotifications.setPrefHeight(100);
        bottomBox.getChildren().addAll(notifLabel, zoneNotifications);
        root.setBottom(bottomBox);
        BorderPane.setMargin(bottomBox, new Insets(10));
        
        // Configuration et affichage de la scène
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Système de Gestion d'Événements");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // Charger les données existantes
        try {
            gestionEvenements.chargerEvenements("evenements.json");
            actualiserTableEvenements();
            afficherNotification("Données chargées avec succès.");
        } catch (Exception e) {
            afficherNotification("Aucune donnée précédente trouvée ou erreur de chargement.");
        }
    }
    
    /**
     * Initialise les composants principaux de l'interface.
     */
    private void initComponents() {
        // Création du TabPane principal
        tabPane = new TabPane();
        
        // Onglet Événements
        Tab tabEvenements = new Tab("EVENEMENTS");
        tabEvenements.setClosable(false);
        tabEvenements.setContent(creerContenuOngletEvenements());
        
        // Onglet Participants
        Tab tabParticipants = new Tab("PARTICIPANTS");
        tabParticipants.setClosable(false);
        tabParticipants.setContent(creerContenuOngletParticipants());
        
        // Onglet Sauvegarde
        Tab tabSauvegarde = new Tab("SAUVEGARDE");
        tabSauvegarde.setClosable(false);
        tabSauvegarde.setContent(creerContenuOngletSauvegarde());
        
        // Ajout des onglets au TabPane
        tabPane.getTabs().addAll(tabEvenements, tabParticipants, tabSauvegarde);
    }
    
    //Crée le contenu de l'onglet de gestion des événements.

    private BorderPane creerContenuOngletEvenements() {
        BorderPane pane = new BorderPane();
        
        // Tableau des événements
        tableEvenements = new TableView<>();
        listeEvenements = FXCollections.observableArrayList();
        tableEvenements.setItems(listeEvenements);
        
        // Colonnes du tableau
        TableColumn<Evenement, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId()));
        
        TableColumn<Evenement, String> colNom = new TableColumn<>("Nom");
        colNom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
        
        TableColumn<Evenement, String> colDate = new TableColumn<>("Date");
        colDate.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
        
        TableColumn<Evenement, String> colLieu = new TableColumn<>("Lieu");
        colLieu.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLieu()));
        
        TableColumn<Evenement, String> colType = new TableColumn<>("Type");
        colType.setCellValueFactory(cellData -> {
            if (cellData.getValue() instanceof Concert) {
                return new SimpleStringProperty("Concert");
            } else if (cellData.getValue() instanceof Conference) {
                return new SimpleStringProperty("Conférence");
            }
            return new SimpleStringProperty("Inconnu");
        });
        
        tableEvenements.getColumns().addAll(colId, colNom, colDate, colLieu, colType);
        
        // Création des boutons d'action
        HBox boutons = new HBox(10);
        boutons.setPadding(new Insets(10));
        boutons.setAlignment(Pos.CENTER);
        
        Button btnAjouter = new Button("Ajouter un événement");
        btnAjouter.setOnAction(e -> afficherDialogueAjoutEvenement());
        
        Button btnSupprimer = new Button("Supprimer");
        btnSupprimer.setOnAction(e -> supprimerEvenementSelectionne());
        
        Button btnDetails = new Button("Voir détails");
        btnDetails.setOnAction(e -> afficherDetailsEvenement());
        
        Button btnInscrire = new Button("Inscrire participant");
        btnInscrire.setOnAction(e -> inscrireParticipant());
        
        boutons.getChildren().addAll(btnAjouter, btnSupprimer, btnDetails, btnInscrire);
        
        pane.setCenter(tableEvenements);
        pane.setBottom(boutons);
        
        return pane;
    }
    
    //Crée le contenu de l'onglet de gestion des participants.
    private BorderPane creerContenuOngletParticipants() {
        BorderPane pane = new BorderPane();
        
        // Tableau des participants
        tableParticipants = new TableView<>();
        listeParticipants = FXCollections.observableArrayList();
        tableParticipants.setItems(listeParticipants);
        
        // Colonnes du tableau
        TableColumn<Participant, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId()));
        
        TableColumn<Participant, String> colNom = new TableColumn<>("Nom");
        colNom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
        
        TableColumn<Participant, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        
        TableColumn<Participant, String> colType = new TableColumn<>("Type");
        colType.setCellValueFactory(cellData -> {
            if (cellData.getValue() instanceof Organisateur) {
                return new SimpleStringProperty("Organisateur");
            } else {
                return new SimpleStringProperty("Participant");
            }
        });
        
        tableParticipants.getColumns().addAll(colId, colNom, colEmail, colType);
        
        // Création des boutons d'action
        HBox boutons = new HBox(10);
        boutons.setPadding(new Insets(10));
        boutons.setAlignment(Pos.CENTER);
        
        Button btnAjouter = new Button("Ajouter un participant");
        btnAjouter.setOnAction(e -> afficherDialogueAjoutParticipant());
        
        Button btnAjouterOrg = new Button("Ajouter un organisateur");
        btnAjouterOrg.setOnAction(e -> afficherDialogueAjoutOrganisateur());
        
        Button btnSupprimer = new Button("Supprimer");
        btnSupprimer.setOnAction(e -> supprimerParticipantSelectionne());
        
        boutons.getChildren().addAll(btnAjouter, btnAjouterOrg, btnSupprimer);
        
        pane.setCenter(tableParticipants);
        pane.setBottom(boutons);
        
        return pane;
    }
    
    /**
     * Crée le contenu de l'onglet de sauvegarde des données.
     * @return Le contenu de l'onglet
     */
    private VBox creerContenuOngletSauvegarde() {
        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);
        
        Label titre = new Label("Sauvegarde et Chargement des Données");
        titre.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        // Options de format
        HBox formatBox = new HBox(20);
        formatBox.setAlignment(Pos.CENTER);
        
        Label lblFormat = new Label("Format de sauvegarde :");
        
        ToggleGroup groupFormat = new ToggleGroup();
        RadioButton rbJSON = new RadioButton("JSON");
        rbJSON.setToggleGroup(groupFormat);
        rbJSON.setSelected(true);

        
        formatBox.getChildren().addAll(lblFormat, rbJSON);///////
        
        // Boutons de sauvegarde et chargement
        HBox boutonsBox = new HBox(20);
        boutonsBox.setAlignment(Pos.CENTER);
        
        Button btnSauvegarder = new Button("Sauvegarder les données");
        btnSauvegarder.setOnAction(e -> {
            SerializationStrategy strategy = null;
            String extension = "";
            
            if (rbJSON.isSelected()) {
                strategy = new JSONSerializationStrategy();
                extension = ".json";
            }
            gestionEvenements.setSerializationStrategy(strategy);
            gestionEvenements.sauvegarderEvenements("evenements" + extension);
            afficherNotification("Données sauvegardées avec succès au format " + extension.toUpperCase() + ".");
        });
        
        Button btnCharger = new Button("Charger les données");
        btnCharger.setOnAction(e -> {
            SerializationStrategy strategy = null;
            String extension = null;
            
            if (rbJSON.isSelected()) {
                strategy = new JSONSerializationStrategy();
                extension = "json";
            }
            
            gestionEvenements.setSerializationStrategy(strategy);
            
            try {
                gestionEvenements.chargerEvenements("evenements." + extension);
                actualiserTableEvenements();
                afficherNotification("Données chargées avec succès depuis le format " + extension.toUpperCase() + ".");
            } catch (Exception ex) {
                afficherNotification("Erreur lors du chargement des données : " + ex.getMessage());
            }
        });
        
        boutonsBox.getChildren().addAll(btnSauvegarder, btnCharger);
        
        vbox.getChildren().addAll(titre, formatBox, boutonsBox);
        return vbox;
    }

    // Affiche une boîte de dialogue pour ajouter un nouvel événement.
    private void afficherDialogueAjoutEvenement() {
        // Création d'un dialogue personnalisé
        Dialog<Evenement> dialogue = new Dialog<>();
        dialogue.setTitle("Ajouter un événement");
        dialogue.setHeaderText("Veuillez saisir les informations de l'événement");
        
        // Ajout des boutons
        ButtonType btnConfirmer = new ButtonType("Confirmer", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnAnnuler = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialogue.getDialogPane().getButtonTypes().addAll(btnConfirmer, btnAnnuler);
        
        // Création du formulaire
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        TextField tfNom = new TextField();
        TextField tfLieu = new TextField();
        TextField tfCapacite = new TextField();
        DatePicker dpDate = new DatePicker();
        TextField tfHeure = new TextField();
        tfHeure.setPromptText("HH:MM");
        
        // Champ type d'événement
        ChoiceBox<String> cbType = new ChoiceBox<>();
        cbType.getItems().addAll("Concert", "Conférence");
        cbType.setValue("Concert");
        
        // Champs spécifiques au type
        Label lblSpecifique1 = new Label("Artiste :");
        TextField tfSpecifique1 = new TextField();
        
        Label lblSpecifique2 = new Label("Genre musical :");
        TextField tfSpecifique2 = new TextField();
        
        // Changement des champs selon le type sélectionné
        cbType.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if ("Concert".equals(newVal)) {
                lblSpecifique1.setText("Artiste :");
                lblSpecifique2.setText("Genre musical :");
            } else {
                lblSpecifique1.setText("Thème :");
                lblSpecifique2.setText("Mitre de conférence :");
            }
        });
        
        // Ajout des champs au formulaire
        grid.add(new Label("Nom :"), 0, 0);
        grid.add(tfNom, 1, 0);
        grid.add(new Label("Lieu :"), 0, 1);
        grid.add(tfLieu, 1, 1);
        grid.add(new Label("Capacité max :"), 0, 2);
        grid.add(tfCapacite, 1, 2);
        grid.add(new Label("Date :"), 0, 3);
        grid.add(dpDate, 1, 3);
        grid.add(new Label("Heure :"), 0, 4);
        grid.add(tfHeure, 1, 4);
        grid.add(new Label("Type :"), 0, 5);
        grid.add(cbType, 1, 5);
        grid.add(lblSpecifique1, 0, 6);
        grid.add(tfSpecifique1, 1, 6);
        grid.add(lblSpecifique2, 0, 7);
        grid.add(tfSpecifique2, 1, 7);
        
        dialogue.getDialogPane().setContent(grid);
        
        // Conversion du résultat
        dialogue.setResultConverter(dialogButton -> {
            if (dialogButton == btnConfirmer) {
                try {
                    String nom = tfNom.getText();
                    String lieu = tfLieu.getText();
                    int capacite = Integer.parseInt(tfCapacite.getText());
                    
                    String[] heureMinute = tfHeure.getText().split(":");
                    int heure = Integer.parseInt(heureMinute[0]);
                    int minute = Integer.parseInt(heureMinute[1]);
                    
                    LocalDateTime dateHeure = LocalDateTime.of(
                            dpDate.getValue().getYear(),
                            dpDate.getValue().getMonthValue(),
                            dpDate.getValue().getDayOfMonth(),
                            heure, minute);
                    
                    Evenement evenement;
                    
                    if ("Concert".equals(cbType.getValue())) {
                        evenement = new Concert(nom, dateHeure, lieu, capacite, 
                                tfSpecifique1.getText(), tfSpecifique2.getText());
                    } else {
                        evenement = new Conference(nom, dateHeure, lieu, capacite, tfSpecifique1.getText());
                        if (!tfSpecifique2.getText().isEmpty()) {
                            ((Conference) evenement).ajouterIntervenant(tfSpecifique2.getText());
                        }
                    }
                    
                    return evenement;
                } catch (Exception e) {
                    afficherNotification("Erreur : " + e.getMessage());
                    return null;
                }
            }
            return null;
        });
        
        // Affichage et traitement du résultat
        Optional<Evenement> resultat = dialogue.showAndWait();
        
        resultat.ifPresent(evenement -> {
            try {
                gestionEvenements.ajouterEvenement(evenement);
                actualiserTableEvenements();
                afficherNotification("Événement ajouté avec succès : " + evenement.getNom());
            } catch (EvenementDejaExistantException e) {
                afficherNotification("Erreur : " + e.getMessage());
            }
        });
    }
    
    /**
     * Supprime l'événement sélectionné dans le tableau.
     */
    private void supprimerEvenementSelectionne() {
        Evenement evenement = tableEvenements.getSelectionModel().getSelectedItem();
        
        if (evenement == null) {
            afficherNotification("Aucun événement sélectionné.");
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer l'événement");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer l'événement : " + evenement.getNom() + " ?");
        
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.isPresent() && result.get() == ButtonType.OK) {
            gestionEvenements.supprimerEvenement(evenement.getId());
            actualiserTableEvenements();
            afficherNotification("Événement supprimé : " + evenement.getNom());
        }
    }
    
    /**
     * Affiche les détails de l'événement sélectionné.
     */
    private void afficherDetailsEvenement() {
        Evenement evenement = tableEvenements.getSelectionModel().getSelectedItem();
        
        if (evenement == null) {
            afficherNotification("Aucun événement sélectionné.");
            return;
        }
        
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
    
    //Inscrit un participant à l'événement sélectionné.
    private void inscrireParticipant() {
        Evenement evenement = tableEvenements.getSelectionModel().getSelectedItem();
        
        if (evenement == null) {
            afficherNotification("Aucun événement sélectionné.");
            return;
        }
        
        // Dialogue de sélection ou création d'un participant
        Dialog<Participant> dialogue = new Dialog<>();
        dialogue.setTitle("Inscrire un participant");
        dialogue.setHeaderText("Choisissez un participant ou créez-en un nouveau");
        
        ButtonType btnSelectionner = new ButtonType("Sélectionner", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnNouveau = new ButtonType("Nouveau participant", ButtonBar.ButtonData.OTHER);
        ButtonType btnAnnuler = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        
        dialogue.getDialogPane().getButtonTypes().addAll(btnSelectionner, btnNouveau, btnAnnuler);
        
        ListView<Participant> listeVueParticipants = new ListView<>();
        listeVueParticipants.setItems(listeParticipants);
        listeVueParticipants.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Participant item, boolean empty) {
                super.updateItem(item, empty);
                
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNom() + " (" + item.getEmail() + ")");
                }
            }
        });
        
        dialogue.getDialogPane().setContent(listeVueParticipants);
        
        Button btnSelectionnerButton = (Button) dialogue.getDialogPane().lookupButton(btnSelectionner);
        btnSelectionnerButton.setDisable(true);
        
        listeVueParticipants.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> 
                btnSelectionnerButton.setDisable(newValue == null));
        
        dialogue.setResultConverter(dialogButton -> {
            if (dialogButton == btnSelectionner) {
                return listeVueParticipants.getSelectionModel().getSelectedItem();
            } else if (dialogButton == btnNouveau) {
                return creerNouveauParticipant();
            }
            return null;
        });
        
        Optional<Participant> resultat = dialogue.showAndWait();
        
        resultat.ifPresent(participant -> {
            try {
                evenement.ajouterParticipant(participant);
                
                // S'abonner aux notifications
                evenement.ajouterObservateur(participant);
                
                // Notification asynchrone
                CompletableFuture<Void> future = gestionEvenements.envoyerNotificationAsync(
                        participant, 
                        "Vous avez été inscrit à l'événement : " + evenement.getNom());
                
                // Gérer le résultat de la notification asynchrone
                future.thenRunAsync(() -> 
                    afficherNotification("Notification envoyée à " + participant.getNom()), 
                    Platform::runLater);
                
                afficherNotification("Participant inscrit avec succès : " + participant.getNom());
            } catch (Exception e) {
                afficherNotification("Erreur lors de l'inscription : " + e.getMessage());
            }
        });
    }
    
    /**
     * Affiche une boîte de dialogue pour ajouter un nouveau participant.
     */
    private void afficherDialogueAjoutParticipant() {
        Dialog<Participant> dialogue = new Dialog<>();
        dialogue.setTitle("Ajouter un participant");
        dialogue.setHeaderText("Veuillez saisir les informations du participant");
        
        ButtonType btnConfirmer = new ButtonType("Confirmer", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnAnnuler = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialogue.getDialogPane().getButtonTypes().addAll(btnConfirmer, btnAnnuler);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        TextField tfNom = new TextField();
        TextField tfEmail = new TextField();
        
        grid.add(new Label("Nom :"), 0, 0);
        grid.add(tfNom, 1, 0);
        grid.add(new Label("Email :"), 0, 1);
        grid.add(tfEmail, 1, 1);
        
        dialogue.getDialogPane().setContent(grid);
        
        dialogue.setResultConverter(dialogButton -> {
            if (dialogButton == btnConfirmer) {
                String nom = tfNom.getText();
                String email = tfEmail.getText();
                
                if (nom.isEmpty() || email.isEmpty()) {
                    afficherNotification("Tous les champs sont obligatoires.");
                    return null;
                }
                
                return new Participant(nom, email);
            }
            return null;
        });
        
        Optional<Participant> resultat = dialogue.showAndWait();
        
        resultat.ifPresent(participant -> {
            listeParticipants.add(participant);
            afficherNotification("Participant ajouté : " + participant.getNom());
        });
    }
    
    //Affiche une boîte de dialogue pour ajouter un nouvel organisateur.

    private void afficherDialogueAjoutOrganisateur() {
        Dialog<Organisateur> dialogue = new Dialog<>();
        dialogue.setTitle("Ajouter un organisateur");
        dialogue.setHeaderText("Veuillez saisir les informations de l'organisateur");
        
        ButtonType btnConfirmer = new ButtonType("Confirmer", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnAnnuler = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialogue.getDialogPane().getButtonTypes().addAll(btnConfirmer, btnAnnuler);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        TextField tfNom = new TextField();
        TextField tfEmail = new TextField();
        
        grid.add(new Label("Nom :"), 0, 0);
        grid.add(tfNom, 1, 0);
        grid.add(new Label("Email :"), 0, 1);
        grid.add(tfEmail, 1, 1);
        
        dialogue.getDialogPane().setContent(grid);
        
        dialogue.setResultConverter(dialogButton -> {
            if (dialogButton == btnConfirmer) {
                String nom = tfNom.getText();
                String email = tfEmail.getText();
                
                if (nom.isEmpty() || email.isEmpty()) {
                    afficherNotification("Tous les champs sont obligatoires.");
                    return null;
                }
                
                return new Organisateur(nom, email);
            }
            return null;
        });
        
        Optional<Organisateur> resultat = dialogue.showAndWait();
        
        resultat.ifPresent(organisateur -> {
            listeParticipants.add(organisateur);
            afficherNotification("Organisateur ajouté : " + organisateur.getNom());
        });
    }
    
    //Supprime le participant sélectionné dans le tableau.
    private void supprimerParticipantSelectionne() {
        Participant participant = tableParticipants.getSelectionModel().getSelectedItem();
        
        if (participant == null) {
            afficherNotification("Aucun participant sélectionné.");
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer le participant");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer le participant : " + participant.getNom() + " ?");
        
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.isPresent() && result.get() == ButtonType.OK) {
            listeParticipants.remove(participant);
            afficherNotification("Participant supprimé : " + participant.getNom());
        }
    }
    
    //Crée un nouveau participant via une boîte de dialogue.
    private Participant creerNouveauParticipant() {
        Dialog<Participant> dialogue = new Dialog<>();
        dialogue.setTitle("Nouveau participant");
        dialogue.setHeaderText("Veuillez saisir les informations du nouveau participant");
        
        ButtonType btnConfirmer = new ButtonType("Confirmer", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnAnnuler = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialogue.getDialogPane().getButtonTypes().addAll(btnConfirmer, btnAnnuler);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        TextField tfNom = new TextField();
        TextField tfEmail = new TextField();
        
        grid.add(new Label("Nom :"), 0, 0);
        grid.add(tfNom, 1, 0);
        grid.add(new Label("Email :"), 0, 1);
        grid.add(tfEmail, 1, 1);
        
        dialogue.getDialogPane().setContent(grid);
        
        dialogue.setResultConverter(dialogButton -> {
            if (dialogButton == btnConfirmer) {
                String nom = tfNom.getText();
                String email = tfEmail.getText();
                
                if (nom.isEmpty() || email.isEmpty()) {
                    afficherNotification("Tous les champs sont obligatoires.");
                    return null;
                }
                
                Participant participant = new Participant(nom, email);
                listeParticipants.add(participant);
                return participant;
            }
            return null;
        });
        
        return dialogue.showAndWait().orElse(null);
    }
    
    /**
     * Actualise le tableau des événements avec les données du gestionnaire.
     */
    private void actualiserTableEvenements() {
        listeEvenements.clear();
        Map<String, Evenement> evenements = gestionEvenements.getEvenements();
        listeEvenements.addAll(evenements.values());
    }
    
    //Affiche une notification dans la zone de notifications.
    private void afficherNotification(String message) {
        String horodatage = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        zoneNotifications.appendText("[" + horodatage + "] " + message + "\n");
    }
}