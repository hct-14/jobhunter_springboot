package job_hunter.hct_14.entity.response.RoleResponsetoty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResRoleDTO {
    private int id;
    private String name;
    private String description;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
    private List<PermistionRole> permissions;



    @Setter
    @Getter
    @AllArgsConstructor
//    @NoArgsConstructor
    public static class PermistionRole {
        private int id;
        private String name;
    }
}
