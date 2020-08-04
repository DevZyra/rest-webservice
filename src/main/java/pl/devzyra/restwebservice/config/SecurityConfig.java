package pl.devzyra.restwebservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.devzyra.restwebservice.config.filters.AuthenticationFilter;
import pl.devzyra.restwebservice.config.filters.VerificationFilter;
import pl.devzyra.restwebservice.services.UserService;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static pl.devzyra.restwebservice.config.SecurityConstants.SING_UP_URL;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

private final UserService userService;
private final PasswordEncoder passwordEncoder;

    public SecurityConfig(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, SING_UP_URL)
                .permitAll()
                .antMatchers("/activation")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new AuthenticationFilter(authenticationManager()))
                .addFilterAfter(new VerificationFilter(authenticationManager()),AuthenticationFilter.class)
                .sessionManagement()
                .sessionCreationPolicy(STATELESS);
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }


}
