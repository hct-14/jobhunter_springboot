package job_hunter.hct_14.service;

import job_hunter.hct_14.entity.Permission;
import job_hunter.hct_14.entity.Role;
import job_hunter.hct_14.entity.Skills;
import job_hunter.hct_14.repository.PermissionReponsetory;
import job_hunter.hct_14.repository.RoleReponsetory;
import job_hunter.hct_14.util.error.IdInvaldException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PermissionService {
    @Autowired
    private final PermissionReponsetory permissionReponsetory;
    private final RoleReponsetory roleReponsetory;

    public PermissionService(PermissionReponsetory permissionReponsetory, RoleReponsetory roleReponsetory) {
        this.permissionReponsetory = permissionReponsetory;
        this.roleReponsetory = roleReponsetory;
    }

    boolean isExistsByModuleAndAndApiPathAndAndMethodCheck(Permission permission){
        return permissionReponsetory.existsByModuleAndApiPathAndMethod(
                permission.getModule(),
                permission.getApiPath(),
                permission.getMethod()
        );

    }
    boolean isExistsByModuleCheck(String module){
        return permissionReponsetory.existsByModule(module);
    }
    boolean isExistsbyApiPath(String apiPath){
        return permissionReponsetory.existsByApiPath(apiPath);

    }
    boolean isExistsByMethod(String method){
        return permissionReponsetory.existsByMethod(method);
    }
//    boolean existsby
    public Permission createPermission(Permission permission) throws IdInvaldException {
//        boolean permissionCheck = this.permissionReponsetory.existsByNoduleAndAndApiPathAndAndMethod(permission.getModule(), permission.getApiPath(), permission.getMethod());
        boolean permissionCheckAll = this.isExistsByModuleAndAndApiPathAndAndMethodCheck(permission) ;
        boolean permissionCheckModule = this.isExistsByModuleCheck(permission.getModule());
        boolean permissionCheckPath = this.isExistsbyApiPath(permission.getApiPath());
        boolean permissionCheckMethod = this.isExistsByMethod(permission.getMethod());
        if (permissionCheckAll==true){
            throw new IdInvaldException("3 cái thằng này tồn tại rồi em ơi");
        }
        if (permissionCheckModule==true){
            throw new IdInvaldException("cái thằng module này tồn tại rồi em oi");
        }
        if (permissionCheckPath == true){
            throw new IdInvaldException("Cái thằng path này tồn tại rồi em ơi");
        }
        if (permissionCheckMethod==true){
            throw new IdInvaldException("cái thằng method này tồn tại rồi em ơi");
        }


            return this.permissionReponsetory.save(permission);


//        return this.permissionReponsetory.save(permission);

    }
    public Permission handleUpdatePermission(Permission permisFe, Permission permisBe) {
        Optional<Permission> optionalPermission = this.permissionReponsetory.findById(permisFe.getId());
        if (!optionalPermission.isPresent()) {
            // Xử lý trường hợp không tìm thấy Permission, ví dụ: ném ra một exception
            throw new IllegalArgumentException("Permission not found");
        }

        // Cập nhật các trường nếu giá trị trong permisFe không phải null
        if (permisFe.getName() != null) {
            permisBe.setName(permisFe.getName());
        }
        if (permisFe.getApiPath() != null) {
            permisBe.setApiPath(permisFe.getApiPath());
        }
        if (permisFe.getMethod() != null) {
            permisBe.setMethod(permisFe.getMethod());
        }
        if (permisFe.getModule() != null) {
            permisBe.setModule(permisFe.getModule());
        }

        Permission per = this.permissionReponsetory.save(permisBe);

        if (permisFe.getRoles() != null) {
            List<Integer> reqRoleIds = permisFe.getRoles()
                    .stream()
                    .map(Role::getId)
                    .collect(Collectors.toList());

            List<Role> dbRoles = this.roleReponsetory.findByIdIn(reqRoleIds);

            permisBe.setRoles(dbRoles);
        }

        return per;
    }

}
