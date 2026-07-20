package com.travel.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ReviewDto {
    @Min(1) @Max(5)
    private int rating;

    @NotBlank(message = "Comment is required")
    @Size(min = 10, message = "Comment must be at least 10 characters")
    private String comment;

    private Long providerId;
    private Long bookingId;
}
