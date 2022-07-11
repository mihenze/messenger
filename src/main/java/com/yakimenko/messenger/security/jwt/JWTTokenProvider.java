package com.yakimenko.messenger.security.jwt;

import com.yakimenko.messenger.entity.User;
import com.yakimenko.messenger.security.config.SecurityConstants;
import com.yakimenko.messenger.security.details.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

//Для создания нашего токена и валидации
@Component
public class JWTTokenProvider {
    public static final Logger log = LoggerFactory.getLogger(JWTTokenProvider.class);

    public String generateToken(Authentication authentication) {
        User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();

        //создаем объект claims и кладем в него данные
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("name", user.getName());

        //кодирование токена
        return Jwts.builder().setSubject(user.getName()).addClaims(claimsMap)
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET).compact();
    }

    //валидация токена
    public boolean validateToken(String token){
        try { //принимаем токен, если все хорошо то декодируем
            Jwts.parser().setSigningKey(SecurityConstants.SECRET) //парсим (декодируем) в соответсвии с секретным словом
                    .parseClaimsJws(token); //и берем клэймсы
            return true;
        } catch (SignatureException | MalformedJwtException |
                ExpiredJwtException | UnsupportedJwtException |
                IllegalArgumentException exception) {
            //иначе заносим в лог
            log.error(exception.getMessage());
            return false;
        }
    }

    //берем имя пользователя из токена
    public String getUserNameFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(token).getBody();
        String name = (String) claims.get("name");
        return  name;
    }

}
