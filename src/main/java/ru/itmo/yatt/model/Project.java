package ru.itmo.yatt.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.*;
import java.util.List;

/**
 * @author Vladimir Goncharov
 * @created 08.05.2021
 */

@Entity
@Data
@EnableAutoConfiguration
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "set")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", columnDefinition = "TEXT")
    @NonNull
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    @NonNull
    private String description;

    @OneToMany(mappedBy = "project", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Task> tasks;

    @ManyToMany(mappedBy = "projects", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JsonIgnoreProperties({"role", "status", "assigned", "reported", "projects"})
    private List<User> participants;

    public boolean isMember(String email) {
        return participants.stream().anyMatch(user -> user.getEmail().equals(email));
    }

    public boolean isMember(User user) {
        return isMember(user.getEmail());
    }

}
