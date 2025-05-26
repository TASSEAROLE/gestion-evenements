package fr.gestionevenements.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Classe utilitaire pour la manipulation des dates et heures.
 */
public class DateTimeUtils {
    
    // Format par défaut pour l'affichage des dates
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    // Format par défaut pour l'affichage des heures
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    
    // Format par défaut pour l'affichage des dates et heures
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    /**
     * Convertit une chaîne de caractères en LocalDateTime.
     * @param dateStr La chaîne de caractères représentant la date (format dd/MM/yyyy)
     * @param timeStr La chaîne de caractères représentant l'heure (format HH:mm)
     * @return L'objet LocalDateTime correspondant
     * @throws DateTimeParseException Si le format de la date ou de l'heure est incorrect
     */
    public static LocalDateTime parseDateTime(String dateStr, String timeStr) throws DateTimeParseException {
        LocalDate date = LocalDate.parse(dateStr, DATE_FORMATTER);
        LocalTime time = LocalTime.parse(timeStr, TIME_FORMATTER);
        return LocalDateTime.of(date, time);
    }
    
    /**
     * Convertit un LocalDateTime en chaîne de caractères formatée.
     * @param dateTime L'objet LocalDateTime à convertir
     * @return La chaîne de caractères formatée
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(DATETIME_FORMATTER);
    }
    
    /**
     * Extrait la date d'un LocalDateTime et la retourne au format chaîne de caractères.
     * @param dateTime L'objet LocalDateTime
     * @return La date au format chaîne de caractères
     */
    public static String formatDate(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(DATE_FORMATTER);
    }
    
    /**
     * Extrait l'heure d'un LocalDateTime et la retourne au format chaîne de caractères.
     * @param dateTime L'objet LocalDateTime
     * @return L'heure au format chaîne de caractères
     */
    public static String formatTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(TIME_FORMATTER);
    }
    
    /**
     * Vérifie si une date est future par rapport à maintenant.
     * @param dateTime La date à vérifier
     * @return true si la date est dans le futur, false sinon
     */
    public static boolean isFutureDate(LocalDateTime dateTime) {
        return dateTime != null && dateTime.isAfter(LocalDateTime.now());
    }
    
    /**
     * Vérifie si une date est comprise dans l'intervalle spécifié.
     * @param dateTime La date à vérifier
     * @param start Le début de l'intervalle
     * @param end La fin de l'intervalle
     * @return true si la date est dans l'intervalle, false sinon
     */
    public static boolean isDateInRange(LocalDateTime dateTime, LocalDateTime start, LocalDateTime end) {
        if (dateTime == null || start == null || end == null) {
            return false;
        }
        return !dateTime.isBefore(start) && !dateTime.isAfter(end);
    }
}