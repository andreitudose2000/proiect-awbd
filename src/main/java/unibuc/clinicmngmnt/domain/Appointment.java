package unibuc.clinicmngmnt.domain;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
    @Column(name = "start_date")
    private LocalDateTime startDate;
    @Column(name = "end_date")
    private LocalDateTime endDate;
    private String comments;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "prescription_id")
    @ToString.Exclude
    private Prescription prescription;

    public Appointment(LocalDateTime startDate, LocalDateTime endDate, String comments) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.comments = comments;
    }

    public Appointment(LocalDateTime startDate, LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }
}


