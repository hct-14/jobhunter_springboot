package job_hunter.hct_14.controller;

import jakarta.validation.Valid;
import job_hunter.hct_14.dto.LoginDTO;
import job_hunter.hct_14.dto.ResLoginDTO;
import job_hunter.hct_14.util.SercuryUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SercuryUtil sercuryUtil;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SercuryUtil sercuryUtil) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.sercuryUtil = sercuryUtil;
    }
//    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    @PostMapping("/login")
    public ResponseEntity<String> Login(@Valid @RequestBody LoginDTO loginDto){
//        UsernamePasswordAuthenticationToken(loginDto.setUsername(), loginDto.getPassword());
        UsernamePasswordAuthenticationToken authencationToken = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

//        loadbyUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authencationToken);
//createtoken
       String access_token = this.sercuryUtil.CreateToken(authentication);
       ResLoginDTO res = new ResLoginDTO();
       res.setAccesstoken(access_token);
        return ResponseEntity.ok().body(access_token);
    }
}
