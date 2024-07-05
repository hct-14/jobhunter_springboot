package job_hunter.hct_14.entity.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Date;
@Getter
@Setter
public class ResUpdateUserDTO {
    private int id;
    private String name;
    private String email;
    private String address;
    private int age;
    private String password;
    private Date updatedAt;
    private String updatedBy;

}
