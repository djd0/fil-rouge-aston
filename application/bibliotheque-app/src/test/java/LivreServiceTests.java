//package fr.bibliotheque;
//
//import fr.bibliotheque.livre.dto.LivreDTO;
//import fr.bibliotheque.livre.exception.LivreAlreadyExistsException;
//import fr.bibliotheque.livre.exception.LivreAlreadyInPrepareException;
//import fr.bibliotheque.livre.exception.LivreCommandeAlreadyValidateException;
//import fr.bibliotheque.livre.exception.LivreNotFoundException;
//import fr.bibliotheque.livre.model.Livre;
//import fr.bibliotheque.livre.service.ILivreService;
//import lombok.AllArgsConstructor;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@AllArgsConstructor(onConstructor = @__(@Autowired))
//@RunWith(SpringRunner.class)
//@SpringBootTest
//class LivreServiceTests {
//
//
//    @Autowired
//    ILivreService livreService;
//
//    @BeforeEach
//    void setup() {
//        livreService.deleteAll();
//    }
//
//    @Test
//    void getAllLivres() throws LivreAlreadyExistsException {
//
//        LivreDTO livre1 = LivreDTO.builder()
//                .reference(1)
//                .titre("test1")
//                .auteur("test1")
//                .build();
//
//        LivreDTO livre2 = LivreDTO.builder()
//                .reference(2)
//                .titre("test2")
//                .auteur("test2")
//                .build();
//
//        livreService.addLivre(livre1);
//        livreService.addLivre(livre2);
//        List<Livre> livres = (List<Livre>) livreService.getAllLivres(0, 5).getOrDefault("livres", null);
//        assertNotNull(livres);
//        assertEquals(2, livres.size());
//    }
//
//    @Test
//    void getLivre() throws LivreNotFoundException, LivreAlreadyExistsException  {
//
//        LivreDTO livre = LivreDTO.builder()
//                .reference(1)
//                .titre("test")
//                .auteur("test")
//                .build();
//
//        livreService.addLivre(livre);
//        Livre result = livreService.getLivre(livre.getReference());
//        assertNotNull(result);
//    }
//
//    @Test
//    void getLivreACommander() throws LivreAlreadyExistsException {
//
//        LivreDTO livre1 = LivreDTO.builder()
//                .reference(1)
//                .titre("test1")
//                .auteur("test1")
//                .quantite(0)
//                .nombreDemandes(1)
//                .build();
//
//        LivreDTO livre2 = LivreDTO.builder()
//                .reference(2)
//                .titre("test2")
//                .auteur("test2")
//                .quantite(2)
//                .nombreDemandes(1)
//                .build();
//
//        LivreDTO livre3 = LivreDTO.builder()
//                .reference(3)
//                .titre("test3")
//                .auteur("test3")
//                .quantite(0)
//                .nombreDemandes(1)
//                .build();
//
//        livreService.addLivre(livre1);
//        livreService.addLivre(livre2);
//        livreService.addLivre(livre3);
//
//        List<Livre> livres = (List<Livre>) livreService.getLivresACommander(0, 5).getOrDefault("livres", null);
//        assertNotNull(livres);
//        assertEquals(2, livres.size());
//        assertEquals(livre1.getReference(), livres.get(0).getReference());
//        assertEquals(livre3.getReference(), livres.get(1).getReference());
//    }
//
//    @Test
//    void addLivre() throws LivreAlreadyExistsException, LivreNotFoundException {
//
//        LivreDTO livre = LivreDTO.builder()
//                .reference(1)
//                .titre("test")
//                .auteur("test")
//                .build();
//
//        livreService.addLivre(livre);
//        Livre result = livreService.getLivre(livre.getReference());
//        assertNotNull(result);
//        assertEquals(1, result.getReference());
//    }
//
//    @Test
//    void updatelivre() throws LivreNotFoundException, LivreAlreadyExistsException {
//
//        LivreDTO livre = LivreDTO.builder()
//                .reference(1)
//                .titre("test")
//                .auteur("test")
//                .build();
//
//        livreService.addLivre(livre);
//        livre.setAuteur("test2");
//        livreService.updateLivre(livre.getReference(), livre);
//        Livre result = livreService.getLivre(livre.getReference());
//        assertNotNull(result);
//        assertEquals("test2", result.getAuteur());
//    }
//
//    @Test
//    void validateCommande() throws LivreNotFoundException, LivreCommandeAlreadyValidateException, LivreAlreadyExistsException {
//
//        LivreDTO livre = LivreDTO.builder()
//                .reference(1)
//                .titre("test")
//                .auteur("test")
//                .commandeEnCours(false)
//                .build();
//
//        livreService.addLivre(livre);
//        livreService.validateCommande(livre.getReference());
//        Livre result = livreService.getLivre(livre.getReference());
//        assertNotNull(result);
//        assertTrue(result.isCommandeEnCours());
//    }
//
//    @Test
//    void prepareCommande() throws LivreNotFoundException, LivreAlreadyInPrepareException, LivreAlreadyExistsException {
//
//        LivreDTO livre = LivreDTO.builder()
//                .reference(1)
//                .titre("test")
//                .auteur("test")
//                .enPreparation(false)
//                .build();
//
//        livreService.addLivre(livre);
//        livreService.prepareCommande(livre.getReference());
//        Livre result = livreService.getLivre(livre.getReference());
//        assertNotNull(result);
//        assertTrue(result.isEnPreparation());
//    }
//
//    @Test
//    void deleteLivre() {
//
//        assertThrows(LivreNotFoundException.class, () -> {
//
//            LivreDTO livre = LivreDTO.builder()
//                    .reference(1)
//                    .titre("test")
//                    .auteur("test")
//                    .build();
//
//            livreService.addLivre(livre);
//            livreService.deleteLivre(livre.getReference());
//            livreService.getLivre(livre.getReference());
//        });
//    }
//
//    @Test
//    void deleteAllLivres() throws LivreAlreadyExistsException {
//
//        LivreDTO livre1 = LivreDTO.builder()
//                .reference(1)
//                .titre("test1")
//                .auteur("test1")
//                .build();
//
//        LivreDTO livre2 = LivreDTO.builder()
//                .reference(2)
//                .titre("test2")
//                .auteur("test2")
//                .build();
//
//        livreService.addLivre(livre1);
//        livreService.addLivre(livre2);
//        livreService.deleteAll();
//        List<Livre> livres = (List<Livre>) livreService.getAllLivres(0, 5);
//        assertEquals(0, livres.size());
//    }
//}
