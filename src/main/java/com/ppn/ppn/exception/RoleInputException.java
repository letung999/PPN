package com.ppn.ppn.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

import static com.ppn.ppn.constant.MessageStatus.ERR_MSG_ROLE_IS_NOT_CONTAIN;
import static com.ppn.ppn.constant.RoleConstant.*;

@Getter
@Setter
public class RoleInputException extends RuntimeException {
    private static List<String> nameRoles = Arrays.asList(SYSTEM_ADMIN, ADMIN, OWNER, VIEWER, CONTRIBUTE, EDITOR);
    private String value;

    public RoleInputException(String value) {
        super(String.format("%s " + ERR_MSG_ROLE_IS_NOT_CONTAIN + " %s ", value, nameRoles));
        this.value = value;
    }
}
