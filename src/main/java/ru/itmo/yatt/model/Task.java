package ru.itmo.yatt.model;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.*;

/**
 * @author Vladimir Goncharov
 * @created 08.05.2021
 */

@Entity
@Data
@EnableAutoConfiguration
@Table(name = "task")
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "with")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", columnDefinition = "TEXT")
    private String name;

    @Column(name = "description", columnDefinition="TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    @JsonIgnoreProperties({"tasks", "participants"})
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id")
    @JsonIgnoreProperties(value = {"assigned", "reported", "status", "projects", "hibernateLazyInitializer"})
    private User assignee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", nullable = false)
    @JsonIgnoreProperties(value = {"assigned", "reported", "status", "projects", "hibernateLazyInitializer"})
    private User reporter;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private TaskStatus status;
}
