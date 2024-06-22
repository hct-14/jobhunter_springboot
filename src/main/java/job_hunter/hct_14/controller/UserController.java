package job_hunter.hct_14.controller;

import job_hunter.hct_14.entity.User;
import job_hunter.hct_14.service.UserService;
import job_hunter.hct_14.util.error.IdInvaldException;
import job_hunter.hct_14.util.error.UpdateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
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
    public String test() throws IdInvaldException {

        return "hoang cong thanh dep trai ";
    }
//    @GetMapping()
    @PostMapping("/user")
    public ResponseEntity<User> createNewUser(@RequestBody User postManUser){
        String hashPassword = this.passwordEncoder.encode(postManUser.getPassWord());
        postManUser.setPassWord(hashPassword);
        User createUser = this.userService.handleCreateUser(postManUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(createUser);
    }
    @DeleteMapping("/user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id)
        throws IdInvaldException{
        if (id >= 1500) {
            throw  new IdInvaldException("id khong hop le");
        }
        this.userService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("xoa oke");

    }
    @GetMapping("/user/find/{id}")
    public ResponseEntity<User> findById(@PathVariable int id) {
//        User fetchUser = this.userService.findById(id);
//        return ResponseEntity.status(HttpStatus.OK).body(fetchUser);
        try {
            User fetchUser = this.userService.findById(id);
            if (fetchUser != null) {
                return ResponseEntity.status(HttpStatus.OK).body(fetchUser);
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

    @GetMapping("/user/findall")
    public ResponseEntity<List<User>> getAllUser() {
//        return this.userService.findbyAllUser();
//        User findAll = (User) this.userService.findbyAllUser();
//        if (findAll != null) {
//            return ResponseEntity.status(HttpStatus.OK).body(this.userService.findbyAllUser());
//        }else {
//            throw new FindAllError("khong co user nao ca");
//        }

        return ResponseEntity.status(HttpStatus.OK).body(this.userService.findbyAllUser());
    }
    @PutMapping("/user/{id}")
        public ResponseEntity<User> updateUser(@PathVariable int id, @RequestBody User user) throws UpdateException {
            User userupdate = this.userService.updateUser(id, user);
            if (userupdate != null) {
                return ResponseEntity.status(HttpStatus.OK).body(userupdate);

            } else {
              throw new UpdateException("khong co user nao ca");

            }
    //        return userupdate;
    //        return ResponseEntity.status(HttpStatus.OK).body(userupdate);
        }

}
