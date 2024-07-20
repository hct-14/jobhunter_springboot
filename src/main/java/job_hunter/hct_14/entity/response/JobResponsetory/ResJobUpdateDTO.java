package job_hunter.hct_14.entity.response.JobResponsetory;

import job_hunter.hct_14.util.constant.LevelEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class ResJobUpdateDTO {
    private int id;
    private String name;
    private String location;
    private double salary;
    private int quantity;
    private LevelEnum level;
    private String description;
    private Instant startDate;
    private Instant endDate;
    private boolean Active;
    private List<String> skills;

    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
}
