package job_hunter.hct_14.service;

import job_hunter.hct_14.entity.Permission;
import job_hunter.hct_14.entity.Role;
import job_hunter.hct_14.entity.Skills;
import job_hunter.hct_14.repository.PermissionReponsetory;
import job_hunter.hct_14.repository.RoleReponsetory;
import job_hunter.hct_14.util.error.IdInvaldException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RoleService {
    private final RoleReponsetory roleReponsetory;
    private final PermissionReponsetory permissionReponsetory;

    public RoleService(RoleReponsetory roleReponsetory, PermissionReponsetory permissionReponsetory) {
        this.roleReponsetory = roleReponsetory;
        this.permissionReponsetory= permissionReponsetory;
    }
    boolean existsByNameRole(String name){
        return this.roleReponsetory.existsByName(name);
    }
    public  Role findById(int id){
        Optional<Role> role = this.roleReponsetory.findById(id);
        if (role.isPresent()){
            return role.get();
        }
        return null;
    }
    public Role createRole(Role role) throws IdInvaldException {
        boolean existByNameCheck = this.existsByNameRole(role.getName());
        if (existByNameCheck=true){
            throw new IdInvaldException("role này đã tồn tại rồi em");
        }
        return this.roleReponsetory.save(role);
    }
    public Role update(Role roleFe, Role roleBe)throws IdInvaldException{
        Optional<Role> optionalRole = this.roleReponsetory.findById(roleFe.getId());
        if (!optionalRole.isPresent()){
            throw new IdInvaldException("không có role này mà cập nhập");
        }
        if (roleFe.getPermissions() != null){
            List<Integer> reqPer = roleFe.getPermissions()
                    .stream()
                    .map(Permission::getId)
                    .collect(Collectors.toList());

            List<Permission> roleBe1 = this.permissionReponsetory.findByIdIn(reqPer);
            roleBe.setPermissions(roleBe1);
        }
        if (roleFe.getName() != null){
            roleBe.setName(roleFe.getName());
        }
        roleBe.setActive(roleFe.isActive());

        Role role = this.roleReponsetory.save(roleBe);
        return role;

    }
}
