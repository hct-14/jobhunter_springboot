package job_hunter.hct_14.entity.DTO;

import job_hunter.hct_14.util.constant.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResUserDTO {


//    public class ResCreateUserDTO {
        private int id;
        private String name;
        private String email;
        private GenderEnum gender;
        private String address;
        private int age;
        private Instant createdAt;
        private Instant updatedAt;
    }


