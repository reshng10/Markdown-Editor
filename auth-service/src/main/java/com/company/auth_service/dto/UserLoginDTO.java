package com.company.auth_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoginDTO {
    private String email;
    private String password;
}
