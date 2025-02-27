package ar.edu.unq.epersgeist.modelo;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@ToString
@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document
public class MaxCantidadDominacion {

        @Id
        private String id;
        private int total = 0;

}
