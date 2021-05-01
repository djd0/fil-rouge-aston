package fr.bibliotheque;

import fr.bibliotheque.livre.dto.LivreDTO;
import fr.bibliotheque.livre.exception.LivreAlreadyExistsException;
import fr.bibliotheque.livre.exception.LivreAlreadyInPrepareException;
import fr.bibliotheque.livre.exception.LivreCommandeAlreadyValidateException;
import fr.bibliotheque.livre.exception.LivreNotFoundException;
import fr.bibliotheque.livre.model.Livre;
import fr.bibliotheque.livre.service.ILivreService;
import fr.bibliotheque.reservation.service.IReservationService;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@RunWith(SpringRunner.class)
@SpringBootTest
class LivreServiceTests {

    private final ILivreService livreService;
    private final IReservationService reservationService;


    @BeforeEach
    void setup() {
        reservationService.deleteAll();
        livreService.deleteAll();
    }

    @Test
    void getAllLivres() throws LivreAlreadyExistsException {

        LivreDTO livre1 = LivreDTO.builder()
                .reference(1)
                .titre("test1")
                .auteur("test1")
                .build();

        LivreDTO livre2 = LivreDTO.builder()
                .reference(2)
                .titre("test2")
                .auteur("test2")
                .build();

        livreService.addLivre(livre1);
        livreService.addLivre(livre2);
        List<Livre> livres = (List<Livre>) livreService.getAllLivres(0, 5).getOrDefault("livres", null);
        assertNotNull(livres);
        assertEquals(2, livres.size());
        reservationService.deleteAll();
        livreService.deleteAll();
    }

    @Test
    void getLivre() throws LivreNotFoundException, LivreAlreadyExistsException  {

        LivreDTO livre = LivreDTO.builder()
                .reference(1)
                .titre("test")
                .auteur("test")
                .build();

        long id = livreService.addLivre(livre);
        Livre result = livreService.getLivre(id);
        assertNotNull(result);
        reservationService.deleteAll();
        livreService.deleteAll();
    }

    @Test
    void getLivreACommander() throws LivreAlreadyExistsException {

        LivreDTO livre1 = LivreDTO.builder()
                .reference(1)
                .titre("test1")
                .auteur("test1")
                .quantite(0)
                .nombreDemandes(1)
                .build();

        LivreDTO livre2 = LivreDTO.builder()
                .reference(2)
                .titre("test2")
                .auteur("test2")
                .quantite(2)
                .nombreDemandes(1)
                .build();

        LivreDTO livre3 = LivreDTO.builder()
                .reference(3)
                .titre("test3")
                .auteur("test3")
                .quantite(0)
                .nombreDemandes(1)
                .build();

        long id1 = livreService.addLivre(livre1);
        livreService.addLivre(livre2);
        long id3 = livreService.addLivre(livre3);

        List<Livre> livres = (List<Livre>) livreService.getLivresACommander(0, 5).getOrDefault("livres", null);
        assertNotNull(livres);
        assertEquals(2, livres.size());
        assertEquals(id1, livres.get(0).getReference());
        assertEquals(id3, livres.get(1).getReference());
        reservationService.deleteAll();
        livreService.deleteAll();
    }

    @Test
    void addLivre() throws LivreAlreadyExistsException, LivreNotFoundException {

        LivreDTO livre = LivreDTO.builder()
                .reference(1)
                .titre("test")
                .auteur("test")
                .build();

        long id = livreService.addLivre(livre);
        Livre result = livreService.getLivre(id);
        assertNotNull(result);
        assertEquals(id, result.getReference());
        reservationService.deleteAll();
        livreService.deleteAll();
    }

    @Test
    void updatelivre() throws LivreNotFoundException, LivreAlreadyExistsException {

        LivreDTO livre = LivreDTO.builder()
                .reference(1)
                .titre("test")
                .auteur("test")
                .build();

        long id = livreService.addLivre(livre);
        livre.setAuteur("test2");
        livreService.updateLivre(id, livre);
        Livre result = livreService.getLivre(id);
        assertNotNull(result);
        assertEquals("test2", result.getAuteur());
        reservationService.deleteAll();
        livreService.deleteAll();
    }

    @Test
    void validateCommande() throws LivreNotFoundException, LivreCommandeAlreadyValidateException, LivreAlreadyExistsException {

        LivreDTO livre = LivreDTO.builder()
                .reference(1)
                .titre("test")
                .auteur("test")
                .commandeEnCours(false)
                .build();

        long id = livreService.addLivre(livre);
        livreService.validateCommande(id);
        Livre result = livreService.getLivre(id);
        assertNotNull(result);
        assertTrue(result.isCommandeEnCours());
        reservationService.deleteAll();
        livreService.deleteAll();
    }

    @Test
    void prepareCommande() throws LivreNotFoundException, LivreAlreadyInPrepareException, LivreAlreadyExistsException {

        LivreDTO livre = LivreDTO.builder()
                .reference(1)
                .titre("test")
                .auteur("test")
                .enPreparation(false)
                .build();

        long id = livreService.addLivre(livre);
        livreService.prepareCommande(id);
        Livre result = livreService.getLivre(id);
        assertNotNull(result);
        assertTrue(result.isEnPreparation());
        reservationService.deleteAll();
        livreService.deleteAll();
    }

    @Test
    void deleteLivre() throws LivreAlreadyExistsException, LivreNotFoundException {

        LivreDTO livre = LivreDTO.builder()
                .reference(1)
                .titre("test")
                .auteur("test")
                .build();

        long id = livreService.addLivre(livre);
        livreService.deleteLivre(id);
        assertThrows(LivreNotFoundException.class, () -> livreService.getLivre(id));
        reservationService.deleteAll();
        livreService.deleteAll();
    }

    @Test
    void deleteAllLivres() throws LivreAlreadyExistsException {

        LivreDTO livre1 = LivreDTO.builder()
                .reference(1)
                .titre("test1")
                .auteur("test1")
                .build();

        LivreDTO livre2 = LivreDTO.builder()
                .reference(2)
                .titre("test2")
                .auteur("test2")
                .build();

        livreService.addLivre(livre1);
        livreService.addLivre(livre2);
        livreService.deleteAll();
        List<Livre> livres = (List<Livre>) livreService.getAllLivres(0, 5).getOrDefault("livres", null);
        assertEquals(0, livres.size());
        reservationService.deleteAll();
        livreService.deleteAll();
    }
}
