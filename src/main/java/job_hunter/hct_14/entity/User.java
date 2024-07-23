package job_hunter.hct_14.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import job_hunter.hct_14.util.SercuryUtil;
import job_hunter.hct_14.util.constant.GenderEnum;
import lombok.Getter;
import lombok.Setter;

import javax.xml.crypto.Data;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

//    @NotBlank(message = "email không được để trống")
    private String email;

//    @NotBlank(message = "password không được để trống")
    private String password;

    private int age;

    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    private String address;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String refreshToken;

    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    List<Resume> resumes;



    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Resume> resumeList;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

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
    public void removeCompany() {
        this.company = null;
    }
}
