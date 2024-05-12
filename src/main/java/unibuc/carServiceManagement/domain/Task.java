package unibuc.carServiceManagement.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne(mappedBy = "task")
    @JsonIgnore
    private Appointment appointment;
    private String comments;
    @ManyToMany
    @JoinTable(name = "task_part",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "part_id"))
    @ToString.Exclude
    private List<Part> parts;

    public Task(String comments) {
        this.comments = comments;
    }

}
