package com.localkart.services.store.api.users.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateUserRequest(
        @NotBlank
        @Pattern(regexp = "\\+91[6-9]\\d{9}$", message = "invalid mobile number")
        String phoneNumber,
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @NotBlank
        @Email(message = "invalid emailId")
        String emailId
) {
}
