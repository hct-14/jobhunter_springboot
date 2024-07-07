package job_hunter.hct_14.controller;

import jakarta.validation.Valid;
import job_hunter.hct_14.entity.DTO.LoginDTO;
import job_hunter.hct_14.entity.DTO.ResLoginDTO;
import job_hunter.hct_14.entity.User;
import job_hunter.hct_14.service.UserService;
import job_hunter.hct_14.util.SercuryUtil;
import job_hunter.hct_14.util.error.IdInvaldException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SercuryUtil sercuryUtil;
    private final UserService userService;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SercuryUtil sercuryUtil, UserService userService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.sercuryUtil = sercuryUtil;
        this.userService = userService;
    }
//    private final AuthenticationManagerBuilder authenticationManagerBuilder;
        @PostMapping("/login")
        public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody LoginDTO loginDto) throws IdInvaldException {
            // Nạp input gồm username/password vào Security
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginDto.getUsername(), loginDto.getPassword());

            // xác thực người dùng => cần viết hàm loadUserByUsername
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            // create a token
            String access_token = this.sercuryUtil.CreateAccsessToken(authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            ResLoginDTO res = new ResLoginDTO();
            User currentUserDB = this.userService.handleGetUserByUsername(loginDto.getUsername());
            if (currentUserDB !=null){
                ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(currentUserDB.getId(),
                        currentUserDB.getEmail(),currentUserDB.getName());
                res.setUser(userLogin);

            }
//            ResLoginDTO res = new ResLoginDTO();
//            ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(1, " abc", " xyz");
//            res.setUser(userLogin);

//            create refresh token
            String refresh_token = this.sercuryUtil.CreateRefreshToken(loginDto.getUsername(), res);
            res.setAccesstoken(access_token);
            //update user refresh token
            this.userService.updateUserToken(refresh_token, loginDto.getUsername());


            //set cookie
            ResponseCookie responseCookie = ResponseCookie.from("refresh_tokeb", refresh_token)
                    .httpOnly(true)
                    .secure(true)
//                    .domain("/")
                    .maxAge(60)
                    .path("/")
                    .build();
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,responseCookie.toString())
                                        .body(res);
        }
}
