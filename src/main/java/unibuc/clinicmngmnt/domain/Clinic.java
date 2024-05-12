package unibuc.clinicmngmnt.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Clinic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String address;
    private String name;

    @OneToMany(mappedBy = "clinic", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Mechanic> mechanics = new ArrayList<>();

    public Clinic(String name, String address, List<Mechanic> mechanics) {
        this.name = name;
        this.address = address;
        this.mechanics = mechanics;
    }

    public Clinic(String name, String address) {
        this.name = name;
        this.address = address;
    }

}
