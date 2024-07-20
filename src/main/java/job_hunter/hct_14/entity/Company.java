package job_hunter.hct_14.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import job_hunter.hct_14.util.SercuryUtil;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "companies")
@Getter
@Setter
//@AllArgsConstructor

public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "name khong duoc de trong dau em oi")
    private String name;
    @NotBlank(message = "desc không được để trống đâu em ơi")
    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;
    private String address;
    private String logo;
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private Instant createdAt;
    private Instant updatedAt;
    private String updatedBy;
    private String createdBy;
//    SercuryUtil

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
//    @OneToMany(mappedBy = "company", cascade = CascadeType.REMOVE)

//    @JsonManagedReference
     @JsonIgnore
    List<User> users;


    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    @JsonIgnore
    List<Job> jobs;

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
