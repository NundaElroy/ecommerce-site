package com.ecommerce.nunda.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillingDetailsDTO {

    @NotBlank(message = "fullname is required")
    private String fullName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Street address is required")
    private String address;

//    @NotBlank(message = "City is required")
//    @Pattern(regexp = "Kampala", message = "City must be Kampala")
//    private String city = "Kampala";

//    @NotBlank(message = "Country is required")
//    private String country;


    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+2567[0-9]{8}$", message = "Invalid phone number format. Use +256XXXXXXXXX")
    private String phoneNumber;

    @NotNull(message = "Amount is required")
    private Double amount;


}
