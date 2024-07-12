package job_hunter.hct_14.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import job_hunter.hct_14.util.SercuryUtil;
import job_hunter.hct_14.util.constant.GenderEnum;
import lombok.Getter;
import lombok.Setter;

import javax.xml.crypto.Data;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Entity
@Table(name = "user")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
//    @Column(name = "name")
    @NotBlank(message = "email khong duoc de trong")
    private String name;
    @Column(name = "email")
    @NotBlank(message = "password khong duoc de trong")
    private String email;
//    @Column(name = "pass_word")
    private String passWord;
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
    @JoinColumn(name="company_id")
    @JsonBackReference
    private Company company;


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
