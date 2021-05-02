package fr.bibliotheque.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
public class ClientDTO {

    @NotBlank
    private long reference;

    @NotBlank
    private String nom;

    @NotBlank
    private String prenom;

    @NotBlank
    private String numeroTel;

    @NotBlank
    private Email email;

}
