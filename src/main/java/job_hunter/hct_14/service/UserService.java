package job_hunter.hct_14.service;

import job_hunter.hct_14.controller.ResumeController;
import job_hunter.hct_14.entity.*;
import job_hunter.hct_14.entity.response.ResCreateUserDTO;
import job_hunter.hct_14.entity.response.ResUpdateUserDTO;
import job_hunter.hct_14.entity.response.ResUserDTO;
import job_hunter.hct_14.entity.response.ResultPaginationDTO;
import job_hunter.hct_14.repository.CompanyRepository;
import job_hunter.hct_14.repository.ResumeReponsetory;
import job_hunter.hct_14.repository.UserRepository;
import job_hunter.hct_14.util.error.IdInvaldException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService{
@Autowired
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final ResumeReponsetory resumeReponsetory;
    public final CompanyService companyService;
    private final ResumeService resumeService;
    private final ResumeController resumeController;
    private final RoleService roleService;


    public UserService(UserRepository userRepository, CompanyRepository companyRepository,
                       ResumeReponsetory resumeReponsetory, CompanyService companyService,
                       ResumeService resumeService, ResumeController resumeController, RoleService roleService){
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.resumeReponsetory = resumeReponsetory;
        this.companyService =companyService;
        this.resumeService =resumeService ;
        this.resumeController = resumeController;
        this.roleService = roleService;
    }

    public boolean isEmailExist(String email){
        return this.userRepository.existsByEmail(email);
    }
//    public User findByEmail()
    //create
    public User handleCreateUser(User user) {

        if (user.getCompany() !=null){
            Optional<Company> companyOptional = this.companyService.findById(user.getCompany().getId());
            user.setCompany(companyOptional.isPresent() ? companyOptional.get() : null);

        }
        if (user.getRole() != null){
            Optional<Role> role = this.roleService.findById(user.getRole().getId());
            user.setRole(role.isPresent() ? role.get() : null);
        }

        return this.userRepository.save(user);

    }
    public User handleRegister(User user) {
        return this.userRepository.save(user);

    }



    public Optional<User> findById(int id){
            return this.userRepository.findById(id);
    }
//    public User findByEmail (String email){
//        Optional<User> userOptional = this.userRepository.findByEmail(email);
//        if (userOptional.isPresent()){
//            return userOptional.get();
//        }
//    }
    public ResultPaginationDTO findbyAllUser(Specification<User> spec, Pageable pageable){
        Page<User> pageUser = this.userRepository.findAll(spec,pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalPages());

        rs.setMeta(mt);

        List<ResUserDTO> listUser = pageUser.getContent()

                .stream().map(item ->new ResUserDTO(
                        item.getId(),
                        item.getName(),
                        item.getEmail(),
                        item.getGender(),
                        item.getAddress(),
                        item.getAge(),
                        item.getCreatedAt(),
                        item.getUpdatedAt(),
                        new ResUserDTO.Role(
                                item.getRole() != null ? item.getRole().getId() : 0,
                                item.getRole() != null ? item.getRole().getName() : null),

                        new ResUserDTO.CompanyUser(
                        item.getCompany() != null ? item.getCompany().getId() : 0,
                        item.getCompany() != null ? item.getCompany().getName() : null)
    ))
                .collect(Collectors.toList());


        rs.setResult(listUser);

        return rs;
    }
//    @Transactional
public User handleUpdateUser(User userFe, User userBe) {
    if (userFe == null || userBe == null) {
        throw new IllegalArgumentException("User to update or existing user cannot be null");
    }

    // Kiểm tra xem người dùng có tồn tại không
    Optional<User> userOptional = this.userRepository.findById(userFe.getId());
    if (!userOptional.isPresent()) {
        throw new IllegalArgumentException("User not found");
    }

    // Cập nhật các trường chỉ khi có giá trị mới
    if (userFe.getAddress() != null) {
        userBe.setAddress(userFe.getAddress());
    }
    if (userFe.getAge() > 0) { // Kiểm tra tuổi hợp lệ
        userBe.setAge(userFe.getAge());
    }
    if (userFe.getEmail() != null && !userFe.getEmail().isEmpty()) {
        userBe.setEmail(userFe.getEmail());
    }
    if (userFe.getGender() != null) {
        userBe.setGender(userFe.getGender());
    }
    if (userFe.getName() != null) {
        userBe.setName(userFe.getName());
    }
    if (userFe.getPassword() != null) {
        userBe.setPassword(userFe.getPassword());
    }
    if (userFe.getRole() !=null){
        userBe.setRole(userFe.getRole());
    }
//    if (userFe.getRole() !=null){
//        for(Role role : userFe.getRole()){
//
//        }
//
//    }
    if (userFe.getCompany() != null) {
        Company company = userFe.getCompany();
        if (company.getId() > 0) {
            // Công ty đã tồn tại, lấy từ cơ sở dữ liệu
            Optional<Company> companyOptional = this.companyService.findById(company.getId());
            if (companyOptional.isPresent()) {
                userBe.setCompany(companyOptional.get());
            } else {
                // Công ty không tồn tại, có thể lưu hoặc xử lý tùy ý
                userBe.setCompany(null);
            }
        } else {
            // Nếu không có công ty, đặt giá trị null
            userBe.setCompany(null);
        }
 }

        return this.userRepository.save(userBe);
    }

    public void deleteById(int id) throws IdInvaldException {
        // Tìm người dùng theo ID
        Optional<User> userOptional = this.userRepository.findById(id);

        // Nếu người dùng không tồn tại, ném ra ngoại lệ
        if (!userOptional.isPresent()) {
            throw new IdInvaldException("Người dùng không tồn tại");
        }

        User user = userOptional.get();

        // Xóa các bản ghi liên quan trong bảng resume
        if (user.getResumes() != null) {
            for (Resume resume : user.getResumes()) {
                // Xóa bản ghi resume
                this.resumeReponsetory.delete(resume);
            }
        }
        if (user.getRole() != null){
            user.setRole(null);
            this.userRepository.save(user);
        }

        // Xử lý công ty liên kết (nếu có)
        if (user.getCompany() != null) {
            // Gỡ liên kết công ty
            user.setCompany(null);
            // Lưu lại thay đổi của người dùng
            this.userRepository.save(user);
        }

        // Xóa người dùng
        this.userRepository.deleteById(id);
    }


    public User handleGetUserByUsername(String username) {
        return this.userRepository.findByEmail(username);
    }
    public ResCreateUserDTO converToResCreateUserDTO(User user){
        ResCreateUserDTO res = new ResCreateUserDTO();
        ResCreateUserDTO.CompanyUser companyUser = new ResCreateUserDTO.CompanyUser();
        ResCreateUserDTO.Role role =  new ResCreateUserDTO.Role();
        res.setId(user.getId());
        res.setName(user.getName());
        res.setGender(user.getGender());
        res.setEmail(user.getEmail());
        res.setAge(user.getAge());
        res.setCreatedAt(user.getCreatedAt());
        res.setAddress(user.getAddress());
//        res.setGender();
        if (user.getCompany() != null){
            companyUser.setId(user.getCompany().getId());
            companyUser.setName(user.getCompany().getName());
            res.setCompanyUser(companyUser);
        }
        if (user.getRole() != null){
            role.setId(user.getRole().getId());
            role.setName(user.getRole().getName());
            res.setRole(role);
        }

        return  res;
    }
    public ResUserDTO converToResUserDTO(User user){
        ResUserDTO res = new ResUserDTO();

        res.setId(user.getId());
        res.setName(user.getName());
        res.setGender(user.getGender());
        res.setEmail(user.getEmail());
        res.setAge(user.getAge());
        res.setCreatedAt(user.getCreatedAt());
        res.setAddress(user.getAddress());
        res.setGender(user.getGender());
        res.setUpdatedAt(user.getUpdatedAt());
        res.setCreatedAt(user.getCreatedAt());
//        ResUserDTO
        ResUserDTO.CompanyUser companyUser = new ResUserDTO.CompanyUser();
        if (user.getCompany() != null){
            companyUser.setId(user.getCompany().getId());
            companyUser.setName(user.getCompany().getName());
            res.setCompany(companyUser);
        }
        new ResUserDTO.Role(
                res.getRole() != null ? res.getRole().getId() : 0,
                res.getRole() != null ? res.getRole().getName() : null);


        return  res;
    }
    public ResUpdateUserDTO converToResUpdateUserDTO(User user) {
        ResUpdateUserDTO res = new ResUpdateUserDTO();

        ResUpdateUserDTO.CompanyUser companyUser = new ResUpdateUserDTO.CompanyUser();

        // Đặt thông tin công ty nếu tồn tại
        if (user.getCompany() != null) {
            companyUser.setId(user.getCompany().getId());
            companyUser.setName(user.getCompany().getName());
            res.setCompanyUser(companyUser);
        }
        ResUpdateUserDTO.RoleUser role = new ResUpdateUserDTO.RoleUser();
        if (user.getRole() != null){
            role.setId(user.getRole().getId());
            role.setName(user.getRole().getName());
            res.setRoleUser(role);
        }

        // Đặt thông tin người dùng
        res.setId(user.getId());
        res.setName(user.getName());
        res.setGender(user.getGender());
        res.setEmail(user.getEmail());
        res.setAge(user.getAge());
        res.setCreatedAt(user.getCreatedAt());
        res.setAddress(user.getAddress());
        res.setUpdatedAt(user.getUpdatedAt());

        return res;
    }

    public void updateUserToken(String token, String email){
        User currentUser = this.handleGetUserByUsername(email);
        if(currentUser != null){
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }
    public User getUserByRefreshTokenAndEmail(String token, String email) {
        return this.userRepository.findByRefreshTokenAndEmail(token, email);
    }


//    public List<User> findByCompany(Company company) {
//        return this.userRepository.findByCompany(company);
//    }


}
