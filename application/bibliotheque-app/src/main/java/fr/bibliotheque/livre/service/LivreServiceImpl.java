package fr.bibliotheque.livre.service;

import fr.bibliotheque.livre.constante.LivreExceptionConstante;
import fr.bibliotheque.livre.dto.LivreDTO;
import fr.bibliotheque.livre.exception.LivreAlreadyExistsException;
import fr.bibliotheque.livre.exception.LivreAlreadyInPrepareException;
import fr.bibliotheque.livre.exception.LivreCommandeAlreadyValidateException;
import fr.bibliotheque.livre.exception.LivreNotFoundException;
import fr.bibliotheque.livre.mapper.LivreMapper;
import fr.bibliotheque.livre.model.Livre;
import fr.bibliotheque.livre.repository.LivreRepository;
import fr.bibliotheque.livre.validator.LivreValidator;
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

@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class LivreServiceImpl implements ILivreService {


    private final LivreRepository livreRepository;
    private final LivreMapper livreMapper;
    private final LivreValidator livreValidator;


    @Override
    public Livre getLivre(long reference) throws LivreNotFoundException {
        return this.livreRepository.findByReference(reference)
                .orElseThrow(LivreNotFoundException::new);
    }

    @Override
    public Map<String, Object> getAllLivres(int page, int size) {

        Pageable paging = PageRequest.of(page, size);
        Page<Livre> pageLivres = livreRepository.findAll(paging);
        List<Livre> livres = pageLivres.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("livres", livres);
        response.put("currentPage", pageLivres.getNumber());
        response.put("totalItems", pageLivres.getTotalElements());
        response.put("totalPages", pageLivres.getTotalPages());

        return response;
    }

    @Override
    public long addLivre(LivreDTO livre) throws LivreAlreadyExistsException {

        livreValidator.isLivreExists(livre);
        return this.livreRepository.save(livreMapper.mapLivreDTOToLivre(livre))
                .getReference();
    }

    @Override
    public long updateLivre(long reference, LivreDTO dto) throws LivreNotFoundException {

        Livre livre;
        try {
            livre = this.getLivre(reference);

        } catch (LivreNotFoundException e) {
            log.error(String.format(LivreExceptionConstante.LIVRE_REF_NOT_FOUND, reference));
            throw new LivreNotFoundException(String.format(LivreExceptionConstante.LIVRE_REF_NOT_FOUND, reference), e);
        }

        log.debug("Livre found, updating");
        return this.livreRepository.save(livreMapper.mapLivreWithDTO(livre, dto))
                .getReference();
    }


    @Override
    public void deleteLivre(long reference) throws LivreNotFoundException {

        try {
            this.getLivre(reference);

        } catch (LivreNotFoundException e) {
            log.error(String.format(LivreExceptionConstante.LIVRE_REF_NOT_FOUND, reference));
            throw new LivreNotFoundException(String.format(LivreExceptionConstante.LIVRE_REF_NOT_FOUND, reference), e);
        }

        log.debug("Livre found, deleting");
        this.livreRepository.deleteByReference(reference);
    }

    @Override
    public Map<String, Object> getLivresACommander(int page, int size) {

        Pageable paging = PageRequest.of(page, size);
        Page<Livre> pageLivres = livreRepository.findLivresACommanderOrderByDemandesAsc(paging);
        List<Livre> livres = pageLivres.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("livres", livres);
        response.put("currentPage", pageLivres.getNumber());
        response.put("totalItems", pageLivres.getTotalElements());
        response.put("totalPages", pageLivres.getTotalPages());

        return response;
    }

    @Override
    public long validateCommande(long reference) throws LivreNotFoundException, LivreCommandeAlreadyValidateException {

        Livre livre = this.livreRepository.findByReference(reference)
                .orElseThrow(LivreNotFoundException::new);

        if(livre.isCommandeEnCours()) {
            log.error(String.format(LivreExceptionConstante.LIVRE_COMMANDE_ALREADY_VALIDATE, livre.getAuteur(), livre.getTitre()));
            throw new LivreCommandeAlreadyValidateException(String.format(LivreExceptionConstante.LIVRE_COMMANDE_ALREADY_VALIDATE, livre.getAuteur(), livre.getTitre()));
        }

        log.debug("Livre found, validating");
        return this.livreRepository
                .save(livreMapper.mapValidateCommande(livre))
                .getReference();
    }

    @Override
    public long prepareCommande(long reference) throws LivreNotFoundException, LivreAlreadyInPrepareException {

        Livre livre  = this.livreRepository.findByReference(reference)
                .orElseThrow(LivreNotFoundException::new);

        if(livre.isEnPreparation()) {
            log.error(String.format(LivreExceptionConstante.LIVRE_ALREADY_IN_PREPARE, livre.getAuteur(), livre.getTitre()));
            throw new LivreAlreadyInPrepareException(String.format(LivreExceptionConstante.LIVRE_ALREADY_IN_PREPARE, livre.getAuteur(), livre.getTitre()));
        }

        log.debug("Livre found, validating");
        return this.livreRepository
                .save(livreMapper.mapPrepareCommande(livre))
                .getReference();
    }

    @Override
    public void deleteAll() {

        livreRepository.deleteAll();
    }
}
