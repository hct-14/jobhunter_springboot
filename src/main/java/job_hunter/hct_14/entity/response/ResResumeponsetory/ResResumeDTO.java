package job_hunter.hct_14.entity.response.ResResumeponsetory;

import job_hunter.hct_14.util.constant.ResumeStateEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResResumeDTO {

    private int id;
    private String email;
    private String url;
    private ResumeStateEnum status;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    private UserResume userResume;
    private JobResume jobResume;

//    public void setName(String name) {
//    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserResume {
        private int id;
        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class JobResume {
        private int id;
        private String name;

        }
    }

