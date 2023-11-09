package com.ppn.ppn.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseSearchUserRequest {
    @NotNull
    private Integer pageIndex;

    @NotNull
    private Integer pageSize;

    private String sortBy;

    @JsonProperty
    private boolean isAscending;
}
