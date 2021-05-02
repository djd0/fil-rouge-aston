package fr.bibliotheque.reservation.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ReservationAlreadyExistsException extends Exception {

    public ReservationAlreadyExistsException() {
    }

    public ReservationAlreadyExistsException(String message) {
        super(message);
    }

    public ReservationAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
