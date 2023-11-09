package com.ppn.ppn.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchUserRequest extends BaseSearchUserRequest {
    private String email;
    private String firstName;
    private String phoneNumber;
    private String status;
    private String gender;
}
