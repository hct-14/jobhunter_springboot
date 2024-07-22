package job_hunter.hct_14.repository;

import job_hunter.hct_14.entity.Permission;
import job_hunter.hct_14.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PermissionReponsetory extends JpaRepository<Permission, Integer>, JpaSpecificationExecutor<Permission> {

//    boolean existsByNoduleAndAndApiPathAndAndMethod(Permission permission);

    boolean existsByModuleAndApiPathAndMethod(String module, String apiPath, String method);
    boolean existsByModule(String module);
    boolean existsByApiPath(String apiPath);
    boolean existsByMethod(String method);
    List<Permission> findByIdIn(List<Integer> id);

}
