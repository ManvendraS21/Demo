package com.example.demo.helper;


import com.example.demo.service.WriterService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
       return httpSecurity
               .csrf(AbstractHttpConfigurer::disable)
               .authorizeHttpRequests(registry->{
                    registry.requestMatchers("/register/**","/quotes").permitAll();
                    registry.requestMatchers("/admin/**").hasRole("ADMIN");
                    registry.requestMatchers("/user/**").hasRole("USER");
                    registry.anyRequest().authenticated();
        }).formLogin(AbstractAuthenticationFilterConfigurer::permitAll).build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(WriterService writerService, PasswordEncoder passwordEncoder){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(writerService);
        return provider;
    }


    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter(){
        return new HiddenHttpMethodFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
