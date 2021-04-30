package fr.bibliotheque.reservation.mapper;

import fr.bibliotheque.reservation.constante.Common;
import fr.bibliotheque.reservation.dto.ReservationDTO;
import fr.bibliotheque.reservation.model.Reservation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class ReservationMapper {

    public Reservation mapReservationWithValidatingDate(Reservation reservation, String validatingDate) {

        reservation.setDateRetrait(LocalDate.parse(validatingDate, DateTimeFormatter.ofPattern(Common.DATE_FORMAT)));
        return reservation;
    }

    public ReservationDTO mapReservationToDTO(Reservation reservation) {

        return ReservationDTO.builder()
                .reference(reservation.getReference())
                .dateReservation(this.dateTimeToString(reservation.getDateReservation()))
                .dateRetrait(reservation.getDateRetrait() == null ? null : this.dateToString(reservation.getDateRetrait()))
                .enPreparation(reservation.isEnPreparation())
                .livres(reservation.getLivres())
                .client(reservation.getClient())
                .build();
    }

    public Reservation mapReservationDTOToReservation(ReservationDTO dto) {

        return Reservation.builder()
                .reference(dto.getReference())
                .dateReservation(LocalDateTime.parse(dto.getDateReservation(), DateTimeFormatter.ofPattern(Common.DATE_FORMAT)))
                .enPreparation(dto.isEnPreparation())
                .dateRetrait(LocalDate.parse(dto.getDateRetrait(), DateTimeFormatter.ofPattern(Common.DATE_FORMAT)))
                .livres(dto.getLivres())
                .client(dto.getClient())
                .build();
    }

    public Reservation mapPrepareReservation(Reservation reservation) {

        reservation.setEnPreparation(true);
        return reservation;
    }

    private String dateToString(LocalDate date) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Common.DATE_FORMAT);
        return date.format(formatter);
    }

    public String dateTimeToString(LocalDateTime date) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return date.format(formatter);
    }
}
