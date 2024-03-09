package com.example.partypurse.dto.request;

public record PasswordUpdateForm(String oldPassword, String newPassword, String newPasswordConfirm) {
}
