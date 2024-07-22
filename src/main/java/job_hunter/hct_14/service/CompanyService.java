package job_hunter.hct_14.service;

import jakarta.transaction.Transactional;
import job_hunter.hct_14.entity.Company;
//import job_hunter.hct_14.entity.DTO.Meta;
import job_hunter.hct_14.entity.User;
import job_hunter.hct_14.entity.response.ResCompanyDTO;
import job_hunter.hct_14.entity.response.ResUpdateCom;
import job_hunter.hct_14.entity.response.ResultPaginationDTO;
import job_hunter.hct_14.repository.CompanyRepository;
import job_hunter.hct_14.repository.UserRepository;
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
    public CompanyService(CompanyRepository companyRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
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
//        Optional<User> userOptional = this.userRepository.findById(id);
//        if (userOptional.isPresent()){
//            return userOptional.get();
//        }
//        return null;
//    }
    }

//    public ResultPaginationDTO getAllCompanies(Specification<Company> spec, Pageable pageable){
////        return this.companyRepository.findAll(pageable);
//        Page<Company> pageCompany = this.companyRepository.findAll(pageable);
//        ResultPaginationDTO rs = new ResultPaginationDTO();
//        Meta mt = new Meta();
//        mt.setPage(pageCompany.getNumber() + 1);
//        mt.setPageSize(pageCompany.getSize());
//        mt.setPages(pageCompany.getTotalPages());
//        mt.setTotal(pageCompany.getTotalPages());
//        rs.setMeta(mt);
//        rs.setResult(pageCompany.getContent());
//
//        return rs;
//
//    }

    public ResultPaginationDTO getAllCompanies(Specification<Company> spec, Pageable pageable){
        Page<Company> pageCompany = this.companyRepository.findAll(spec,pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageCompany.getTotalPages());
        mt.setTotal(pageCompany.getTotalPages());

        rs.setMeta(mt);
//        List<ResUserDTO> listUser = pageUser.getContent();
//        List<User> userList = pageUser.getContent();
//        List<ResUserDTO> dtoList = new ArrayList<>();
//
//        for (User user : userList) {
//            ResUserDTO dto = new ResUserDTO(
//                    user.getId(),
//                    user.getName(),
//                    user.getEmail(),
//                    user.getGender(),
//                    user.getAddress(),
//                    user.getAge(),
//                    user.getCreatedAt(),
//                    user.getUpdatedAt()
//            );
//            dtoList.add(dto);
//        }
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
    public void DeleteCompany(int id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        // Tìm tất cả người dùng thuộc công ty này và đặt company = null
        List<User> users = userRepository.findByCompany(company);
        if (!users.isEmpty()) {
            users.forEach(user -> user.setCompany(null));
            userRepository.saveAll(users); // Cập nhật danh sách người dùng
        }

        // Xóa công ty
        companyRepository.delete(company);
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
