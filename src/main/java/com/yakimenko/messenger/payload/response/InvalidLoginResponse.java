package com.yakimenko.messenger.payload.response;

import lombok.Getter;

//когда ошибка 401 создаю этот объект и выдаю клиенту
@Getter
public class InvalidLoginResponse {
    private String name;
    private String password;
    public InvalidLoginResponse(){
        this.name = "Invalid name";
        this.password = "invalid password";
    }
}
