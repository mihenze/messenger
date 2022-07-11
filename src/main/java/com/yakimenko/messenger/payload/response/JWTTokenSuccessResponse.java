package com.yakimenko.messenger.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

//наш токен для клиента
/**Ответ на успешный вход
 * в теле запроса приходит  структура:
 * {
 *     token: "тут сгенерированный токен"
 * }
 * структура сгенерированного токена:
 * prefix_token => Bearer_токен
 * */
@Data
@AllArgsConstructor
public class JWTTokenSuccessResponse {
    private String token;
}
