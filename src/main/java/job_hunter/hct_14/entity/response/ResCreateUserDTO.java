package job_hunter.hct_14.entity.response;

import job_hunter.hct_14.entity.Company;
import job_hunter.hct_14.util.constant.GenderEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ResCreateUserDTO {

    private int id;
    private String name;
    private String email;
    private GenderEnum gender;
    private String address;
    private int age;
    private Instant createdAt;

    private CompanyUser companyUser;

    @Getter
    @Setter
    public static class CompanyUser{
        private int id;
        private String name;
    }
}
