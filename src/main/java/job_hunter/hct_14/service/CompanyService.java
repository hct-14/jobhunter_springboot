package job_hunter.hct_14.service;

import job_hunter.hct_14.entity.Company;
import job_hunter.hct_14.entity.DTO.Meta;
import job_hunter.hct_14.entity.DTO.ResultPaginationDTO;
import job_hunter.hct_14.repository.CompanyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
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

    public Company findId(int id){
        Optional<Company> company = this.companyRepository.findById(id);
        if(company.isPresent()){
            return company.get();
        }
        return null;
    }
    public ResultPaginationDTO getAllCompanies(Specification<Company> spec, Pageable pageable){
//        return this.companyRepository.findAll(pageable);
        Page<Company> pageCompany = this.companyRepository.findAll(pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        Meta mt = new Meta();
        mt.setPage(pageCompany.getNumber() + 1);
        mt.setPageSize(pageCompany.getSize());
        mt.setPages(pageCompany.getTotalPages());
        mt.setTotal(pageCompany.getTotalPages());
        rs.setMeta(mt);
        rs.setResult(pageCompany.getContent());

        return rs;

    }
    public Company updateCompany(int id,Company Updatecompany){
        Optional<Company> exitscompany = this.companyRepository.findById(id);
        if(exitscompany.isPresent()){
            exitscompany.get().setName(Updatecompany.getName());
            exitscompany.get().setAddress(Updatecompany.getAddress());
            exitscompany.get().setLogo(Updatecompany.getLogo());
            exitscompany.get().setDescription(Updatecompany.getDescription());
            return this.companyRepository.save(exitscompany.get());
        }
        return null;
    }

    public void DeleteCompany(int id){
        Optional<Company> company = this.companyRepository.findById(id);
        if(company.isPresent()){
            this.companyRepository.delete(company.get());
        }

    }
}
