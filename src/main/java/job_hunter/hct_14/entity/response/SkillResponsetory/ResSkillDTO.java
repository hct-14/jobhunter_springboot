package job_hunter.hct_14.entity.response.SkillResponsetory;

import job_hunter.hct_14.entity.response.ResUserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResSkillDTO {
    private int id;
    private String name;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
//    private List<JobSkills> jobSkills;
//
//    @Getter
//    @Setter
//    @AllArgsConstructor
//    @NoArgsConstructor
//    public static class JobSkills {
//        private int id;
//        private String name;
//    }
}
