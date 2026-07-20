package com.travel.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Data
public class BookingDto {
    @NotNull(message = "Booking date is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate bookingDate;

    @Min(value = 1, message = "At least 1 day required")
    @Max(value = 30, message = "Maximum 30 days")
    private int numberOfDays;

    private String specialRequests;
    private Long providerId;
}
