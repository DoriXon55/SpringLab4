package org.dorixon.springlab4.config;


import lombok.RequiredArgsConstructor;
import org.dorixon.springlab4.service.StudentService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

   @Bean
   public UserDetailsService userDetailsService(StudentService studentService) {
       return username -> studentService
               .findByEmail(username)
               .orElseThrow(() -> new UsernameNotFoundException(String.format(
                       "User '%s' not found!", username
               )));
   }

   @Bean
   public PasswordEncoder passwordEncoder(){
       return new BCryptPasswordEncoder();
   }

   @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
       return config.getAuthenticationManager();
   }


}
