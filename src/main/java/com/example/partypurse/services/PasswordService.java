package com.example.partypurse.services;

import com.example.partypurse.util.errors.PasswordComplexityException;
import com.example.partypurse.util.errors.PasswordNotEqualsException;
import com.example.partypurse.util.errors.SamePasswordEqualsWhenUpdateException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordService {
    @Value("${regexp}")
    private String regex;
    private final BCryptPasswordEncoder encoder;

    public void validate(String password, String confirmPassword) throws PasswordComplexityException, PasswordNotEqualsException {
        if (!password.matches(regex))
            throw new PasswordComplexityException();
        if (!password.equals(confirmPassword))
            throw new PasswordNotEqualsException();

    }

    public void validate(String oldPassword, String newPassword, String newPasswordConfirm)
            throws SamePasswordEqualsWhenUpdateException, PasswordComplexityException, PasswordNotEqualsException {
        if (oldPassword.equals(newPassword))
            throw new SamePasswordEqualsWhenUpdateException();
        if (!newPassword.matches(regex))
            throw new PasswordComplexityException();
        if (!newPassword.equals(newPasswordConfirm))
            throw new PasswordNotEqualsException();
    }

    public void verify(String rawPassword, String encodedPassword) throws PasswordNotEqualsException {
        if (!encoder.matches(rawPassword, encodedPassword))
            throw new PasswordNotEqualsException();
    }

    public String encode(String password) {
        return encoder.encode(password);
    }
}
