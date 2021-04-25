package fr.bibliotheque.reservation.controller;

import fr.bibliotheque.reservation.constante.ReservationExceptionConstante;
import fr.bibliotheque.reservation.dto.ReservationDTO;
import fr.bibliotheque.reservation.exception.ReservationAlreadyInPrepareException;
import fr.bibliotheque.reservation.exception.ReservationAlreadyValidateException;
import fr.bibliotheque.reservation.exception.ReservationNotFoundException;
import fr.bibliotheque.reservation.exception.ReservationValidationException;
import fr.bibliotheque.reservation.service.IReservationService;
import fr.bibliotheque.reservation.validator.ReservationValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("api/reservations")
public class ReservationController {

    private final IReservationService reservationService;
    private final ReservationValidator reservationValidator;


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> getAllReservations(@RequestParam(name = "page", defaultValue = "0") int page,
                                                  @RequestParam(name = "size", defaultValue = "5") int size) {
        log.debug("Get all reservations");
        return this.reservationService.getAllReservations(page, size);
    }

    @GetMapping(value = "/{reference}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ReservationDTO getReservation(@PathVariable long reference) {

        log.debug(String.format("Get reservation with reference : %d", reference));

        try {
            return this.reservationService.getReservation(reference);

        } catch(ReservationNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format(ReservationExceptionConstante.RESERVATION_REF_NOT_FOUND, reference),
                    e);
        }
    }

    @GetMapping(value="/daily", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> getReservationDuJour(@RequestParam(name = "page", defaultValue = "0") int page,
                                                  @RequestParam(name = "size", defaultValue = "5") int size) {

        log.debug("Get daily reservations");
        return this.reservationService.getDailyReservations(page, size);
    }

    @PatchMapping(value = "/validate/{reference}")
    public long validateReservation(@PathVariable long reference,
                                    @RequestBody String validatingDate) {

        log.debug(String.format("Validate reservation with reference : %d at : %s", reference, validatingDate));

        try {
            reservationValidator.validateReservationValidatingDate(reference, validatingDate);
            return this.reservationService.validateReservation(reference, validatingDate);

        } catch(ReservationValidationException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage(),
                    e);

        } catch(ReservationNotFoundException e) {
            log.error(String.format(ReservationExceptionConstante.RESERVATION_REF_NOT_FOUND, reference), e);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format(ReservationExceptionConstante.RESERVATION_REF_NOT_FOUND, reference),
                    e);

        } catch(ReservationAlreadyValidateException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    e.getMessage(),
                    e);

        }
    }

    @PatchMapping(value = "/prepare/{reference}")
    public long prepareReservation(@PathVariable long reference) {

        log.debug(String.format("Prepare reservation with reference : %d", reference));

        try {
            return this.reservationService.prepareReservation(reference);

        } catch(ReservationNotFoundException e) {
            log.error(String.format(ReservationExceptionConstante.RESERVATION_REF_NOT_FOUND, reference), e);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format(ReservationExceptionConstante.RESERVATION_REF_NOT_FOUND, reference),
                    e);

        } catch(ReservationAlreadyInPrepareException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    e.getMessage(),
                    e);

        }
    }

    @DeleteMapping(value = "/{reference}")
    public void deleteReservation(@PathVariable long reference) {

        log.debug(String.format("Delete reservation with reference : %d", reference));

        try {
            this.reservationService.deleteReservation(reference);

        }  catch(ReservationNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    e.getMessage(),
                    e);
        }


    }
}
