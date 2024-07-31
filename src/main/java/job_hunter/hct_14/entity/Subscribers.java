package job_hunter.hct_14.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import job_hunter.hct_14.util.SercuryUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Subscribers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "name không được để trống")
    private String name;
    @NotBlank(message = "email không được để trống")
    private String email;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "subscriber_skill",
            joinColumns = @JoinColumn(name = "subscriber_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private List<Skills> skills;

    @PrePersist
    public void handleBeforeCreatedateAt() {
        this.createdBy = SercuryUtil.getCurrentUserLogin().isPresent()==true ?
                SercuryUtil.getCurrentUserLogin().get() : null;
        this.createdAt = Instant.now();

    }
    @PreUpdate
    public void handleBeforeUpdateAt() {
        Optional<String> currentUserLogin = SercuryUtil.getCurrentUserLogin();
        this.updatedBy = currentUserLogin.orElse(null);
        this.updatedAt = Instant.now();
    }
}
