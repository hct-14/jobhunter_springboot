package job_hunter.hct_14.controller;

import jakarta.validation.Valid;
import job_hunter.hct_14.entity.DTO.LoginDTO;
import job_hunter.hct_14.entity.DTO.ResLoginDTO;
import job_hunter.hct_14.entity.User;
import job_hunter.hct_14.service.UserService;
import job_hunter.hct_14.util.SercuryUtil;
import job_hunter.hct_14.util.annotation.ApiMessage;
import job_hunter.hct_14.util.error.IdInvaldException;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SercuryUtil sercuryUtil;
    private final UserService userService;
    @Value("${hct_14.jwt.refresh-token-validity-in-seconds}")
    private long JwtExpirationRefreshToken;
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

            // create a token set thong tin nguoi dung dang nhap vao context

            SecurityContextHolder.getContext().setAuthentication(authentication);

            ResLoginDTO res = new ResLoginDTO();
            User currentUserDB = this.userService.handleGetUserByUsername(loginDto.getUsername());
            if (currentUserDB !=null){
                ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
                        currentUserDB.getId(),
                        currentUserDB.getEmail(),
                        currentUserDB.getName());
                res.setUser(userLogin);

            }
//            ResLoginDTO res = new ResLoginDTO();
//            ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(1, " abc", " xyz");
//            res.setUser(userLogin);

//            create refresh token
            String access_token = this.sercuryUtil.CreateAccsessToken(authentication.getName(), res.getUser());

            res.setAccessToken(access_token);
            //update user refresh token
            String refresh_token = this.sercuryUtil.CreateRefreshToken(loginDto.getUsername(), res);

            this.userService.updateUserToken(refresh_token, loginDto.getUsername());


            //set cookie
            ResponseCookie responseCookie = ResponseCookie.from("refresh_token", refresh_token)
                    .httpOnly(true)
                    .secure(true)
//                    .domain("/")
                    .maxAge(JwtExpirationRefreshToken)
                    .path("/")
                    .build();
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,responseCookie.toString())
                                        .body(res);
        }


     @GetMapping("/account")
        @ApiMessage("fetch account")
     public ResponseEntity<ResLoginDTO.UserGetAccount> getAccount() {
         String email = SercuryUtil.getCurrentUserLogin().isPresent()
                 ? SercuryUtil.getCurrentUserLogin().get()
                 : "";

         User currentUserDB = this.userService.handleGetUserByUsername(email);
         ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin();
         ResLoginDTO.UserGetAccount userGetAccount = new ResLoginDTO.UserGetAccount();

         if (currentUserDB != null) {
             userLogin.setId(currentUserDB.getId());
             userLogin.setEmail(currentUserDB.getEmail());
             userLogin.setName(currentUserDB.getName());
             userGetAccount.setUser(userLogin);
         }

         return ResponseEntity.ok().body(userGetAccount);
     }

    @ApiMessage("get user by refresh token")
    @GetMapping("/refresh")
    public ResponseEntity<ResLoginDTO> getRefreshToken(
            @CookieValue(name = "refresh_token", defaultValue = "abc") String refresh_token) throws IdInvaldException {
        if (refresh_token.equals("abc")) {
            throw new IdInvaldException("Bạn không có refresh token ở cookie");
        }
        // check valid
        Jwt decodedToken = this.sercuryUtil.checkValidRefreshToken(refresh_token);
        String email = decodedToken.getSubject();

        // check user by token + email
        User currentUser = this.userService.getUserByRefreshTokenAndEmail(refresh_token, email);
        if (currentUser == null) {
            throw new IdInvaldException("Refresh Token không hợp lệ");
        }

        // issue new token/set refresh token as cookies
        ResLoginDTO res = new ResLoginDTO();
        User currentUserDB = this.userService.handleGetUserByUsername(email);
        if (currentUserDB != null) {
            ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
                    currentUserDB.getId(),
                    currentUserDB.getEmail(),
                    currentUserDB.getName());
            res.setUser(userLogin);
        }

        // create access token
        String access_token = this.sercuryUtil.CreateAccsessToken(email, res.getUser());
        res.setAccessToken(access_token);

        // create refresh token
        String new_refresh_token = this.sercuryUtil.CreateRefreshToken(email, res);

        // update user
        this.userService.updateUserToken(new_refresh_token, email);

        // set cookies
        ResponseCookie resCookies = ResponseCookie
                .from("refresh_token", new_refresh_token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(JwtExpirationRefreshToken)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resCookies.toString())
                .body(res);
    }

    @PostMapping("/logout")
    @ApiMessage("logout access")
    public ResponseEntity<Void> logout()throws IdInvaldException {
        String email = SercuryUtil.getCurrentUserLogin().isPresent()
                ? SercuryUtil.getCurrentUserLogin().get()
                : "";
        if (email.equals("")) {
            throw new IdInvaldException("token khong hop le");
        }
        this.userService.updateUserToken(null,email);
        ///remove  refresh token cookie
        ResponseCookie deleteSpringCookie = ResponseCookie
                .from("refresh_token", null)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, deleteSpringCookie.toString()).body(null);

    }
}






