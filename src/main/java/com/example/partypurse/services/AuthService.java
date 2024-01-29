package com.example.partypurse.services;

import com.example.partypurse.dto.*;
import com.example.partypurse.dto.request.SignInRequest;
import com.example.partypurse.dto.request.SignUpRequest;
import com.example.partypurse.dto.response.ApplicationError;
import com.example.partypurse.dto.response.JwtResponse;
import com.example.partypurse.models.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.partypurse.dto.JwtType.ACCESS;
import static com.example.partypurse.dto.JwtType.REFRESH;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final BCryptPasswordEncoder encoder;
    private final JwtService jwtService;

    @Transactional
    public ResponseEntity<?> register(SignUpRequest signUpRequest) {

        if (userService.findByUsername(signUpRequest.username()).isPresent())
            return new ResponseEntity<>(new ApplicationError(HttpStatus.BAD_REQUEST.value(), "Пользователь с таким именем уже существет!"), HttpStatus.BAD_REQUEST);

        if (!signUpRequest.password().equals(signUpRequest.confirmPassword()))
            return new ResponseEntity<>(new ApplicationError(HttpStatus.BAD_REQUEST.value(), "Пароли не совпадают!"), HttpStatus.BAD_REQUEST);

        User user = new User();
        user.setFirstName(signUpRequest.firstName());
        user.setLastName(signUpRequest.lastName());
        user.setUsername(signUpRequest.username());
        user.setPassword(encoder.encode(signUpRequest.password()));

        return ResponseEntity.ok(userService.save(user));
    }

    public ResponseEntity<?> login(final SignInRequest signInRequest) {
        User user = userService.findByUsername(signInRequest.username()).orElseThrow(()-> new IllegalArgumentException(
                String.format("Пользователя с именем '%s' не существует", signInRequest.username()))
        );
        if (!encoder.matches(signInRequest.password(), user.getPassword()))
            return new ResponseEntity<>(new ApplicationError(HttpStatus.BAD_REQUEST.value(), "Неправильный пароль!"), HttpStatus.BAD_REQUEST);

        UserClaims userClaims = new UserClaims(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName());

        String accessToken = jwtService.generateToken(userClaims, ACCESS);
        String refreshToken = jwtService.generateToken(userClaims, REFRESH);

        return ResponseEntity.ok(new JwtResponse(accessToken, refreshToken));
    }
}
