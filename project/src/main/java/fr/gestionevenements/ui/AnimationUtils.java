package fr.gestionevenements.ui;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

/*Classe utilitaire pour les animations JavaFX.*/
public class AnimationUtils {
    
    /*Crée une animation de fondu.*/
    public static FadeTransition createFadeTransition(Node node, boolean fadeIn, int durationMs) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(durationMs), node);
        fadeTransition.setFromValue(fadeIn ? 0.0 : 1.0);
        fadeTransition.setToValue(fadeIn ? 1.0 : 0.0);
        return fadeTransition;
    }
    
    /* Crée une animation de mise à l'échelle.*/
    public static ScaleTransition createScaleTransition(Node node, double fromScale, double toScale, int durationMs) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(durationMs), node);
        scaleTransition.setFromX(fromScale);
        scaleTransition.setFromY(fromScale);
        scaleTransition.setToX(toScale);
        scaleTransition.setToY(toScale);
        return scaleTransition;
    }
    
    /*Crée une animation de translation.*/
    public static TranslateTransition createTranslateTransition(
            Node node, double fromX, double toX, double fromY, double toY, int durationMs) {
        TranslateTransition translateTransition = 
                new TranslateTransition(Duration.millis(durationMs), node);
        translateTransition.setFromX(fromX);
        translateTransition.setToX(toX);
        translateTransition.setFromY(fromY);
        translateTransition.setToY(toY);
        return translateTransition;
    }
    
    /*Applique une animation de (rebond) à un nœud*/
    public static void applyBounceEffect(Node node) {
        ScaleTransition scaleTransition = createScaleTransition(node, 1.0, 1.05, 200);
        scaleTransition.setCycleCount(2);
        scaleTransition.setAutoReverse(true);
        scaleTransition.play();
    }
    
    /*Applique une animation de fondu entrant à un nœud.*/
    public static void applyFadeInEffect(Node node) {
        node.setOpacity(0);
        FadeTransition fadeTransition = createFadeTransition(node, true, 500);
        fadeTransition.play();
    }
    
    /*Applique une animation de fondu sortant à un nœud.*/
    public static void applyFadeOutEffect(Node node) {
        FadeTransition fadeTransition = createFadeTransition(node, false, 500);
        fadeTransition.play();
    }
}