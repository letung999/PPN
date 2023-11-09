package com.ppn.ppn.payload;

import com.ppn.ppn.dto.UsersDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class SearchUserResponse {
    private List<UsersDto> userDtoList;
    private Long numOfItems;
    private Integer numOfPage;
}
