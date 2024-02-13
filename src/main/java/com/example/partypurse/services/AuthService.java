package com.example.partypurse.services;

import com.example.partypurse.dto.*;
import com.example.partypurse.dto.request.SignInRequest;
import com.example.partypurse.dto.request.SignUpRequest;
import com.example.partypurse.dto.response.ApplicationError;
import com.example.partypurse.dto.response.JwtResponse;
import com.example.partypurse.models.Role;
import com.example.partypurse.models.Room;
import com.example.partypurse.models.User;
import com.example.partypurse.repositories.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.example.partypurse.dto.JwtType.ACCESS;
import static com.example.partypurse.dto.JwtType.REFRESH;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final BCryptPasswordEncoder encoder;
    private final JwtService jwtService;
    private final RoleRepository roleRepository;

    @Transactional
    public ResponseEntity<?> registerNewUser(SignUpRequest signUpRequest) {

        //TODO: если пользователь существует кидать ошибку.

        try {
            userService.findByUsername(signUpRequest.username());
        } catch (UsernameNotFoundException e){
            //return new ResponseEntity<>(new ApplicationError(HttpStatus.BAD_REQUEST.value(), "Пользователь с таким именем уже существет!"), HttpStatus.BAD_REQUEST);

        }

        if (!signUpRequest.password().equals(signUpRequest.confirmPassword()))
            return new ResponseEntity<>(new ApplicationError(HttpStatus.BAD_REQUEST.value(), "Пароли не совпадают!"), HttpStatus.BAD_REQUEST);



        User user = new User();
        user.setFirstName(signUpRequest.firstName());
        user.setLastName(signUpRequest.lastName());
        user.setUsername(signUpRequest.username());
        user.setVisitedRooms(new ArrayList<>());
        user.setCreatedRooms(new ArrayList<>());
        user.setPassword(encoder.encode(signUpRequest.password()));
        user.setRoles(Collections.singletonList(roleRepository.findByName("ROLE_USER")));

        return ResponseEntity.ok(userService.save(user));
    }

    public ResponseEntity<?> login(final SignInRequest signInRequest) {
        User user = userService.findByUsername(signInRequest.username());
        if (!encoder.matches(signInRequest.password(), user.getPassword()))
            return new ResponseEntity<>(new ApplicationError(HttpStatus.BAD_REQUEST.value(), "Неправильный пароль!"), HttpStatus.BAD_REQUEST);

        UserClaims userClaims = new UserClaims(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName());

        String accessToken = jwtService.generateToken(userClaims, ACCESS);
        String refreshToken = jwtService.generateToken(userClaims, REFRESH);

        return ResponseEntity.ok(new JwtResponse(accessToken, refreshToken));
    }
}
