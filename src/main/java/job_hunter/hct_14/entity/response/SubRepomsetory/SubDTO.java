package job_hunter.hct_14.entity.response.SubRepomsetory;

import job_hunter.hct_14.entity.Skills;
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
public class SubDTO {

    private int id;
    private String name;
    private String email;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
    private List<Skills> skills;

    public void setSkills(List<String> skills) {
    }
}
