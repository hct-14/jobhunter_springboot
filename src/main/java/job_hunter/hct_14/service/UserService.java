package job_hunter.hct_14.service;

import jakarta.transaction.Transactional;
import job_hunter.hct_14.entity.DTO.Meta;
import job_hunter.hct_14.entity.DTO.ResCreateUserDTO;
import job_hunter.hct_14.entity.DTO.ResUserDTO;
import job_hunter.hct_14.entity.DTO.ResultPaginationDTO;
import job_hunter.hct_14.entity.User;
import job_hunter.hct_14.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService{

    private final UserRepository userRepository;


    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public boolean isEmailExist(String email){
        return this.userRepository.existsByEmail(email);
    }
//    public User findByEmail()
    //create
    public User handleCreateUser(User user) {

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
        Meta mt = new Meta();
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
                        item.getUpdatedAt()
                ) ).collect(Collectors.toList());
        rs.setResult(listUser);

        return rs;
    }
    @Transactional
    public User updateUser(User requestUser) {
        // Tìm người dùng hiện tại bằng ID từ requestUser
        User existingUser = this.findById(requestUser.getId());
        if (existingUser != null) {
            // Cập nhật thông tin của người dùng hiện tại bằng các giá trị từ requestUser
            existingUser.setName(requestUser.getName());
            existingUser.setEmail(requestUser.getEmail());
            existingUser.setPassWord(requestUser.getPassWord());
            existingUser.setAddress(requestUser.getAddress());
            existingUser.setAge(requestUser.getAge());
            existingUser.setGender(requestUser.getGender());
            // Lưu người dùng đã cập nhật vào cơ sở dữ liệu và trả về
            return this.userRepository.save(existingUser);
        } else {
            // Trả về null nếu người dùng không tồn tại
            return null;
        }
    }
    public User handleGetUserByUsername(String username) {
        return this.userRepository.findByEmail(username);
    }
    public ResCreateUserDTO converToResCreateUserDTO(User user){
        ResCreateUserDTO res = new ResCreateUserDTO();
        res.setId(user.getId());
        res.setName(user.getName());
        res.setGender(user.getGender());
        res.setEmail(user.getEmail());
        res.setAge(user.getAge());
        res.setCreatedAt(user.getCreatedAt());
        res.setAddress(user.getAddress());
//        res.setGender();

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

        return  res;
    }
    public void updateUserToken(String token, String email){
        User currentUser = this.handleGetUserByUsername(email);
        if(currentUser != null){
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }

}
