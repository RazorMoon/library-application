package com.example.alleywayalliancelms.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@EnableWebSecurity
public class WebSecurity {

    private final UserDetailsService userDetailsService;

    public WebSecurity(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }




    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf()
                .disable()
                .userDetailsService(userDetailsService)
                .authorizeRequests()
                //Доступ только для не зарегистрированных пользователей
                .antMatchers("/registration/**").not().fullyAuthenticated()
                //Доступ только для пользователей с ролью Администратор
                .antMatchers("/admin/**", "/checkout/**").hasAnyRole("ADMIN")
                .antMatchers("/catalog/**").hasRole("USER")
                //Доступ разрешен всем пользователей
                .antMatchers("/","/css/**","/js/**","/accounts/**", "/books/**", "/pic/**", "/copies", "/holds/**" ).permitAll()
                .anyRequest().authenticated()
                .and()
//                    Настройка для входа в систему
                    .formLogin()
                    .usernameParameter("email")
                    .passwordParameter("pwd")
                    .loginPage("/login")
                    .defaultSuccessUrl("/")
                    .permitAll()
                .and()
                    .logout()
                    .permitAll()
                    .logoutSuccessUrl("/")
                .and()
                .build();
    }

}
