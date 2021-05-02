//package fr.bibliotheque;
//
//import fr.bibliotheque.client.model.Client;
//import fr.bibliotheque.livre.model.Livre;
//import fr.bibliotheque.reservation.dto.ReservationDTO;
//import fr.bibliotheque.reservation.exception.ReservationAlreadyExistsException;
//import fr.bibliotheque.reservation.exception.ReservationAlreadyInPrepareException;
//import fr.bibliotheque.reservation.exception.ReservationAlreadyValidateException;
//import fr.bibliotheque.reservation.exception.ReservationNotFoundException;
//import fr.bibliotheque.reservation.mapper.ReservationMapper;
//import fr.bibliotheque.reservation.model.Reservation;
//import fr.bibliotheque.reservation.service.IReservationService;
//import lombok.AllArgsConstructor;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@AllArgsConstructor(onConstructor = @__(@Autowired))
//@RunWith(SpringRunner.class)
//@SpringBootTest
//class ReservationServiceTests {
//
//
//    @Autowired
//    private final IReservationService reservationService;
//    private final ReservationMapper reservationMapper;
//
//
//    @BeforeEach
//    void setup() {
//        reservationService.deleteAll();
//    }
//
//    @Test
//    void getAllReservations() throws ReservationAlreadyExistsException {
//
//        List<Livre> livres = new ArrayList<>();
//        livres.add(Livre.builder()
//                .titre("test")
//                .auteur("test")
//                .build()
//        );
//
//        Client client = Client.builder()
//                .nom("Jean")
//                .prenom("Claude")
//                .numeroTel("0123456789")
//                .email("jean@claude.fr")
//                .build();
//
//        ReservationDTO reservation1 = ReservationDTO.builder()
//                .reference(1)
//                .dateReservation(reservationMapper.dateTimeToString(LocalDateTime.now()))
//                .enPreparation(false)
//                .livres(livres)
//                .client(client)
//                .build();
//
//        ReservationDTO reservation2 = ReservationDTO.builder()
//                .reference(2)
//                .dateReservation(reservationMapper.dateTimeToString(LocalDateTime.now()))
//                .enPreparation(false)
//                .livres(livres)
//                .client(client)
//                .build();
//
//        reservationService.addReservation(reservation1);
//        reservationService.addReservation(reservation2);
//        List<Reservation> reservations = (List<Reservation>) reservationService.getAllReservations(0, 5).getOrDefault("reservations", null);
//        assertNotNull(reservations);
//        assertEquals(2, reservations.size());
//    }
//
//    @Test
//    void getReservation() throws ReservationNotFoundException, ReservationAlreadyExistsException {
//
//        List<Livre> livres = new ArrayList<>();
//        livres.add(Livre.builder()
//                .titre("test")
//                .auteur("test")
//                .build()
//        );
//
//        Client client = Client.builder()
//                .nom("Jean")
//                .prenom("Claude")
//                .numeroTel("0123456789")
//                .email("jean@claude.fr")
//                .build();
//
//        ReservationDTO reservation = ReservationDTO.builder()
//                .reference(1)
//                .dateReservation(reservationMapper.dateTimeToString(LocalDateTime.now()))
//                .livres(livres)
//                .client(client)
//                .build();
//
//        reservationService.addReservation(reservation);
//        ReservationDTO result = reservationService.getReservation(reservation.getReference());
//        assertNotNull(result);
//    }
//
//    @Test
//    void getReservationDuJour() throws ReservationAlreadyExistsException {
//
//        List<Livre> livres = new ArrayList<>();
//        livres.add(Livre.builder()
//                .titre("test")
//                .auteur("test")
//                .build()
//        );
//
//        Client client = Client.builder()
//                .nom("Jean")
//                .prenom("Claude")
//                .numeroTel("0123456789")
//                .email("jean@claude.fr")
//                .build();
//
//        ReservationDTO reservation1 = ReservationDTO.builder()
//                .reference(1)
//                .dateReservation(reservationMapper.dateTimeToString(LocalDateTime.now()))
//                .livres(livres)
//                .client(client)
//                .build();
//
//        ReservationDTO reservation2 = ReservationDTO.builder()
//                .reference(2)
//                .dateReservation(reservationMapper.dateTimeToString(LocalDateTime.now()))
//                .livres(livres)
//                .client(client)
//                .build();
//
//        ReservationDTO reservation3 = ReservationDTO.builder()
//                .reference(3)
//                .dateReservation("01/01/2021")
//                .livres(livres)
//                .client(client)
//                .build();
//
//        reservationService.addReservation(reservation1);
//        reservationService.addReservation(reservation2);
//        reservationService.addReservation(reservation3);
//        List<Reservation> reservations = (List<Reservation>) reservationService.getDailyReservations(0, 5).getOrDefault("reservations", null);
//        assertNotNull(reservations);
//        assertEquals(2, reservations.size());
//        assertEquals(reservation1.getReference(), reservations.get(0).getReference());
//        assertEquals(reservation3.getReference(), reservations.get(1).getReference());
//    }
//
//    @Test
//    void addReservation() throws ReservationAlreadyExistsException, ReservationNotFoundException {
//
//        List<Livre> livres = new ArrayList<>();
//        livres.add(Livre.builder()
//                .titre("test")
//                .auteur("test")
//                .build()
//        );
//
//        Client client = Client.builder()
//                .nom("Jean")
//                .prenom("Claude")
//                .numeroTel("0123456789")
//                .email("jean@claude.fr")
//                .build();
//
//        ReservationDTO reservation = ReservationDTO.builder()
//                .reference(1)
//                .dateReservation("01/01/2021")
//                .livres(livres)
//                .client(client)
//                .build();
//
//        reservationService.addReservation(reservation);
//        ReservationDTO result = reservationService.getReservation(reservation.getReference());
//        assertNotNull(result);
//        assertEquals(1, result.getReference());
//    }
//
//    @Test
//    void validateReservation() throws ReservationNotFoundException, ReservationAlreadyValidateException, ReservationAlreadyExistsException {
//
//        List<Livre> livres = new ArrayList<>();
//        livres.add(Livre.builder()
//                .titre("test")
//                .auteur("test")
//                .build()
//        );
//
//        Client client = Client.builder()
//                .nom("Jean")
//                .prenom("Claude")
//                .numeroTel("0123456789")
//                .email("jean@claude.fr")
//                .build();
//
//        ReservationDTO reservation = ReservationDTO.builder()
//                .reference(1)
//                .dateReservation(reservationMapper.dateTimeToString(LocalDateTime.now()))
//                .livres(livres)
//                .client(client)
//                .build();
//
//        reservationService.addReservation(reservation);
//        reservationService.validateReservation(reservation.getReference(), "01/01/2021");
//        ReservationDTO result = reservationService.getReservation(reservation.getReference());
//        assertNotNull(result.getDateRetrait());
//        assertEquals("01/01/2021", result.getDateRetrait());
//    }
//
//    @Test
//    void prepareReservation() throws ReservationNotFoundException, ReservationAlreadyInPrepareException, ReservationAlreadyExistsException {
//
//        List<Livre> livres = new ArrayList<>();
//        livres.add(Livre.builder()
//                .titre("test")
//                .auteur("test")
//                .build()
//        );
//
//        Client client = Client.builder()
//                .nom("Jean")
//                .prenom("Claude")
//                .numeroTel("0123456789")
//                .email("jean@claude.fr")
//                .build();
//
//        ReservationDTO reservation = ReservationDTO.builder()
//                .reference(1)
//                .dateReservation(reservationMapper.dateTimeToString(LocalDateTime.now()))
//                .enPreparation(false)
//                .livres(livres)
//                .client(client)
//                .build();
//
//        reservationService.addReservation(reservation);
//        reservationService.prepareReservation(reservation.getReference());
//        ReservationDTO result = reservationService.getReservation(reservation.getReference());
//        assertTrue(result.isEnPreparation());
//    }
//
//    @Test
//    void deleteReservation() {
//
//        assertThrows(ReservationNotFoundException.class, () -> {
//
//            List<Livre> livres = new ArrayList<>();
//            livres.add(Livre.builder()
//                    .titre("test")
//                    .auteur("test")
//                    .build()
//            );
//
//            Client client = Client.builder()
//                    .nom("Jean")
//                    .prenom("Claude")
//                    .numeroTel("0123456789")
//                    .email("jean@claude.fr")
//                    .build();
//
//            ReservationDTO reservation = ReservationDTO.builder()
//                    .reference(1)
//                    .dateReservation(reservationMapper.dateTimeToString(LocalDateTime.now()))
//                    .enPreparation(false)
//                    .livres(livres)
//                    .client(client)
//                    .build();
//
//            reservationService.addReservation(reservation);
//            reservationService.deleteReservation(reservation.getReference());
//            reservationService.getReservation(reservation.getReference());
//        });
//    }
//
//    @Test
//    void deleteAllReservations() throws ReservationAlreadyExistsException {
//
//        List<Livre> livres = new ArrayList<>();
//        livres.add(Livre.builder()
//                .titre("test")
//                .auteur("test")
//                .build()
//        );
//
//        Client client = Client.builder()
//                .nom("Jean")
//                .prenom("Claude")
//                .numeroTel("0123456789")
//                .email("jean@claude.fr")
//                .build();
//
//        ReservationDTO reservation1 = ReservationDTO.builder()
//                .reference(1)
//                .dateReservation(reservationMapper.dateTimeToString(LocalDateTime.now()))
//                .enPreparation(false)
//                .livres(livres)
//                .client(client)
//                .build();
//
//        ReservationDTO reservation2 = ReservationDTO.builder()
//                .reference(1)
//                .dateReservation(reservationMapper.dateTimeToString(LocalDateTime.now()))
//                .enPreparation(false)
//                .livres(livres)
//                .client(client)
//                .build();
//
//        reservationService.addReservation(reservation1);
//        reservationService.addReservation(reservation2);
//        reservationService.deleteAll();
//        List<Reservation> reservations = (List<Reservation>) reservationService.getAllReservations(0, 5);
//        assertEquals(0, reservations.size());
//    }
//}
