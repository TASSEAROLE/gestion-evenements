module systeme.gestion.evenements {
	// Modules JavaFX requis
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.base;
	requires javafx.graphics;

	// Modules Jackson pour la sérialisation JSON
	requires com.fasterxml.jackson.core;
	requires com.fasterxml.jackson.databind;
	requires com.fasterxml.jackson.datatype.jsr310;
	requires com.fasterxml.jackson.annotation;

	// Module Java de base
	requires java.base;
	requires java.desktop;

	// EXPORTS - Packages accessibles depuis l'extérieur
	exports fr.gestionevenements.ui;              // AJOUT CRITIQUE pour JavaFX
	exports fr.gestionevenements.gestionnaire;
	exports fr.gestionevenements.modele;
	exports fr.gestionevenements.serialisation;

	// OPENS - Packages ouverts pour la réflexion (nécessaire pour Jackson et JavaFX)
	opens fr.gestionevenements.ui to
			javafx.fxml,
			javafx.graphics,
			javafx.base;

	opens fr.gestionevenements.modele to
			com.fasterxml.jackson.databind,
			com.fasterxml.jackson.core,
			com.fasterxml.jackson.datatype.jsr310,
			javafx.base;

	opens fr.gestionevenements.gestionnaire to
			com.fasterxml.jackson.databind,
			javafx.base;

	opens fr.gestionevenements.serialisation to
			com.fasterxml.jackson.databind,
			javafx.base;
}