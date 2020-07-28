package pl.devzyra.restwebservice.config.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.devzyra.restwebservice.config.SpringApplicationContext;
import pl.devzyra.restwebservice.dto.UserDto;
import pl.devzyra.restwebservice.model.request.UserLoginRequestModel;
import pl.devzyra.restwebservice.services.UserService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import static pl.devzyra.restwebservice.config.SecurityConstants.*;


public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    private final AuthenticationManager authenticationManager;

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
       try{
           UserLoginRequestModel logNpass = new ObjectMapper().readValue(request.getInputStream(),UserLoginRequestModel.class);

           return authenticationManager.authenticate(
                     new UsernamePasswordAuthenticationToken(
                             logNpass.getEmail(),
                             logNpass.getPassword(),
                             new ArrayList<>()
                     ));

       } catch (IOException e){
           throw new RuntimeException(e);
       }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {


        String token = Jwts.builder()
                           .setSubject(authResult.getName())
                           .setIssuedAt(new Date())
                           .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusWeeks(2)))
                           .signWith(Keys.hmacShaKeyFor(TOKEN_SECRET.getBytes()))
                           .compact();


        UserService userService = (UserService) SpringApplicationContext.getBean("userServiceImpl");

        UserDto userDto = userService.getUser(authResult.getName());

        response.addHeader(HEADER_STRING,TOKEN_PREFIX + token);
        response.addHeader("UserID",userDto.getUserId());

    }
}
