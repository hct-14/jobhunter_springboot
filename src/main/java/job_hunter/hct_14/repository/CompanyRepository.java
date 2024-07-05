package job_hunter.hct_14.repository;

import job_hunter.hct_14.entity.Company;
import job_hunter.hct_14.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CompanyRepository extends JpaRepository<Company, Integer>, JpaSpecificationExecutor<Company> {
//    User findByEmail(String email);

}
