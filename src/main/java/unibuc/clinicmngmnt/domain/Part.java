package unibuc.clinicmngmnt.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Part {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String brand;
    @JsonIgnore
    @ManyToMany(mappedBy = "parts")
    private List<Prescription> prescriptions;

    public Part(String name, String brand) {
        this.name = name;
        this.brand = brand;
    }
}
