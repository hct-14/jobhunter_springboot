package job_hunter.hct_14.controller;

import job_hunter.hct_14.entity.User;
import job_hunter.hct_14.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String test(){
        return "hoang cong thanh dep trai ";
    }
//    @GetMapping()
    @PostMapping("/user/create")
    public User createNewUser(@RequestBody User postManUser){

//
//        User user = new User();
//        user.setEmail("hoangthanghgolee@gmail");
//        user.setName("hoang cong thanh");
//        user.setPassWord("123456");
        User createUser = this.userService.handleCreateUser(postManUser);
        return createUser;
    }
}
