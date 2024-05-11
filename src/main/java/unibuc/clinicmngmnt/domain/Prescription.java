package unibuc.clinicmngmnt.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne(mappedBy = "prescription")
    @JsonIgnore
    private Appointment appointment;
    private String comments;
    @ManyToMany
    @JoinTable(name = "prescription_medication",
            joinColumns = @JoinColumn(name = "prescription_id"),
            inverseJoinColumns = @JoinColumn(name = "medication_id"))
    @ToString.Exclude
    private List<Medication> medications;

    public Prescription(String comments) {
        this.comments = comments;
    }

}
