package fr.bibliotheque.reservation.model;

import fr.bibliotheque.client.model.Client;
import fr.bibliotheque.livre.model.Livre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Table(name = "RESERVATION")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Reservation implements Serializable {

    @Id
    @GeneratedValue
    @Column
    private long reference;

    @Column
    private LocalDateTime dateReservation;

    @Column
    private LocalDate dateRetrait;

    @Column
    private boolean enPreparation;

    @ManyToMany(cascade = {CascadeType.ALL})
    private List<Livre> livres;

    @ManyToOne(cascade = {CascadeType.ALL})
    private Client client;

}
