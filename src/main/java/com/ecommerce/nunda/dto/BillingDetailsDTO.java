package com.ecommerce.nunda.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
public class BillingDetailsDTO {

    @NotBlank(message = "Street address is required")
    private String address;

    @NotBlank(message = "City is required")
    @Pattern(regexp = "Kampala", message = "City must be Kampala")
    private String city = "Kampala";

    @NotBlank(message = "Country is required")
    private String country;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "\\+2567[0-9]{8}|07[0-9]{8}", message = "Invalid phone number format")
    private String tel;
}
