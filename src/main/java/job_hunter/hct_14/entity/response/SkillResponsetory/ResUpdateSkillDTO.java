package job_hunter.hct_14.entity.response.SkillResponsetory;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
@Setter
@Getter
public class ResUpdateSkillDTO {

    private int id;
    private String name;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

}
