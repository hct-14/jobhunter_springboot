package job_hunter.hct_14.entity.response;

import job_hunter.hct_14.util.constant.GenderEnum;
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
    private GenderEnum gender;
    private String address;
    private int age;
    private Instant createdAt;
    private Instant updatedAt;

    private CompanyUser companyUser;

    @Getter
    @Setter
    public static class CompanyUser{
        private int id;
        private String name;
    }

}
