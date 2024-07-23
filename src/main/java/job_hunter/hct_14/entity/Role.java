package job_hunter.hct_14.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
//@AllArgsConstructor
//@NoArgsConstructor
@Table(name = "Roles")

public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "name này không được để trống đâu em")
    private String name;
    private String description;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"roles"})
    @JoinTable(name = "permission_role", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name ="permission_id"))
    private List<Permission> permissions;


    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    @JsonIgnore
    List<User> users;

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
