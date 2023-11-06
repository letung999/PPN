package com.ppn.ppn.controller;

import com.ppn.ppn.config.security.CustomUserDetails;
import com.ppn.ppn.config.security.JwtTokenProvider;
import com.ppn.ppn.entities.Users;
import com.ppn.ppn.payload.APIResponse;
import com.ppn.ppn.payload.LoginRequest;
import com.ppn.ppn.service.UsersServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.ppn.ppn.constant.MessageStatus.*;

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
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
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
                APIResponse apiResponse = APIResponse.builder()
                        .message(INF_MSG_SUCCESSFULLY)
                        .timeStamp(LocalDateTime.now())
                        .isSuccess(true)
                        .statusCode(200)
                        .data(response)
                        .build();
                return ResponseEntity.ok(apiResponse);
            } else {
                APIResponse apiResponse = APIResponse.builder()
                        .message(ERR_MSG_UNAUTHENTICATED_ACCESS)
                        .timeStamp(LocalDateTime.now())
                        .isSuccess(false)
                        .statusCode(400)
                        .data(null)
                        .build();
                return ResponseEntity.ok(apiResponse);
            }
        } catch (Exception ex) {
            APIResponse apiResponse = APIResponse.builder()
                    .message(ERR_MSG_SOME_THING_WENT_WRONG)
                    .timeStamp(LocalDateTime.now())
                    .isSuccess(false)
                    .statusCode(500)
                    .data(null)
                    .build();
            return ResponseEntity.ok(apiResponse);
        }
    }
}
