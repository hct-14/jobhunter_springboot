package job_hunter.hct_14.controller;


import com.turkraft.springfilter.boot.Filter;
import job_hunter.hct_14.entity.Permission;
import job_hunter.hct_14.entity.Role;
import job_hunter.hct_14.entity.response.ResultPaginationDTO;
import job_hunter.hct_14.service.RoleService;
import job_hunter.hct_14.util.annotation.ApiMessage;
import job_hunter.hct_14.util.error.IdInvaldException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/v1")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("roles")
    @ApiMessage("create api success")
    public ResponseEntity<Role> createRole(@RequestBody Role role) throws IdInvaldException {
        Role roleSave = this.roleService.createRole(role);
        return ResponseEntity.status(HttpStatus.OK).body(roleSave);
    }


    @GetMapping("roles/{id}")
    @ApiMessage("get thành công")
    public ResponseEntity<Role> getRole(@RequestParam Role role) throws IdInvaldException{
        Optional<Role> roleOptional = this.roleService.findById(role.getId());
        if (!roleOptional.isPresent()){
            throw new IdInvaldException("không có thằng id này em ơi");
        }
        return ResponseEntity.status(HttpStatus.OK).body(roleOptional.get());
    }
    @PutMapping("roles")
    @ApiMessage("update oke")
    public ResponseEntity<Role> updateRole(@RequestBody Role role)throws IdInvaldException{
        Optional<Role> roleCheck = this.roleService.findById(role.getId());
        if (!roleCheck.isPresent()){
            throw new IdInvaldException("không có id này em oi");
        }
        Role roleUpdate = this.roleService.updateRoles(role, roleCheck.get());
        return ResponseEntity.status(HttpStatus.OK).body(roleUpdate);
    }
    @DeleteMapping("roles/{id}")
    @ApiMessage("xoa oke")
    public ResponseEntity<String> deleteById(@PathVariable int id)throws IdInvaldException{
        this.roleService.handleDeleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("xoa oke");
    }
    @GetMapping("roles")
    public ResponseEntity<ResultPaginationDTO> getAllPermission(@Filter Specification<Role> spec, Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(this.roleService.getAllJob(spec, pageable));
    }


}
