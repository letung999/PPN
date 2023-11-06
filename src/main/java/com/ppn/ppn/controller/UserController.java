package com.ppn.ppn.controller;

import com.ppn.ppn.dto.UsersDto;
import com.ppn.ppn.payload.APIResponse;
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

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.ppn.ppn.constant.HostConstant.HOST_URL_VERIFY_CODE;
import static com.ppn.ppn.constant.MessageStatus.ERR_MSG_VERIFY_SUCCESS;
import static com.ppn.ppn.constant.MessageStatus.INF_MSG_SUCCESSFULLY;

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
    public ResponseEntity<?> add(@RequestBody @Valid UsersDto usersDto) {
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
        APIResponse apiResponse = APIResponse.builder()
                .message(INF_MSG_SUCCESSFULLY)
                .timeStamp(LocalDateTime.now())
                .isSuccess(true)
                .statusCode(201)
                .data(result)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam(name = "verifyCode") String verifyCode) throws MessagingException {
        log.info("verify code: {}", verifyCode);
        usersService.verifyUser(verifyCode);
        APIResponse apiResponse = APIResponse.builder()
                .message(ERR_MSG_VERIFY_SUCCESS)
                .timeStamp(LocalDateTime.now())
                .statusCode(200)
                .isSuccess(true)
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
