package job_hunter.hct_14.repository;

import job_hunter.hct_14.entity.Company;
import job_hunter.hct_14.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobReponsetory extends JpaRepository<Job,Integer>, JpaSpecificationExecutor<Job> {
    List<Job> findByCompany(Optional<Company> company);


}
