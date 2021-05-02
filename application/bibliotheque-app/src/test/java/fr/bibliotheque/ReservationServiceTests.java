package fr.bibliotheque;

import fr.bibliotheque.client.model.Client;
import fr.bibliotheque.livre.model.Livre;
import fr.bibliotheque.livre.service.ILivreService;
import fr.bibliotheque.reservation.dto.ReservationDTO;
import fr.bibliotheque.reservation.exception.ReservationAlreadyExistsException;
import fr.bibliotheque.reservation.exception.ReservationAlreadyInPrepareException;
import fr.bibliotheque.reservation.exception.ReservationAlreadyValidateException;
import fr.bibliotheque.reservation.exception.ReservationNotFoundException;
import fr.bibliotheque.reservation.mapper.ReservationMapper;
import fr.bibliotheque.reservation.model.Reservation;
import fr.bibliotheque.reservation.service.IReservationService;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@RunWith(SpringRunner.class)
@SpringBootTest
class ReservationServiceTests {


    private final IReservationService reservationService;
    private final ILivreService livreService;
    private final ReservationMapper reservationMapper;


    @BeforeEach
    void setup() {
        reservationService.deleteAll();
        livreService.deleteAll();
    }

    @Test
    void getAllReservations() throws ReservationAlreadyExistsException {

        List<Livre> livres = new ArrayList<>();
        livres.add(Livre.builder()
                .reference(1)
                .titre("test")
                .auteur("test")
                .build()
        );

        Client client = Client.builder()
                .reference(1)
                .nom("Jean")
                .prenom("Claude")
                .numeroTel("0123456789")
                .email("jean@claude.fr")
                .build();

        ReservationDTO reservation1 = ReservationDTO.builder()
                .reference(1)
                .dateReservation(reservationMapper.dateTimeToString(LocalDateTime.now()))
                .enPreparation(false)
                .livres(livres)
                .client(client)
                .build();

        ReservationDTO reservation2 = ReservationDTO.builder()
                .reference(2)
                .dateReservation(reservationMapper.dateTimeToString(LocalDateTime.now()))
                .enPreparation(false)
                .livres(livres)
                .client(client)
                .build();

        reservationService.addReservation(reservation1);
        reservationService.addReservation(reservation2);
        List<Reservation> reservations = (List<Reservation>) reservationService.getAllReservations(0, 5).getOrDefault("reservations", null);
        assertNotNull(reservations);
        assertEquals(2, reservations.size());
        reservationService.deleteAll();
        livreService.deleteAll();
    }

    @Test
    void getReservation() throws ReservationNotFoundException, ReservationAlreadyExistsException {

        List<Livre> livres = new ArrayList<>();
        livres.add(Livre.builder()
                .reference(1)
                .titre("test")
                .auteur("test")
                .build()
        );

        Client client = Client.builder()
                .reference(1)
                .nom("Jean")
                .prenom("Claude")
                .numeroTel("0123456789")
                .email("jean@claude.fr")
                .build();

        ReservationDTO reservation = ReservationDTO.builder()
                .reference(1)
                .dateReservation(reservationMapper.dateTimeToString(LocalDateTime.now()))
                .livres(livres)
                .client(client)
                .build();

        long id = reservationService.addReservation(reservation);
        ReservationDTO result = reservationService.getReservation(id);
        assertNotNull(result);
        reservationService.deleteAll();
        livreService.deleteAll();
    }

    @Test
    void getReservationDuJour() throws ReservationAlreadyExistsException {

        List<Livre> livres = new ArrayList<>();
        livres.add(Livre.builder()
                .reference(1)
                .titre("test")
                .auteur("test")
                .build()
        );

        Client client = Client.builder()
                .reference(1)
                .nom("Jean")
                .prenom("Claude")
                .numeroTel("0123456789")
                .email("jean@claude.fr")
                .build();

        ReservationDTO reservation1 = ReservationDTO.builder()
                .reference(1)
                .dateReservation(reservationMapper.dateTimeToString(LocalDateTime.now()))
                .livres(livres)
                .client(client)
                .build();

        ReservationDTO reservation2 = ReservationDTO.builder()
                .reference(2)
                .dateReservation("01/01/2021 00:00")
                .livres(livres)
                .client(client)
                .build();

        ReservationDTO reservation3 = ReservationDTO.builder()
                .reference(3)
                .dateReservation(reservationMapper.dateTimeToString(LocalDateTime.now()))
                .livres(livres)
                .client(client)
                .build();

        long id1 = reservationService.addReservation(reservation1);
        reservationService.addReservation(reservation2);
        long id3 = reservationService.addReservation(reservation3);
        List<ReservationDTO> reservations = (List<ReservationDTO>) reservationService.getDailyReservations(0, 5).getOrDefault("reservations", null);
        assertNotNull(reservations);
        assertEquals(2, reservations.size());
        assertEquals(id1, reservations.get(0).getReference());
        assertEquals(id3, reservations.get(1).getReference());
        reservationService.deleteAll();
        livreService.deleteAll();
    }

