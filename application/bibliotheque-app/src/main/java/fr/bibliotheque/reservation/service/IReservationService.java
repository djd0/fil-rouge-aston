package fr.bibliotheque.reservation.service;

import fr.bibliotheque.reservation.dto.ReservationDTO;
import fr.bibliotheque.reservation.exception.ReservationAlreadyExistsException;
import fr.bibliotheque.reservation.exception.ReservationAlreadyInPrepareException;
import fr.bibliotheque.reservation.exception.ReservationAlreadyValidateException;
import fr.bibliotheque.reservation.exception.ReservationNotFoundException;

import java.util.Map;

public interface IReservationService  {

    ReservationDTO getReservation(long reference) throws ReservationNotFoundException;

    Map<String, Object> getAllReservations(int page, int size);

    void addReservation(ReservationDTO reservation) throws ReservationAlreadyExistsException;

    long validateReservation(long reference, String validatingDate) throws ReservationNotFoundException, ReservationAlreadyValidateException;

    void deleteReservation(long reference) throws ReservationNotFoundException;

    Map<String, Object> getDailyReservations(int page, int size);

    long prepareReservation(long reference) throws ReservationNotFoundException, ReservationAlreadyInPrepareException;

    void deleteAll();
}
