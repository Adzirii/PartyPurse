package com.example.partypurse.services;

import com.example.partypurse.dto.*;
import com.example.partypurse.dto.request.SignInRequest;
import com.example.partypurse.dto.request.SignUpRequest;
import com.example.partypurse.dto.response.UserInfoDto;
import com.example.partypurse.dto.response.JwtResponse;
import com.example.partypurse.models.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.partypurse.dto.JwtType.ACCESS;
import static com.example.partypurse.dto.JwtType.REFRESH;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final PasswordUtil passwordUtil;
    private final JwtService jwtService;


    @Transactional
    public UserInfoDto registerNewUser(final SignUpRequest signUpRequest) {

        //TODO: если пользователь существует кидать ошибку.
//
//        try {
//            userService.findByUsername(signUpRequest.username());
//        } catch (UsernameNotFoundException e){
//            //return new ResponseEntity<>(new ApplicationError(HttpStatus.BAD_REQUEST.value(), "Пользователь с таким именем уже существет!"), HttpStatus.BAD_REQUEST);
//

        var user = userService.save(signUpRequest);

        return userService.getInfo(user);
    }

    public JwtResponse login(final SignInRequest signInRequest){
        User user = userService.findByUsername(signInRequest.username());

        passwordUtil.verify(signInRequest.password(), user.getPassword());

        UserClaims userClaims = new UserClaims(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName());

        String accessToken = jwtService.generateToken(userClaims, ACCESS);
        String refreshToken = jwtService.generateToken(userClaims, REFRESH);

        return new JwtResponse(accessToken, refreshToken);
    }
}
