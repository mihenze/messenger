package com.yakimenko.messenger.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.Map;

//Класс для работы с ошибками, которые пришли на сервер
@Service
public class ResponseErrorValidation {
    //возвращает map с ошибками из payload/request
    public ResponseEntity<Object> mapValidationService(BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            //переписываем ошибки в Map
            if(!CollectionUtils.isEmpty(result.getAllErrors())){
                for (ObjectError error : result.getAllErrors()){
                    errorMap.put(error.getCode(), error.getDefaultMessage());
                }
            }

            for (FieldError error : result.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            // возвращаем если есть ошибки
            return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
        }
        return null;
    }
}
