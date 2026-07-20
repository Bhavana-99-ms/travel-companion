package com.travel.dto;

import com.travel.enums.ServiceType;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProviderProfileDto {
    @NotBlank(message = "Bio is required")
    private String bio;

    @NotBlank(message = "Languages are required")
    private String languages;

    @NotBlank(message = "Location is required")
    private String location;

    @NotBlank(message = "City is required")
    private String city;

    private String country;

    @NotNull(message = "Service type is required")
    private ServiceType serviceType;

    @Min(value = 0, message = "Price must be positive")
    private double pricePerDay;

    @Min(value = 0) @Max(value = 50)
    private int yearsOfExperience;

    private String certifications;
}
