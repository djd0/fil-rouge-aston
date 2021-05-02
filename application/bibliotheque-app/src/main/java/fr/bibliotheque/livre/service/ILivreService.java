package fr.bibliotheque.livre.service;

import fr.bibliotheque.livre.dto.LivreDTO;
import fr.bibliotheque.livre.exception.LivreAlreadyExistsException;
import fr.bibliotheque.livre.exception.LivreAlreadyInPrepareException;
import fr.bibliotheque.livre.exception.LivreCommandeAlreadyValidateException;
import fr.bibliotheque.livre.exception.LivreNotFoundException;
import fr.bibliotheque.livre.model.Livre;

import java.util.Map;

public interface ILivreService {

    Livre getLivre(long reference) throws LivreNotFoundException;

    Map<String, Object> getAllLivres(int page, int size);

    long addLivre(LivreDTO livre) throws LivreAlreadyExistsException;

    long updateLivre(long reference, LivreDTO livre) throws LivreNotFoundException;

    void deleteLivre(long reference) throws LivreNotFoundException;

    Map<String, Object> getLivresACommander(int page, int size);

    long validateCommande(long reference) throws LivreNotFoundException, LivreCommandeAlreadyValidateException;

    long prepareCommande(long reference) throws LivreNotFoundException, LivreAlreadyInPrepareException;

    void deleteAll();
}
