package job_hunter.hct_14.repository;

import job_hunter.hct_14.entity.Skills;
import job_hunter.hct_14.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface SkillReponsetory extends JpaRepository<Skills, Integer>, JpaSpecificationExecutor<Skills> {
    boolean existsByName(String name);
    List<Skills> findByIdIn(List<Integer> id);
//    boolean findByName(@Param("name") String name);

//    Page<User> findAll(Specification<User> spec, Pageable pageable);
}
