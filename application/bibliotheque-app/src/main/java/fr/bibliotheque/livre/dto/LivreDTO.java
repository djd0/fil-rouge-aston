package fr.bibliotheque.livre.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
public class LivreDTO {

    @NotBlank
    private long reference;

    @NotBlank
    private String titre;

    private String genre;

    @NotBlank
    private String auteur;

    private int quantite;

    private int nombreDemandes;

    private boolean commandeEnCours;

    private boolean enPreparation;
}
