package com.yakimenko.messenger.security.jwt;

import com.yakimenko.messenger.entity.User;
import com.yakimenko.messenger.security.config.SecurityConstants;
import com.yakimenko.messenger.security.details.UserDetailsImpl;
import com.yakimenko.messenger.security.details.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class JWTAuthenticationFilter extends OncePerRequestFilter {
    //логгер
    public static final Logger LOG = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    @Autowired
    private JWTTokenProvider tokenProvider;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {//авторизируем пользователя
            String jwt = getJWTFromRequest(request);
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                String name = tokenProvider.getUserNameFromToken(jwt);
                UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(name);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, Collections.emptyList());
                //задаем детали нашей авторизации
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                //контекст
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (Exception exception) {
            LOG.error("Could not set user authentication");
        }
        //пробрасываем по цепочке
        filterChain.doFilter(request, response);
    }

    //получаем токен из поступившего запроса
    private String getJWTFromRequest(HttpServletRequest request) {
        String bearToken = request.getHeader(SecurityConstants.HEADER_STRING);
        if (StringUtils.hasText(bearToken)
                && bearToken.startsWith(SecurityConstants.TOKEN_PREFIX)){
            String token = bearToken.replaceFirst(SecurityConstants.TOKEN_PREFIX, "");
            return token; //возвращаем чистый токен без префикса
        }
        return null;
    }
}
