package unibuc.carServiceManagement.domain;

import lombok.*;

import javax.persistence.*;
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
    @JoinColumn(name = "client_id")
    private Client client;
    @ManyToOne
    @JoinColumn(name = "mechanic_id")
    private Mechanic mechanic;
    @Column(name = "start_date")
    private LocalDateTime startDate;
    @Column(name = "end_date")
    private LocalDateTime endDate;
    private String comments;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "task_id")
    @ToString.Exclude
    private Task task;

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


