package com.ppn.ppn.payload;

import com.ppn.ppn.dto.UsersDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchUserResponse {
    private List<UsersDto> userDtoList;
    private Long numOfItems;
    private Integer numOfPage;
}
