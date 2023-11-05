package com.ppn.ppn.controller;

import com.ppn.ppn.dto.UsersDto;
import com.ppn.ppn.payload.HtmlTemplate;
import com.ppn.ppn.payload.VerifyMailRequest;
import com.ppn.ppn.service.EmailSenderServiceImpl;
import com.ppn.ppn.service.UsersServiceImpl;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static com.ppn.ppn.constant.HostConstant.HOST_URL_VERIFY_CODE;


@RequestMapping("api/v1/users")
@RestController
@Slf4j
public class UserController {
    @Autowired
    private UsersServiceImpl usersService;

    @Autowired
    private EmailSenderServiceImpl emailSenderService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Value("${cox.automation.email}")
    private String coxAutomationEmail;

    @Value("${cox.name}")
    private String nameCompany;

    @PostMapping("/create")
    public ResponseEntity<UsersDto> add(@RequestBody @Valid UsersDto usersDto) {
        UsersDto result = usersService.createUsers(usersDto);
        //send mail for user:
        Map<String, Object> properties = new HashMap<>();
        properties.put("name", usersDto.getEmail());
        properties.put("location", "Viet Nam");
        properties.put("sign", nameCompany);
        properties.put("linkConfirm", HOST_URL_VERIFY_CODE + result.getVerifyCode());

        VerifyMailRequest mail = VerifyMailRequest.builder()
                .from(coxAutomationEmail)
                .to("letung012000@gmail.com")// email from request body
                .htmlTemplate(new HtmlTemplate("VerifyEmail", properties))
                .subject("This is email confirm from COX-AUTOMATION")
                .build();
        try {
            log.info("send email to user {}", usersDto.getEmail());
            emailSenderService.sendMail(mail);
        } catch (MessagingException e) {
            log.error("send email fail! with email {}", usersDto.getEmail());
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam(name = "verifyCode") String verifyCode) throws MessagingException {
        log.info("verify code: {}", verifyCode);
        try{
            usersService.verifyUser(verifyCode);
        }catch (Exception ex){
            log.info("verify fail with code: {}", verifyCode);
            ex.printStackTrace();
        }
        return ResponseEntity.ok("success");
    }
}
