package job_hunter.hct_14.repository;

import job_hunter.hct_14.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface RoleReponsetory extends JpaRepository<Role, Integer>, JpaSpecificationExecutor<Role> {
    boolean existsByName(String name);

    List<Role> findByIdIn(List<Integer> id);
}
