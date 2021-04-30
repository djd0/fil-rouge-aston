package fr.bibliotheque.reservation.service;

import fr.bibliotheque.reservation.constante.ReservationExceptionConstante;
import fr.bibliotheque.reservation.dto.ReservationDTO;
import fr.bibliotheque.reservation.exception.ReservationAlreadyExistsException;
import fr.bibliotheque.reservation.exception.ReservationAlreadyInPrepareException;
import fr.bibliotheque.reservation.exception.ReservationAlreadyValidateException;
import fr.bibliotheque.reservation.exception.ReservationNotFoundException;
import fr.bibliotheque.reservation.mapper.ReservationMapper;
import fr.bibliotheque.reservation.model.Reservation;
import fr.bibliotheque.reservation.repository.ReservationRepository;
import fr.bibliotheque.reservation.validator.ReservationValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class ReservationServiceImpl implements IReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    private final ReservationValidator reservationValidator;


    @Override
    public ReservationDTO getReservation(long reference) throws ReservationNotFoundException {
        return this.reservationRepository.findByReference(reference)
                .map(this.reservationMapper::mapReservationToDTO)
                .orElseThrow(ReservationNotFoundException::new);
    }

    @Override
    public Map<String, Object> getAllReservations(int page, int size) {

        Pageable paging = PageRequest.of(page, size);
        Page<Reservation> pageReservations = reservationRepository.findAll(paging);

        List<ReservationDTO> reservations = pageReservations.getContent()
                .stream()
                .map(this.reservationMapper::mapReservationToDTO)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("reservations", reservations);
        response.put("currentPage", pageReservations.getNumber());
        response.put("totalItems", pageReservations.getTotalElements());
        response.put("totalPages", pageReservations.getTotalPages());

        return response;
    }

    @Override
    public void addReservation(ReservationDTO reservation) throws ReservationAlreadyExistsException {

        reservationValidator.isReservationExists(reservation);
        this.reservationRepository.save(reservationMapper.mapReservationDTOToReservation(reservation));
    }

    @Override
    public long validateReservation(long reference, String validatingDate) throws ReservationNotFoundException, ReservationAlreadyValidateException {

        Reservation reservation = this.reservationRepository.findByReference(reference)
                .orElseThrow(ReservationNotFoundException::new);

        if(reservation.getDateRetrait() != null) {
            log.error(String.format(ReservationExceptionConstante.RESERVATION_ALREADY_VALIDATE, reference));
            throw new ReservationAlreadyValidateException(String.format(ReservationExceptionConstante.RESERVATION_ALREADY_VALIDATE, reference));
        }

        log.debug("Reservation found, validating");
        return this.reservationRepository
                .save(reservationMapper.mapReservationWithValidatingDate(reservation, validatingDate))
                .getReference();
    }

    @Override
    public void deleteReservation(long reference) throws ReservationNotFoundException {

        try {
            this.getReservation(reference);

        } catch (ReservationNotFoundException e) {
            log.error(String.format(ReservationExceptionConstante.RESERVATION_REF_NOT_FOUND, reference));
            throw new ReservationNotFoundException(String.format(ReservationExceptionConstante.RESERVATION_REF_NOT_FOUND, reference), e);
        }

        log.debug("Reservation found, deleting");
        this.reservationRepository.deleteByReference(reference);

    }

    public Map<String, Object> getDailyReservations(int page, int size) {

        Pageable paging = PageRequest.of(page, size);
        Page<Reservation> pageReservations = reservationRepository.findDailyReservationsOrderByTime(paging);

        List<ReservationDTO> reservations = pageReservations.getContent()
                .stream()
                .map(this.reservationMapper::mapReservationToDTO)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("reservations", reservations);
        response.put("currentPage", pageReservations.getNumber());
        response.put("totalItems", pageReservations.getTotalElements());
        response.put("totalPages", pageReservations.getTotalPages());

        return response;
    }

    @Override
    public long prepareReservation(long reference) throws ReservationNotFoundException, ReservationAlreadyInPrepareException {

        Reservation reservation = this.reservationRepository.findByReference(reference)
                .orElseThrow(ReservationNotFoundException::new);

        if(reservation.isEnPreparation()) {
            log.error(String.format(ReservationExceptionConstante.RESERVATION_ALREADY_IN_PREPARE, reference));
            throw new ReservationAlreadyInPrepareException(String.format(ReservationExceptionConstante.RESERVATION_ALREADY_IN_PREPARE, reference));
        }

        log.debug("Reservation found, validating");
        return this.reservationRepository
                .save(reservationMapper.mapPrepareReservation(reservation))
                .getReference();
    }

    @Override
    public void deleteAll() {

        this.reservationRepository.deleteAll();
    }
}
