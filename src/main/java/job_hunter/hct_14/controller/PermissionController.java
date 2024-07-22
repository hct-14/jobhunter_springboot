package job_hunter.hct_14.controller;

import job_hunter.hct_14.entity.Permission;
import job_hunter.hct_14.service.PermissionService;
import job_hunter.hct_14.util.error.IdInvaldException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class PermissionController {

    @Autowired
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("/permission")
    public ResponseEntity<Permission> createPermission(@RequestBody Permission permission) throws IdInvaldException {
        Permission permissionSave = this.permissionService.createPermission(permission);
        return ResponseEntity.status(HttpStatus.OK).body(permissionSave);

    }
}
