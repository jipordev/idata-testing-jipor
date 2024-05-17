package co.istad.idata.domains.api;

import co.istad.idata.domains.auth.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "apis")
public class Api {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdAt;

    private String description;

    @ManyToOne
    private User owner;


}
