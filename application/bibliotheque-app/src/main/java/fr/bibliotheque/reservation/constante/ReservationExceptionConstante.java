package fr.bibliotheque.reservation.constante;

public class ReservationExceptionConstante {

    private ReservationExceptionConstante() throws IllegalAccessException { throw new IllegalAccessException("Utility class can't be instantiated"); }

    public static final String RESERVATION_REF_NOT_FOUND = "Aucune reservation n'a été trouvé pour la reference %d";
    public static final String RESERVATION_BAD_VALIDATING_DATE = "La date fournit : %s pour la reservation : %d n'est pas dans un format valide";
    public static final String RESERVATION_ALREADY_IN_PREPARE = "La reservation %d est déjà en préparation";
    public static final String RESERVATION_ALREADY_VALIDATE = "La reservation %d est déjà validée";
    public static final String RESERVATION_ALREADY_EXISTS = "La reservation %d existe déjà";
}
