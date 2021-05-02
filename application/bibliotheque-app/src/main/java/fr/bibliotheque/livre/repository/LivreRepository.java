package fr.bibliotheque.livre.repository;

import fr.bibliotheque.livre.model.Livre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface LivreRepository extends PagingAndSortingRepository<Livre, Long> {

    Optional<Livre> findByReference(long reference);

    Livre findByAuteurAndTitreIgnoreCase(String auteur, String titre);

    Page<Livre> findByAuteurContainingIgnoreCase(Pageable pageable, String auteur);

    Page<Livre> findByTitreContainingIgnoreCase(Pageable pageable, String titre);

    Page<Livre> findByReference(Pageable pageable, long reference);

    Page<Livre> findByGenreContainingIgnoreCase(Pageable pageable, String genre);

    @Transactional
    void deleteByReference(long reference);

    @Query("SELECT l from Livre l where l.nombreDemandes > l.quantite and l.commandeEnCours is false order by l.nombreDemandes asc")
    Page<Livre> findLivresACommanderOrderByDemandesAsc(Pageable pageable);
}
