package job_hunter.hct_14.service;

import jakarta.transaction.Transactional;
import job_hunter.hct_14.entity.Company;
//import job_hunter.hct_14.entity.DTO.Meta;
import job_hunter.hct_14.entity.Job;
import job_hunter.hct_14.entity.User;
import job_hunter.hct_14.entity.response.ResCompanyDTO;
import job_hunter.hct_14.entity.response.ResUpdateCom;
import job_hunter.hct_14.entity.response.ResultPaginationDTO;
import job_hunter.hct_14.repository.CompanyRepository;
import job_hunter.hct_14.repository.JobReponsetory;
import job_hunter.hct_14.repository.UserRepository;
import job_hunter.hct_14.util.error.IdInvaldException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final JobReponsetory jobReponsetory;
    public CompanyService(CompanyRepository companyRepository, UserRepository userRepository, JobReponsetory jobReponsetory) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.jobReponsetory = jobReponsetory;
    }
    public Company createCompany(Company company){
        return this.companyRepository.save(company);
    }

    public void deleteCompany(int id){
        this.companyRepository.deleteById(id);
    }
    public Company getCompany(int id){
        Optional<Company> company = this.companyRepository.findById(id);
        if(company.isPresent()){
            return company.get();
        }
        return null;
    }

    public Optional<Company> findById(int id) {
        return this.companyRepository.findById(id);


    }

    public ResultPaginationDTO getAllCompanies(Specification<Company> spec, Pageable pageable){
        Page<Company> pageCompany = this.companyRepository.findAll(spec,pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageCompany.getTotalPages());
        mt.setTotal(pageCompany.getTotalPages());

        rs.setMeta(mt);

        List<ResCompanyDTO> listCompany = pageCompany.getContent()

                .stream().map(item ->new ResCompanyDTO(
                        item.getId(),
                        item.getName(),
                        item.getDescription(),
                        item.getAddress(),
                        item.getLogo(),
                        item.getCreatedAt(),
                        item.getUpdatedAt(),
                        item.getUpdatedBy(),
                        item.getCreatedBy()


                )).collect(Collectors.toList());
        rs.setResult(listCompany);

        return rs;
    }
    public Company updateCompany(Company companyFe, Company companyBe) {


        Optional<Company> currentCompanyOpt = this.findById(companyFe.getId());
//        if (currentCompanyOpt.isPresent()){
//           Integer reqCompahe =
//        }
        if (currentCompanyOpt.isPresent()) {
            if (companyFe.getName() != null){
                companyBe.setName(companyFe.getName());
            }
            if (companyFe.getDescription() != null){
                companyBe.setDescription(companyFe.getDescription());
            }
            if (companyFe.getLogo() != null){
                companyBe.setLogo(companyFe.getLogo());
            }
            if (companyFe.getAddress() !=null){
                companyBe.setAddress(companyFe.getAddress());
            }
//            Company currentCompany = currentCompanyOpt.get();
//            currentCompany.setName(companyFe.getName());
//            currentCompany.setDescription(companyFe.getDescription());
//            currentCompany.setLogo(companyFe.getLogo());
//            currentCompany.setAddress(companyFe.getAddress());

            // update
            return this.companyRepository.save(companyBe);
        }
        return null;
    }

    @Transactional

    public void handleDeleteCompany(int id) throws IdInvaldException {
        Optional<Company> companyCheck = companyRepository.findById(id);
        if (!companyCheck.isPresent()){
            throw new IdInvaldException("không có company này đâu em");
        }
        Company company = companyCheck.get();
        if (company.getJobs() != null){
            for (Job job : company.getJobs()){
                this.jobReponsetory.delete(job);
            }
        }
        if (company.getUsers() != null){
            for (User user: company.getUsers()){
                user.setCompany(null);
                this.userRepository.save(user);
            }
        }
        this.companyRepository.deleteById(id);
    }


    public ResUpdateCom converToResUpdateCopanyDTO(Company company) {
        ResUpdateCom res = new ResUpdateCom();

//        // Đặt thông tin user
//        if (company.getUsers()!= null) {
//            ResUpdateUserDTO.CompanyUser companyUser = new ResUpdateUserDTO.CompanyUser();
//            companyUser.setId(company.getUsers());
//            companyUser.setName(company.getUsers());
//            res.scompanyUser(companyUser);
//        }

        // Đặt thông tin người dùng
        res.setId(res.getId());
        res.setName(res.getName());
        res.setDescription(res.getDescription());
        res.setAddress(res.getAddress());
        res.setCreatedAt(res.getCreatedAt());
        res.setUpdatedAt(res.getUpdatedAt());
        res.setUpdatedBy(res.getCreatedBy());
        res.setCreatedBy(res.getCreatedBy());

        return res;
    }

}
