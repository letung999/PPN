package com.ppn.ppn.controller;

import com.ppn.ppn.config.security.CustomUserDetails;
import com.ppn.ppn.config.security.JwtTokenProvider;
import com.ppn.ppn.entities.Users;
import com.ppn.ppn.payload.LoginRequest;
import com.ppn.ppn.payload.LoginResponse;
import com.ppn.ppn.service.UsersServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static com.ppn.ppn.constant.MessageStatus.ERR_MSG_SOME_THING_WENT_WRONG;
import static com.ppn.ppn.constant.MessageStatus.ERR_MSG_UNAUTHENTICATED_ACCESS;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private UsersServiceImpl usersService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping({"/login"})
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        Users user = new Users();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        Users userCheckLogin = usersService.checkLogin(user);
        try {
            if (userCheckLogin != null) {
                Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
                String jwt = jwtTokenProvider.generateToken((CustomUserDetails) authentication.getPrincipal());
                Map<String, Object> response = new HashMap<>();
                response.put("accessToken", jwt);
                response.put("user", userCheckLogin);

                LoginResponse<Map<String, Object>> resultData = new LoginResponse<>(HttpStatus.OK, response);
                return ResponseEntity.ok(resultData);

            } else {
                return ResponseEntity.ok(new LoginResponse(HttpStatus.BAD_REQUEST, ERR_MSG_UNAUTHENTICATED_ACCESS));
            }
        } catch (Exception ex) {
            return ResponseEntity.ok(new LoginResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, ERR_MSG_SOME_THING_WENT_WRONG));
        }
    }
}
