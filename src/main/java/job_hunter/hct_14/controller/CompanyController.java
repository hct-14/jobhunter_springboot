package job_hunter.hct_14.controller;


import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import job_hunter.hct_14.entity.Company;
import job_hunter.hct_14.entity.DTO.ResultPaginationDTO;
import job_hunter.hct_14.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CompanyController {


    @Autowired
    private CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/Company")
    public ResponseEntity<Company> addCompany(@Valid @RequestBody Company company) {
        Company createCompany = this.companyService.createCompany(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(createCompany);
    }
    @GetMapping("/Company")
    public ResponseEntity<ResultPaginationDTO> getAllCompany(@Filter Specification<Company> spec, Pageable pageable) {
//        String sCurent = curentOptinal.isPresent() ? curentOptinal.get() : "";
//        String sPageSize = pageSizeOptinal.isPresent() ? pageSizeOptinal.get() : "";
//        int current = Integer.parseInt(sCurent);
//        int pageSize = Integer.parseInt(sPageSize);
//        Pageable pageable = PageRequest.of(current - 1, pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(this.companyService.getAllCompanies(spec,pageable));
    }

    @PutMapping("/Company/{id}")
    public ResponseEntity<Company> updateCompany(@PathVariable int id, @Valid @RequestBody Company company) {
        Company updateCompany = this.companyService.updateCompany(id, company);
        return ResponseEntity.status(HttpStatus.OK).body(updateCompany);
    }
    @DeleteMapping("/Company/{id}")
    public ResponseEntity<String> deleteCompany(@PathVariable int id) {
        this.companyService.deleteCompany(id);
        return ResponseEntity.status(HttpStatus.OK).body("xoa oke");
    }
//    @GetMapping("/Company")
//    public ResponseEntity<Company> findByAllCompany(Company company) {
//        return  ResponseEntity.status(HttpStatus.OK).body()
//    }
}
