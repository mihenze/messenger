package com.yakimenko.messenger.payload.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

//Этот объект передается на сервер, когда происходит аутентификация на веб сайте
@Data
public class SigninRequest {
    //то что передаем, когда видим логин страницы
    @NotEmpty(message = "Login cannot be empty")
    private String name;
    @NotEmpty(message = "Password cannot be empty")
    private String password;
}
