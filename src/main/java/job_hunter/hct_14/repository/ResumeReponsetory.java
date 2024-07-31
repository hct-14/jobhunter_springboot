package job_hunter.hct_14.repository;

import job_hunter.hct_14.entity.Resume;
import job_hunter.hct_14.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface ResumeReponsetory extends JpaRepository<Resume, Integer>, JpaSpecificationExecutor<Resume> {
//    List<Resume> findByUser(User user);
}
