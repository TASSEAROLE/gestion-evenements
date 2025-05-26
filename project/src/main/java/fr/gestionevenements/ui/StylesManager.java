package fr.gestionevenements.ui;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.Region;

/**
 * Classe utilitaire pour la gestion des styles CSS de l'interface utilisateur.
 * Centralise les styles pour maintenir une apparence cohérente.
 */
public class StylesManager {
    
    // Constantes de couleurs
    private static final String COULEUR_PRIMAIRE = "#3498db";
    private static final String COULEUR_SECONDAIRE = "#2ecc71";
    private static final String COULEUR_ALERTE = "#e74c3c";
    private static final String COULEUR_TEXTE = "#2c3e50";
    private static final String COULEUR_FOND = "#ecf0f1";
    
    //Applique le style primaire à un bouton.
    public static void appliquerStyleBoutonPrimaire(Button bouton) {
        bouton.setStyle(
            "-fx-background-color: " + COULEUR_PRIMAIRE + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 8 15 8 15;" +
            "-fx-background-radius: 4;"
        );
        
        // Ajouter des effets hover avec des gestionnaires d'événements
        bouton.setOnMouseEntered(e -> 
            bouton.setStyle(
                "-fx-background-color: derive(" + COULEUR_PRIMAIRE + ", 20%);" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 8 15 8 15;" +
                "-fx-background-radius: 4;"
            )
        );
        
        bouton.setOnMouseExited(e -> 
            bouton.setStyle(
                "-fx-background-color: " + COULEUR_PRIMAIRE + ";" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 8 15 8 15;" +
                "-fx-background-radius: 4;"
            )
        );
    }
    
    //Applique le style secondaire à un bouton.
    public static void appliquerStyleBoutonSecondaire(Button bouton) {
        bouton.setStyle(
            "-fx-background-color: " + COULEUR_SECONDAIRE + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 8 15 8 15;" +
            "-fx-background-radius: 4;"
        );
        
        bouton.setOnMouseEntered(e -> 
            bouton.setStyle(
                "-fx-background-color: derive(" + COULEUR_SECONDAIRE + ", 20%);" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 8 15 8 15;" +
                "-fx-background-radius: 4;"
            )
        );
        
        bouton.setOnMouseExited(e -> 
            bouton.setStyle(
                "-fx-background-color: " + COULEUR_SECONDAIRE + ";" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 8 15 8 15;" +
                "-fx-background-radius: 4;"
            )
        );
    }
    
    //Applique le style d'alerte à un bouton.
    public static void appliquerStyleBoutonAlerte(Button bouton) {
        bouton.setStyle(
            "-fx-background-color: " + COULEUR_ALERTE + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 8 15 8 15;" +
            "-fx-background-radius: 4;"
        );
        
        bouton.setOnMouseEntered(e -> 
            bouton.setStyle(
                "-fx-background-color: derive(" + COULEUR_ALERTE + ", 20%);" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 8 15 8 15;" +
                "-fx-background-radius: 4;"
            )
        );
        
        bouton.setOnMouseExited(e -> 
            bouton.setStyle(
                "-fx-background-color: " + COULEUR_ALERTE + ";" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 8 15 8 15;" +
                "-fx-background-radius: 4;"
            )
        );
    }
    
    /**
     * Applique le style de titre à un label.
     * @param label Le label à styliser
     */
    public static void appliquerStyleTitre(Label label) {
        label.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + COULEUR_TEXTE + ";"
        );
    }
    
    //Applique le style de sous-titre à un label.
    public static void appliquerStyleSousTitre(Label label) {
        label.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + COULEUR_TEXTE + ";"
        );
    }
    
    //Applique des styles à un tableau pour améliorer son apparence.
    public static <T> void appliquerStyleTableau(TableView<T> tableau) {
        tableau.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: " + COULEUR_PRIMAIRE + ";" +
            "-fx-border-width: 1px;" +
            "-fx-border-radius: 4px;" +
            "-fx-padding: 0;"
        );
        
        // Style pour les en-têtes de colonnes
        tableau.lookup(".column-header-background").setStyle(
            "-fx-background-color: " + COULEUR_PRIMAIRE + ";" +
            "-fx-background-radius: 4px 4px 0px 0px;"
        );
        
        tableau.lookup(".column-header").setStyle(
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;"
        );
    }
    
    //Applique le style de conteneur à un nœud ou une région.
    public static void appliquerStyleConteneur(Node node) {
        if (node instanceof Region) {
            ((Region) node).setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: lightgray;" +
                "-fx-border-width: 1px;" +
                "-fx-border-radius: 4px;" +
                "-fx-padding: 10px;" +
                "-fx-background-radius: 4px;"
            );
        }
    }
    
    //Applique un style d'ombre à un nœud ou une région.
    public static void appliquerOmbre(Node node) {
        node.setStyle(node.getStyle() + 
            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 10, 0, 0, 2);"
        );
    }
}