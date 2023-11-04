package com.ppn.ppn.controller;

import com.ppn.ppn.dto.UsersDto;
import com.ppn.ppn.payload.HtmlTemplate;
import com.ppn.ppn.payload.VerifyMailRequest;
import com.ppn.ppn.service.EmailSenderServiceImpl;
import com.ppn.ppn.service.UsersServiceImpl;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("api/v1/users")
@RestController
@Slf4j
public class UserController {
    @Autowired
    private UsersServiceImpl usersService;

    @Autowired
    private EmailSenderServiceImpl emailSenderService;

    @PostMapping("/create")
    public ResponseEntity<UsersDto> add(@RequestBody @Valid UsersDto usersDto){
        UsersDto result = usersService.createUsers(usersDto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/verify-email")
    public ResponseEntity<String> sendMail() throws MessagingException {
        log.info("sending sample email");

        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("name", "John Michel!");
        properties.put("location", "Viet Nam");
        properties.put("sign", "Java Developer");

        VerifyMailRequest mail = VerifyMailRequest.builder()
                .from("testfrom@gmail.com")
                .to("letung012000@gmail.com")
                .htmlTemplate(new HtmlTemplate("Registration", properties))
                .subject("This is sample email with spring boot and thymeleaf")
                .build();

        emailSenderService.sendMail(mail);
        return ResponseEntity.ok("success");
    }
}
