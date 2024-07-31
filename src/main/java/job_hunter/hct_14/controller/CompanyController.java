package job_hunter.hct_14.controller;


import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import job_hunter.hct_14.entity.Company;
import job_hunter.hct_14.entity.response.ResultPaginationDTO;
import job_hunter.hct_14.service.CompanyService;
import job_hunter.hct_14.util.error.IdInvaldException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
    @GetMapping("/companies/{id}")
    public ResponseEntity<Optional<Company>> getSigleCompany(@PathVariable int id){
     Optional<Company> company = this.companyService.findById(id);
     return ResponseEntity.status(HttpStatus.OK).body(company);
    }

    @PutMapping("/companies")
//    public ResponseEntity<ResUpdateCom> updateCompany(@Valid @RequestBody Company reqCompany) throws IdInvaldException {
     public ResponseEntity<Company> updateCompany(@Valid @RequestBody Company reqCompany)throws IdInvaldException {
        Optional<Company> updatedCompany = this.companyService.findById(reqCompany.getId());
        if (updatedCompany.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(this.companyService.updateCompany(reqCompany, updatedCompany.get()));

        }
        return null;
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<String> deleteCompany(@PathVariable int id) throws IdInvaldException{

            this.companyService.handleDeleteCompany(id);
            return ResponseEntity.status(HttpStatus.OK).body("xoa oke");

    }

}