    @Test
    void addReservation() throws ReservationAlreadyExistsException, ReservationNotFoundException {

        List<Livre> livres = new ArrayList<>();
        livres.add(Livre.builder()
                .reference(1)
                .titre("test")
                .auteur("test")
                .build()
        );

        Client client = Client.builder()
                .reference(1)
                .nom("Jean")
                .prenom("Claude")
                .numeroTel("0123456789")
                .email("jean@claude.fr")
                .build();

        ReservationDTO reservation = ReservationDTO.builder()
                .reference(1)
                .dateReservation("01/01/2021 00:00")
                .livres(livres)
                .client(client)
                .build();

        long id = reservationService.addReservation(reservation);
        ReservationDTO result = reservationService.getReservation(id);
        assertNotNull(result);
        assertEquals(id, result.getReference());
        reservationService.deleteAll();
        livreService.deleteAll();
    }

    @Test
    void validateReservation() throws ReservationNotFoundException, ReservationAlreadyValidateException, ReservationAlreadyExistsException {

        List<Livre> livres = new ArrayList<>();
        livres.add(Livre.builder()
                .reference(1)
                .titre("test")
                .auteur("test")
                .build()
        );

        Client client = Client.builder()
                .reference(1)
                .nom("Jean")
                .prenom("Claude")
                .numeroTel("0123456789")
                .email("jean@claude.fr")
                .build();

        ReservationDTO reservation = ReservationDTO.builder()
                .reference(1)
                .dateReservation(reservationMapper.dateTimeToString(LocalDateTime.now()))
                .livres(livres)
                .client(client)
                .build();

        long id = reservationService.addReservation(reservation);
        reservationService.validateReservation(id, "01/01/2021");
        ReservationDTO result = reservationService.getReservation(id);
        assertNotNull(result.getDateRetrait());
        assertEquals("01/01/2021", result.getDateRetrait());
        reservationService.deleteAll();
        livreService.deleteAll();
    }

    @Test
    void prepareReservation() throws ReservationNotFoundException, ReservationAlreadyInPrepareException, ReservationAlreadyExistsException {

        List<Livre> livres = new ArrayList<>();
        livres.add(Livre.builder()
                .reference(1)
                .titre("test")
                .auteur("test")
                .build()
        );

        Client client = Client.builder()
                .reference(1)
                .nom("Jean")
                .prenom("Claude")
                .numeroTel("0123456789")
                .email("jean@claude.fr")
                .build();

        ReservationDTO reservation = ReservationDTO.builder()
                .reference(1)
                .dateReservation(reservationMapper.dateTimeToString(LocalDateTime.now()))
                .enPreparation(false)
                .livres(livres)
                .client(client)
                .build();

        long id = reservationService.addReservation(reservation);
        reservationService.prepareReservation(id);
        ReservationDTO result = reservationService.getReservation(id);
        assertTrue(result.isEnPreparation());
        reservationService.deleteAll();
        livreService.deleteAll();
    }

    @Test
    void deleteReservation() throws ReservationAlreadyExistsException, ReservationNotFoundException {

        List<Livre> livres = new ArrayList<>();
        livres.add(Livre.builder()
                .reference(1)
                .titre("test")
                .auteur("test")
                .build()
        );

        Client client = Client.builder()
                .reference(1)
                .nom("Jean")
                .prenom("Claude")
                .numeroTel("0123456789")
                .email("jean@claude.fr")
                .build();

        ReservationDTO reservation = ReservationDTO.builder()
                .reference(1)
                .dateReservation(reservationMapper.dateTimeToString(LocalDateTime.now()))
                .enPreparation(false)
                .livres(livres)
                .client(client)
                .build();

        long id = reservationService.addReservation(reservation);
        reservationService.deleteReservation(id);

        assertThrows(ReservationNotFoundException.class, () -> reservationService.getReservation(id));
        reservationService.deleteAll();
        livreService.deleteAll();
    }

