package fr.bibliotheque.livre.controller;

import fr.bibliotheque.livre.constante.LivreExceptionConstante;
import fr.bibliotheque.livre.dto.LivreDTO;
import fr.bibliotheque.livre.exception.*;
import fr.bibliotheque.livre.model.Livre;
import fr.bibliotheque.livre.service.ILivreService;
import fr.bibliotheque.livre.validator.LivreValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("api/livres")
public class LivreRestController {

    private final ILivreService livreService;
    private final LivreValidator livreValidator;


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> getAllLivres(@RequestParam(name = "page", defaultValue = "0") int page,
                                    @RequestParam(name = "size", defaultValue = "10") int size) {
        log.debug("Get all livres");
        return this.livreService.getAllLivres(page, size);
    }

    @GetMapping(value="/{reference}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Livre getLivre(@PathVariable long reference) {

        log.debug(String.format("Get livre with reference : %d", reference));

        try {
            return this.livreService.getLivre(reference);

        } catch(LivreNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    e.getMessage(),
                    e);
        }
    }

    @GetMapping(value="/commandes", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> getLivreACommander(@RequestParam(name = "page", defaultValue = "0") int page,
                                                 @RequestParam(name = "size", defaultValue = "10") int size) {

        log.debug("Get livres à commander");
        return this.livreService.getLivresACommander(page, size);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public long addLivre(@RequestBody LivreDTO livre) {

        log.debug(String.format("Add new livre {%s}", livre));
        try {
            livreValidator.validateLivre(livre);
            return this.livreService.addLivre(livre);

        } catch(LivreValidationException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage(),
                    e);

        } catch(LivreAlreadyExistsException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    e.getMessage(),
                    e);
        }
    }

    @PutMapping(value = "/{reference}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public long updatelivre(@PathVariable long reference,
                              @RequestBody LivreDTO livre) {

        log.debug(String.format("Update livre reference : %s with data : %s", reference, livre));

        try {
            livreValidator.validateLivre(livre);
            return this.livreService.updateLivre(reference, livre);

        } catch(LivreValidationException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage(),
                    e);

        } catch(LivreNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    e.getMessage(),
                    e);
        }
    }

    @PatchMapping(value = "/validate/{reference}")
    public long validateCommande(@PathVariable long reference) {

        log.debug(String.format("Validate commande livre with reference : %d", reference));

        try {
            return this.livreService.validateCommande(reference);

        } catch (LivreNotFoundException e) {
            log.error(String.format(LivreExceptionConstante.LIVRE_REF_NOT_FOUND, reference), e);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format(LivreExceptionConstante.LIVRE_REF_NOT_FOUND, reference),
                    e);

        } catch (LivreCommandeAlreadyValidateException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    e.getMessage(),
                    e);
        }
    }

    @PatchMapping(value = "/prepare/{reference}")
    public long prepareCommande(@PathVariable long reference) {

        log.debug(String.format("Prepare commande livre with reference : %d", reference));

        try {
            return this.livreService.prepareCommande(reference);

        } catch(LivreNotFoundException e) {
            log.error(String.format(LivreExceptionConstante.LIVRE_REF_NOT_FOUND, reference), e);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format(LivreExceptionConstante.LIVRE_REF_NOT_FOUND, reference),
                    e);

        } catch(LivreAlreadyInPrepareException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    e.getMessage(),
                    e);

        }
    }

    @DeleteMapping(value = "/{reference}")
    public void deleteLivre(@PathVariable long reference) {

        log.debug(String.format("Delete livre with reference : %s", reference));

        try {
            this.livreService.deleteLivre(reference);

        } catch(LivreNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    e.getMessage(),
                    e);
        }
    }
}
