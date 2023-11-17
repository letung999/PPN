package com.ppn.ppn.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ppn.ppn.dto.UsersDto;
import com.ppn.ppn.entities.CacheData;
import com.ppn.ppn.payload.*;
import com.ppn.ppn.repository.CacheDataRepository;
import com.ppn.ppn.service.EmailSenderServiceImpl;
import com.ppn.ppn.service.UsersServiceImpl;
import com.ppn.ppn.utils.BuildCacheKey;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.ppn.ppn.constant.HostConstant.HOST_URL_VERIFY_CODE;
import static com.ppn.ppn.constant.MessageStatus.ERR_MSG_VERIFY_SUCCESS;
import static com.ppn.ppn.constant.MessageStatus.INF_MSG_SUCCESSFULLY;
import static com.ppn.ppn.constant.PagingConstant.PAGE_DEFAULT;
import static com.ppn.ppn.constant.PagingConstant.SIZE_DEFAULT;

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

    @Autowired
    private CacheDataRepository cacheDataRepository;

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

    @GetMapping("/all")
    public ResponseEntity<?> all(@RequestParam(defaultValue = PAGE_DEFAULT) @Valid Integer page,
                                 @RequestParam(defaultValue = SIZE_DEFAULT) @Valid Integer size) throws JsonProcessingException {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String cacheKeyUser = BuildCacheKey.cacheKeyAllData("allUsers", page, size);
        Optional<CacheData> cacheData = cacheDataRepository.findById(cacheKeyUser);

        //cache hit
        if (cacheData.isPresent()) {
            String userAsString = cacheData.get().getValue();
            TypeReference<List<UsersDto>> listType = new TypeReference<>() {
            };
            List<UsersDto> resultData = objectMapper.readValue(userAsString, listType);
            APIResponse apiResponse = APIResponse.builder()
                    .message(INF_MSG_SUCCESSFULLY)
                    .timeStamp(LocalDateTime.now())
                    .statusCode(200)
                    .data(resultData)
                    .isSuccess(true)
                    .build();
            return ResponseEntity.ok(apiResponse);
        }

        //cache miss
        List<UsersDto> resultData = usersService.all(pageRequest);
        String usersAsString = objectMapper.writeValueAsString(resultData);
        CacheData cache = new CacheData(cacheKeyUser, usersAsString);

        //save cache
        cacheDataRepository.save(cache);

        APIResponse apiResponse = APIResponse.builder()
                .message(INF_MSG_SUCCESSFULLY)
                .timeStamp(LocalDateTime.now())
                .statusCode(200)
                .data(resultData)
                .isSuccess(true)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody SearchUserRequest request) throws JsonProcessingException {
        PageRequest pageRequest = PageRequest.of(request.getPageIndex() - 1, request.getPageSize());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String cacheKey = BuildCacheKey.buildCacheKeySearchUsers("searchUsers", request);
        Optional<CacheData> cacheData = cacheDataRepository.findById(cacheKey);

        //cache hit
        if (cacheData.isPresent()) {
            String usersAsString = cacheData.get().getValue();
            TypeReference<SearchUserResponse> responseDataType = new TypeReference<SearchUserResponse>() {
            };
            SearchUserResponse searchUserResponse = objectMapper.readValue(usersAsString, responseDataType);
            APIResponse apiResponse = APIResponse.builder()
                    .message(INF_MSG_SUCCESSFULLY)
                    .isSuccess(true)
                    .timeStamp(LocalDateTime.now())
                    .data(searchUserResponse)
                    .build();
            return ResponseEntity.ok(apiResponse);
        }

        //cache miss
        SearchUserResponse searchUserResponse = usersService.search(request, pageRequest);
        String usersAsString = objectMapper.writeValueAsString(searchUserResponse);
        CacheData saveCache = new CacheData(cacheKey, usersAsString);
        cacheDataRepository.save(saveCache);

        APIResponse apiResponse = APIResponse.builder()
                .message(INF_MSG_SUCCESSFULLY)
                .isSuccess(true)
                .timeStamp(LocalDateTime.now())
                .data(searchUserResponse)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody @Valid UsersDto usersDto) {
        UsersDto responseData = usersService.updateUsers(usersDto);
        APIResponse apiResponse = APIResponse.builder()
                .message(INF_MSG_SUCCESSFULLY)
                .statusCode(200)
                .isSuccess(true)
                .timeStamp(LocalDateTime.now())
                .data(responseData)
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
