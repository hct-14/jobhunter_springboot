package job_hunter.hct_14.service;

import jakarta.transaction.Transactional;
import job_hunter.hct_14.entity.User;
import job_hunter.hct_14.repository.UserRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class UserService{

    private final UserRepository userRepository;


    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    public User handleCreateUser(User user){

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
    public List<User> findbyAllUser(){

        return this.userRepository.findAll();
    }
    @Transactional
    public User updateUser(int id ,User requestUser) {
        // Tìm người dùng hiện tại bằng ID từ requestUser
        User existingUser = this.findById(requestUser.getId());
        if (existingUser != null) {
            // Cập nhật thông tin của người dùng hiện tại bằng các giá trị từ requestUser
            existingUser.setName(requestUser.getName());
            existingUser.setEmail(requestUser.getEmail());
            existingUser.setPassWord(requestUser.getPassWord());
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


}
