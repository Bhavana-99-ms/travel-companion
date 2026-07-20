package com.travel.controller;

import com.travel.dto.BookingDto;
import com.travel.dto.ReviewDto;
import com.travel.enums.BookingStatus;
import com.travel.model.User;
import com.travel.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class UserDashboardController {

    private final UserService userService;
    private final BookingService bookingService;
    private final ReviewService reviewService;
    private final ProviderService providerService;

    private User getUser(UserDetails ud) {
        return userService.findByEmail(ud.getUsername()).orElseThrow();
    }

    @GetMapping
    public String dashboard(@AuthenticationPrincipal UserDetails ud, Model model) {
        User user = getUser(ud);
        model.addAttribute("user", user);
        model.addAttribute("bookings", bookingService.getUserBookings(user));
        return "booking/dashboard";
    }

    @GetMapping("/book/{providerId}")
    public String bookingForm(@PathVariable Long providerId,
                              @AuthenticationPrincipal UserDetails ud, Model model) {
        return providerService.findById(providerId).map(provider -> {
            model.addAttribute("provider", provider);
            model.addAttribute("bookingDto", new BookingDto());
            return "booking/book";
        }).orElse("redirect:/providers");
    }

    @PostMapping("/book")
    public String createBooking(@Valid @ModelAttribute BookingDto dto,
                                BindingResult result,
                                @AuthenticationPrincipal UserDetails ud, Model model) {
        if (result.hasErrors()) {
            providerService.findById(dto.getProviderId()).ifPresent(p -> model.addAttribute("provider", p));
            return "booking/book";
        }
        User user = getUser(ud);
        bookingService.createBooking(user, dto);
        return "redirect:/dashboard?booked=true";
    }

    @PostMapping("/booking/{id}/cancel")
    public String cancelBooking(@PathVariable Long id,
                                @AuthenticationPrincipal UserDetails ud) {
        bookingService.findById(id).ifPresent(b -> {
            User user = getUser(ud);
            if (b.getUser().getId().equals(user.getId())) {
                bookingService.updateStatus(id, BookingStatus.CANCELLED);
            }
        });
        return "redirect:/dashboard";
    }

    @GetMapping("/review/{bookingId}")
    public String reviewForm(@PathVariable Long bookingId,
                             @AuthenticationPrincipal UserDetails ud, Model model) {
        return bookingService.findById(bookingId).map(booking -> {
            model.addAttribute("booking", booking);
            model.addAttribute("reviewDto", new ReviewDto());
            return "review/add-review";
        }).orElse("redirect:/dashboard");
    }

    @PostMapping("/review")
    public String addReview(@Valid @ModelAttribute ReviewDto dto,
                            BindingResult result,
                            @AuthenticationPrincipal UserDetails ud) {
        if (!result.hasErrors()) {
            User user = getUser(ud);
            reviewService.addReview(user, dto);
        }
        return "redirect:/dashboard?reviewed=true";
    }
}
