package com.sparta.lv2.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
public class UserRequestDto {
    @Pattern(regexp = "^[a-z0-9]{4,10}$", message = "") // a~z, 0~9 -> 4자 ~ 10자 이내
    private String username;

    @Pattern(regexp = "^[a-zA-Z0-9]{8,15}$") // a~z, A~Z, 0~9 -> 8자 ~ 15이내
    private String password;
}
