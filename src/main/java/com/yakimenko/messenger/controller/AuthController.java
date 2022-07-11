package com.yakimenko.messenger.controller;

import com.yakimenko.messenger.payload.request.SigninRequest;
import com.yakimenko.messenger.payload.request.SignupRequest;
import com.yakimenko.messenger.payload.response.JWTTokenSuccessResponse;
import com.yakimenko.messenger.payload.response.MessageResponse;
import com.yakimenko.messenger.security.config.SecurityConstants;
import com.yakimenko.messenger.security.jwt.JWTTokenProvider;
import com.yakimenko.messenger.service.ResponseErrorValidation;
import com.yakimenko.messenger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/auth")
@PreAuthorize("permitAll()")
public class AuthController {
    @Autowired
    private JWTTokenProvider jwtTokenProvider;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ResponseErrorValidation responseErrorValidation;
    @Autowired
    private UserService userService;

    //авторизация
    /**Первый эндпоинт из задания
     * структура без изменений, в теле запроса приходит
     * {
     *     name: "имя отправителя"
     *     password: "пароль"
     * }
     * */
    @PostMapping("/signin")
    public ResponseEntity<Object> authenticationUser(@Valid @RequestBody SigninRequest signinRequest, BindingResult bindingResult) {
        //проверяем на ошибки
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if(!ObjectUtils.isEmpty(errors)) return errors; //если есть вернем ошибки

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                signinRequest.getName(), signinRequest.getPassword()
        ));

        //задаем security контекст
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = SecurityConstants.TOKEN_PREFIX + jwtTokenProvider.generateToken(authentication);
        //передаем клиенту
        return ResponseEntity.ok(new JWTTokenSuccessResponse(jwt));
    }

    //регистрация
    /**Свой эндпоинт для регистрации (в задании остутствовал)
     * структура аналогичная как при входе, в теле запроса приходит
     * {
     *     name: "имя отправителя"
     *     password: "пароль"
     * }
     * */
    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody SignupRequest signupRequest, BindingResult bindingResult) {
        //проверяем на ошибки
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if(!ObjectUtils.isEmpty(errors)) return errors; //если есть вернем ошибки

        //регистрируем пользователя
        userService.createUser(signupRequest);
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }
}
