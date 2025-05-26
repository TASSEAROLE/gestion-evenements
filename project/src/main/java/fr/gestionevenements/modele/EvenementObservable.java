package fr.gestionevenements.modele;


public interface EvenementObservable {

    void ajouterObservateur(ParticipantObserver observer);
    

    void supprimerObservateur(ParticipantObserver observer);
    

    void notifierObservateurs(String message);
}