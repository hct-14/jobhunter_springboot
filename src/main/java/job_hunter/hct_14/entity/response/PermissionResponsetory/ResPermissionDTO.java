package job_hunter.hct_14.entity.response;

import job_hunter.hct_14.entity.Role;
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
public class ResPermissionDTO {


    private int id;
    private String name;
    private String apiPath;
    private String method;
    private String module;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
    private List<RolePermission> rolePermission; // Sửa thành List<RolePermission>

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RolePermission {
        private int id;
        private String name;
        private String description; // Sửa tên biến cho đúng
    }
}