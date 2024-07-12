package job_hunter.hct_14.repository;

import job_hunter.hct_14.entity.Company;
import job_hunter.hct_14.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Integer>, JpaSpecificationExecutor<Company> {
//    void deleteAll(List<User> users);

//    User findByEmail(String email);
    

}
