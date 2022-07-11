package com.yakimenko.messenger.payload.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

//Используем данный объект при регистрации
@Data
public class SignupRequest {
    @NotEmpty(message = "Please enter your name")
    String name;
    @NotEmpty(message = "Password is required")
    @Size(min = 6)
    private String password;
}
