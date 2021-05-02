package fr.bibliotheque.reservation.validator;

import fr.bibliotheque.reservation.constante.ReservationExceptionConstante;
import fr.bibliotheque.reservation.dto.ReservationDTO;
import fr.bibliotheque.reservation.exception.ReservationAlreadyExistsException;
import fr.bibliotheque.reservation.exception.ReservationNotFoundException;
import fr.bibliotheque.reservation.exception.ReservationValidationException;
import fr.bibliotheque.reservation.model.Reservation;
import fr.bibliotheque.reservation.repository.ReservationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class ReservationValidator {

    private final ReservationRepository reservationRepository;


    public void validateReservationValidatingDate(long reference, String validatingDate) throws ReservationNotFoundException, ReservationValidationException {

        log.debug("Validate reservation validation date [start]");

        try {

            // ResolverStyle.STRICT for 30, 31 days checking, and also leap year.
            LocalDate.parse(validatingDate,
                    DateTimeFormatter.ofPattern("dd/MM/uuuu")
                            .withResolverStyle(ResolverStyle.STRICT)
            );

        } catch (DateTimeParseException e) {
            log.error(String.format(ReservationExceptionConstante.RESERVATION_BAD_VALIDATING_DATE, validatingDate, reference));
            throw new ReservationValidationException(String.format(ReservationExceptionConstante.RESERVATION_BAD_VALIDATING_DATE, validatingDate, reference), e);
        }
        log.debug("Validate reservation validation date [end]");
    }

    public void isReservationExists(ReservationDTO dto) throws ReservationAlreadyExistsException {

        log.debug("Checking if reservation already exists [start]");

        Reservation reservation = this.reservationRepository.findById(dto.getReference()).orElse(null);

        if(reservation != null) {
            log.error(String.format(ReservationExceptionConstante.RESERVATION_ALREADY_EXISTS, dto.getReference()));
            throw new ReservationAlreadyExistsException(String.format(ReservationExceptionConstante.RESERVATION_ALREADY_EXISTS, dto.getReference()));
        }

        log.debug("Checking if reservation already exists [end]");
    }
}
