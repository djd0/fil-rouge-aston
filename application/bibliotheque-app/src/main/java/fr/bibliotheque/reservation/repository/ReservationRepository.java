package fr.bibliotheque.reservation.repository;

import fr.bibliotheque.reservation.model.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Optional<Reservation> findByReference(long reference);

    @Transactional
    void deleteByReference(long reference);

    Page<Reservation> findByReference(Pageable pageable, long reference);

    Page<Reservation> findByClientNomContainingIgnoreCase(Pageable pageable, String genre);

    @Query("SELECT r from Reservation r where r.dateReservation >= CURRENT_DATE and r.dateReservation <= CURRENT_DATE +1 and r.dateRetrait is null order by r.dateReservation asc")
    Page<Reservation> findDailyReservationsOrderByTime(Pageable pageable);
}
