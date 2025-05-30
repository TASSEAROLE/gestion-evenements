package fr.gestionevenements.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateTimeUtils {
    
    // Format par défaut pour l'affichage des dates
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    // Format par défaut pour l'affichage des heures
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    
    // Format par défaut pour l'affichage des dates et heures
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    

    public static LocalDateTime parseDateTime(String dateStr, String timeStr) throws DateTimeParseException {
        LocalDate date = LocalDate.parse(dateStr, DATE_FORMATTER);
        LocalTime time = LocalTime.parse(timeStr, TIME_FORMATTER);
        return LocalDateTime.of(date, time);
    }
    

    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(DATETIME_FORMATTER);
    }
    

    public static String formatDate(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(DATE_FORMATTER);
    }
    
    //Extrait l'heure d'un LocalDateTime et la retourne au format chaîne de caractères.
    public static String formatTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(TIME_FORMATTER);
    }

    public static boolean isFutureDate(LocalDateTime dateTime) {
        return dateTime != null && dateTime.isAfter(LocalDateTime.now());
    }
    
    //Vérifie si une date est comprise dans l'intervalle spécifié.
    public static boolean isDateInRange(LocalDateTime dateTime, LocalDateTime start, LocalDateTime end) {
        if (dateTime == null || start == null || end == null) {
            return false;
        }
        return !dateTime.isBefore(start) && !dateTime.isAfter(end);
    }
}