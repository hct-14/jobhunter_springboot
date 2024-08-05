package job_hunter.hct_14.repository;

import job_hunter.hct_14.entity.Subscribers;
import job_hunter.hct_14.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SubscribersRepository extends JpaRepository<Subscribers, Integer>, JpaSpecificationExecutor<Subscribers> {
//   boolean existsByNameEmail
boolean existsByEmail(String email);

    Subscribers findByEmail(String email);

}
