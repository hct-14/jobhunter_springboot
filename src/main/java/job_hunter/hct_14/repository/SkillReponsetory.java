package job_hunter.hct_14.repository;

import job_hunter.hct_14.entity.Skills;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository

public interface SkillReponsetory extends JpaRepository<Skills, Integer>, JpaSpecificationExecutor<Skills> {
}
