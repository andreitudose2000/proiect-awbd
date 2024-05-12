package unibuc.carServiceManagement.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Mechanic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Enumerated(EnumType.STRING)
    private Speciality speciality;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "carService_id")
    private CarService carService;
    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "mechanic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments = new ArrayList<>();

    public Mechanic(String firstName, String lastName, Speciality speciality, CarService carService) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.speciality = speciality;
        this.carService = carService;
    }

    public Mechanic(String firstName, String lastName, Speciality speciality) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.speciality = speciality;
    }
}