    @Test
    void deleteAllReservations() throws ReservationAlreadyExistsException {

        List<Livre> livres = new ArrayList<>();
        livres.add(Livre.builder()
                .reference(1)
                .titre("test")
                .auteur("test")
                .build()
        );

        Client client = Client.builder()
                .reference(1)
                .nom("Jean")
                .prenom("Claude")
                .numeroTel("0123456789")
                .email("jean@claude.fr")
                .build();

        ReservationDTO reservation1 = ReservationDTO.builder()
                .reference(1)
                .dateReservation(reservationMapper.dateTimeToString(LocalDateTime.now()))
                .enPreparation(false)
                .livres(livres)
                .client(client)
                .build();

        ReservationDTO reservation2 = ReservationDTO.builder()
                .reference(1)
                .dateReservation(reservationMapper.dateTimeToString(LocalDateTime.now()))
                .enPreparation(false)
                .livres(livres)
                .client(client)
                .build();

        reservationService.addReservation(reservation1);
        reservationService.addReservation(reservation2);
        reservationService.deleteAll();
        List<Reservation> reservations = (List<Reservation>) reservationService.getAllReservations(0, 5).getOrDefault("reservations", null);
        assertEquals(0, reservations.size());
        reservationService.deleteAll();
        livreService.deleteAll();
    }


    @Test
    void reservationIsNotFound() {

        assertThrows(ReservationNotFoundException.class, () -> {

            List<Livre> livres = new ArrayList<>();
            livres.add(Livre.builder()
                    .reference(1)
                    .titre("test")
                    .auteur("test")
                    .build()
            );

            Client client = Client.builder()
                    .reference(1)
                    .nom("Jean")
                    .prenom("Claude")
                    .numeroTel("0123456789")
                    .email("jean@claude.fr")
                    .build();

            ReservationDTO reservation = ReservationDTO.builder()
                    .reference(1)
                    .dateReservation(reservationMapper.dateTimeToString(LocalDateTime.now()))
                    .enPreparation(false)
                    .livres(livres)
                    .client(client)
                    .build();

            reservationService.deleteReservation(reservation.getReference());
        });
    }

    @Test
    void reservationAlreadyValidate() {

        assertThrows(ReservationAlreadyValidateException.class, () -> {

            List<Livre> livres = new ArrayList<>();
            livres.add(Livre.builder()
                    .reference(1)
                    .titre("test")
                    .auteur("test")
                    .build()
            );

            Client client = Client.builder()
                    .reference(1)
                    .nom("Jean")
                    .prenom("Claude")
                    .numeroTel("0123456789")
                    .email("jean@claude.fr")
                    .build();

            ReservationDTO reservation = ReservationDTO.builder()
                    .reference(1)
                    .dateReservation(reservationMapper.dateTimeToString(LocalDateTime.now()))
                    .enPreparation(false)
                    .dateRetrait("01/01/2021")
                    .livres(livres)
                    .client(client)
                    .build();

            long id = reservationService.addReservation(reservation);
            reservationService.validateReservation(id, "01/01/2021");
        });
        reservationService.deleteAll();
        livreService.deleteAll();
    }

    @Test
    void reservationAlreadyInPrepare() {

        assertThrows(ReservationAlreadyInPrepareException.class, () -> {

            List<Livre> livres = new ArrayList<>();
            livres.add(Livre.builder()
                    .reference(1)
                    .titre("test")
                    .auteur("test")
                    .build()
            );

            Client client = Client.builder()
                    .reference(1)
                    .nom("Jean")
                    .prenom("Claude")
                    .numeroTel("0123456789")
                    .email("jean@claude.fr")
                    .build();

            ReservationDTO reservation = ReservationDTO.builder()
                    .reference(1)
                    .dateReservation(reservationMapper.dateTimeToString(LocalDateTime.now()))
                    .enPreparation(true)
                    .livres(livres)
                    .client(client)
                    .build();

            long id = reservationService.addReservation(reservation);
            reservationService.prepareReservation(id);
        });
        reservationService.deleteAll();
        livreService.deleteAll();
    }
}
