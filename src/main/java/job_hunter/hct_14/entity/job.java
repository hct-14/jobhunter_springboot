package job_hunter.hct_14.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import job_hunter.hct_14.util.SercuryUtil;
import job_hunter.hct_14.util.constant.LevelEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "jobs")
public class job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(columnDefinition = "MEDIUMTEXT")

    private String name;
    private String location;
    private double salary;
    private int quantity;
    @Enumerated(EnumType.STRING)
    private LevelEnum level;
    private String description;
    private Instant startDate;
    private Instant endDate;

    private boolean Active;

    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @ManyToOne()
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"jobs"})
    @JoinTable(name = "job_skill",joinColumns= @JoinColumn(name = "job_id"),inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private List<skills> skills;

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
