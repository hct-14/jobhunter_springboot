package job_hunter.hct_14.service;

import jakarta.transaction.Transactional;
import job_hunter.hct_14.entity.Company;
import job_hunter.hct_14.entity.response.ResCreateUserDTO;
import job_hunter.hct_14.entity.response.ResUpdateUserDTO;
import job_hunter.hct_14.entity.response.ResUserDTO;
import job_hunter.hct_14.entity.response.ResultPaginationDTO;
import job_hunter.hct_14.entity.User;
import job_hunter.hct_14.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService{

    private final UserRepository userRepository;
    private final CompanyService companyService;


    public UserService(UserRepository userRepository, CompanyService companyService){
        this.userRepository = userRepository;
        this.companyService = companyService;
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

        return this.userRepository.save(user);

    }
    public void deleteById(int id){

        this.userRepository.deleteById(id);
    }
    public User findById(int id){
        Optional<User> userOptional = this.userRepository.findById(id);
        if (userOptional.isPresent()){
            return userOptional.get();
        }
        return null;
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
        List<ResUserDTO> listUser = pageUser.getContent()

                .stream().map(item ->new ResUserDTO(
                        item.getId(),
                        item.getName(),
                        item.getEmail(),
                        item.getGender(),
                        item.getAddress(),
                        item.getAge(),
                        item.getCreatedAt(),
                        item.getUpdatedAt(),new ResUserDTO.CompanyUser(
                        item.getCompany() != null ? item.getCompany().getId() : 0,
                        item.getCompany() != null ? item.getCompany().getName() : null)))
                .collect(Collectors.toList());


        rs.setResult(listUser);

        return rs;
    }
//    @Transactional
public User updateUser(User requestUser) {
    User currentUser = this.findById(requestUser.getId());
    if (currentUser != null) {
        currentUser.setAddress(requestUser.getAddress());
        currentUser.setGender(requestUser.getGender());
        currentUser.setAge(requestUser.getAge());
        currentUser.setName(requestUser.getName());

        // update
        currentUser = this.userRepository.save(currentUser);
    }
    return currentUser;
}


    public User handleGetUserByUsername(String username) {
        return this.userRepository.findByEmail(username);
    }
    public ResCreateUserDTO converToResCreateUserDTO(User user){
        ResCreateUserDTO res = new ResCreateUserDTO();
        ResCreateUserDTO.CompanyUser companyUser = new ResCreateUserDTO.CompanyUser();
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
        ResUserDTO.CompanyUser companyUser = new ResUserDTO.CompanyUser();
        if (user.getCompany() != null){
            companyUser.setId(user.getCompany().getId());
            companyUser.setName(user.getCompany().getName());
            res.setCompany(companyUser);
        }

        return  res;
    }
    public ResUpdateUserDTO converToResUpdateUserDTO(User user) {
        ResUpdateUserDTO res = new ResUpdateUserDTO();

        // Đặt thông tin công ty nếu tồn tại
        if (user.getCompany() != null) {
            ResUpdateUserDTO.CompanyUser companyUser = new ResUpdateUserDTO.CompanyUser();
            companyUser.setId(user.getCompany().getId());
            companyUser.setName(user.getCompany().getName());
            res.setCompanyUser(companyUser);
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



}
