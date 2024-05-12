package unibuc.carServiceManagement.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class CarService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String address;
    private String name;

    @OneToMany(mappedBy = "carService", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Mechanic> mechanics = new ArrayList<>();

    public CarService(String name, String address, List<Mechanic> mechanics) {
        this.name = name;
        this.address = address;
        this.mechanics = mechanics;
    }

    public CarService(String name, String address) {
        this.name = name;
        this.address = address;
    }

}
