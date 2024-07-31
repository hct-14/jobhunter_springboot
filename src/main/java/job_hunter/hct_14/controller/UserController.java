package job_hunter.hct_14.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import job_hunter.hct_14.entity.response.ResCreateUserDTO;
import job_hunter.hct_14.entity.response.ResUpdateUserDTO;
import job_hunter.hct_14.entity.response.ResUserDTO;
import job_hunter.hct_14.entity.response.ResultPaginationDTO;
import job_hunter.hct_14.entity.User;
import job_hunter.hct_14.service.UserService;
import job_hunter.hct_14.util.annotation.ApiMessage;
import job_hunter.hct_14.util.error.IdInvaldException;
//import job_hunter.hct_14.util.error.UpdateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class UserController {
//    @ExceptionHandler(value = IdInvaldException.class)
//    public ResponseEntity<String> handleIdInvaldException(IdInvaldException idInvaldException) {
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(idInvaldException.getMessage());
//    }

    @Autowired
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
//    @CrossOrigin
    public String test() throws IdInvaldException {

        return "hoang cong thanh dep trai ";
    }
//    @GetMapping()
    @PostMapping("/users")
    @ApiMessage("Create a new user")
    public ResponseEntity<ResCreateUserDTO> createNewUser(@Valid @RequestBody User postManUser) throws IdInvaldException{
        boolean isEmailExist = this.userService.isEmailExist(postManUser.getEmail());
        if (isEmailExist){
            throw  new IdInvaldException(
                    "Email " + postManUser.getEmail() + " đã tồn tai rồi em, nhập email khác đi"
            );
        }
        String hashPassword = passwordEncoder.encode(postManUser.getPassword());
        postManUser.setPassword(hashPassword);
        User createUser = this.userService.handleCreateUser(postManUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.converToResCreateUserDTO(createUser));
    }
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@Valid @PathVariable int id)
        throws IdInvaldException{
        Optional<User> currentUser = this.userService.findById(id);
        if (currentUser.isPresent()) {
            //            this.companyService.handleDeleteCompany(id);
            this.userService.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("xoa oke");
        }
        throw  new IdInvaldException("id khong hop le");

    }
    @GetMapping("/users/{id}")
    public ResponseEntity<ResUserDTO> findById(@PathVariable int id) {
//        User fetchUser = this.userService.findById(id);
//        return ResponseEntity.status(HttpStatus.OK).body(fetchUser);
        try {
            Optional<User> fetchUser = this.userService.findById(id);
            if (fetchUser.isPresent()) {
                return ResponseEntity.status(HttpStatus.OK).body(this.userService.converToResUserDTO(fetchUser.orElse(null)));
            } else {
                // Nếu không tìm thấy người dùng, ném ngoại lệ IdInvalidException
                throw new IdInvaldException("Id không hợp lệ");
            }
        } catch (IdInvaldException e) {
            // Nếu Id không hợp lệ, trả về mã trạng thái HTTP 400 (Bad Request) và thông báo lỗi
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (java.lang.Exception e) {
            // Nếu xảy ra lỗi khác, trả về mã trạng thái HTTP 500 (Internal Server Error) và thông báo lỗi
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping("/users")
    @ApiMessage("fetch all users")
    public ResponseEntity<ResultPaginationDTO> getAllUser(@Filter Specification<User> spec, Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(this.userService.findbyAllUser(spec, pageable));
    }


    @PutMapping("/users")
    @ApiMessage("Update a user")
    public ResponseEntity<ResUpdateUserDTO> updateUser(@Valid @RequestBody User user) throws IdInvaldException {
        Optional<User> ericUser = this.userService.findById(user.getId());
        if (!ericUser.isPresent()) {
            throw new IdInvaldException("User với id = " + user.getId() + " không tồn tại");
        }
        User userSave = this.userService.handleUpdateUser(user, ericUser.get());
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.converToResUpdateUserDTO(userSave));
    }


}
