package com.ppn.ppn.payload;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class VerifyMailRequest {
    private HtmlTemplate htmlTemplate;
    private String email;
    private String firstName;
    private String gender;
    private String phoneNumber;
    private String from;
    private String to;
    private String subject;
}
