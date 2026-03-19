package org.dorixon.springlab4.auth;

import java.util.Set;

import jakarta.validation.constraints.NotNull;
import org.dorixon.springlab4.config.JwtService;
import org.dorixon.springlab4.model.Student;
import org.dorixon.springlab4.repository.RoleRepository;
import org.dorixon.springlab4.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final StudentService studentService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public void register(Student student)
    {
        student.setPassword(passwordEncoder.encode(student.getPassword()));

        var role = roleRepository
                .findByName("USER")
                .orElseThrow(() -> new IllegalStateException("Role USER was not initiated"));
        student.setRoles(Set.of(role));
        studentService.setStudent(student);
    }

    @Override
    public Tokens authenticate(Credentials credentials)
    {
        return authenticate(credentials.email(), credentials.password());
    }

    private Tokens authenticate(@NotNull String email, @NotNull String password)
    {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        var user = studentService
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User '%s' not found!", email)));

        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        return Tokens.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public Tokens refreshTokens(Tokens tokens)
    {
        final String refreshToken = tokens.refreshToken();

        if (refreshToken == null || refreshToken.isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Brak tokenu odświeżania");

        final String prefix = "Bearer ";
        final String token = refreshToken.startsWith(prefix) ? refreshToken.substring(prefix.length()) : refreshToken;
        final String email = jwtService.extractUserNameFromRefreshToken(token);

        if (email == null || email.isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Niepoprawny format tokenu odświeżania");

        var user = studentService
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User '%s' not found!", email)));

        if (!jwtService.isRefreshTokenValid(token, user)) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token odświeżania stracił ważność");

        var accessToken = jwtService.generateAccessToken(user);
        var newRefreshToken = jwtService.generateRefreshToken(user);

        return Tokens.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

}
