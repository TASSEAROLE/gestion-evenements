package fr.gestionevenements.modele;

//Interface pour le pattern Observer appliqué aux participants.
public interface ParticipantObserver {
   //Lorsqu'un évenement est mis a jour
    void mettreAJour(String message);
}