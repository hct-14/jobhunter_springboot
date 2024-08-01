package job_hunter.hct_14.entity.response.email;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResEmailJob {
    private String name;
    private double salary;
    private CompanyEmail company;
    private List<SkillEmail> skills;

//    public void getSkills(List<SkillEmail> skillEmails) {
//    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CompanyEmail{
        private String name;
    }
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SkillEmail{
        private String name;
    }
}
