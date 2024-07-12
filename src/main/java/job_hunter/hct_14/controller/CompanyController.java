package job_hunter.hct_14.controller;


import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import job_hunter.hct_14.entity.Company;
import job_hunter.hct_14.entity.User;
import job_hunter.hct_14.entity.response.ResUpdateCom;
import job_hunter.hct_14.entity.response.ResultPaginationDTO;
import job_hunter.hct_14.service.CompanyService;
import job_hunter.hct_14.util.error.IdInvaldException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")

public class CompanyController {


    @Autowired
    private CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    public ResponseEntity<Company> addCompany(@Valid @RequestBody Company company) {
        Company createCompany = this.companyService.createCompany(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(createCompany);
    }
    @GetMapping("/companies")
    public ResponseEntity<ResultPaginationDTO> getAllCompany(@Filter Specification<Company> spec, Pageable pageable) {
//
        return ResponseEntity.status(HttpStatus.OK).body(this.companyService.getAllCompanies(spec,pageable));
    }

    @PutMapping("/companies")
//    public ResponseEntity<ResUpdateCom> updateCompany(@Valid @RequestBody Company reqCompany) throws IdInvaldException {
     public ResponseEntity<Company> updateCompany(@Valid @RequestBody Company reqCompany)throws IdInvaldException {
        Company updatedCompany = this.companyService.updateCompany(reqCompany);
        return ResponseEntity.ok(updatedCompany);
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<String> deleteCompany(@PathVariable int id) {
        this.companyService.deleteCompany(id);
        return ResponseEntity.status(HttpStatus.OK).body("xoa oke");
    }
//    @GetMapping("/Company")
//    public ResponseEntity<Company> findByAllCompany(Company company) {
//        return  ResponseEntity.status(HttpStatus.OK).body()
//    }
}
