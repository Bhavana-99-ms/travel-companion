package com.travel.controller;

import com.travel.dto.ProviderProfileDto;
import com.travel.enums.BookingStatus;
import com.travel.enums.ServiceType;
import com.travel.model.ServiceProvider;
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
@RequestMapping("/provider/dashboard")
@RequiredArgsConstructor
public class ProviderDashboardController {

    private final UserService userService;
    private final ProviderService providerService;
    private final BookingService bookingService;
    private final ReviewService reviewService;

    private User getUser(UserDetails ud) {
        return userService.findByEmail(ud.getUsername()).orElseThrow();
    }

    @GetMapping
    public String dashboard(@AuthenticationPrincipal UserDetails ud, Model model) {
        User user = getUser(ud);
        ServiceProvider provider = providerService.findByUser(user).orElse(null);
        model.addAttribute("user", user);
        model.addAttribute("provider", provider);
        if (provider != null) {
            model.addAttribute("bookings", bookingService.getProviderBookings(provider));
            model.addAttribute("reviews", reviewService.getProviderReviews(provider));
        }
        return "provider/dashboard";
    }

    @GetMapping("/profile")
    public String profileForm(@AuthenticationPrincipal UserDetails ud, Model model) {
        User user = getUser(ud);
        ServiceProvider existing = providerService.findByUser(user).orElse(null);
        ProviderProfileDto dto = new ProviderProfileDto();
        if (existing != null) {
            dto.setBio(existing.getBio());
            dto.setLanguages(existing.getLanguages());
            dto.setLocation(existing.getLocation());
            dto.setCity(existing.getCity());
            dto.setCountry(existing.getCountry());
            dto.setServiceType(existing.getServiceType());
            dto.setPricePerDay(existing.getPricePerDay());
            dto.setYearsOfExperience(existing.getYearsOfExperience());
            dto.setCertifications(existing.getCertifications());
        }
        model.addAttribute("profileDto", dto);
        model.addAttribute("serviceTypes", ServiceType.values());
        return "provider/profile";
    }

    @PostMapping("/profile")
    public String saveProfile(@Valid @ModelAttribute ProviderProfileDto dto,
                              BindingResult result,
                              @AuthenticationPrincipal UserDetails ud, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("serviceTypes", ServiceType.values());
            return "provider/profile";
        }
        User user = getUser(ud);
        providerService.saveProfile(user, dto);
        return "redirect:/provider/dashboard?saved=true";
    }

    @PostMapping("/booking/{id}/confirm")
    public String confirmBooking(@PathVariable Long id) {
        bookingService.updateStatus(id, BookingStatus.CONFIRMED);
        return "redirect:/provider/dashboard";
    }

    @PostMapping("/booking/{id}/complete")
    public String completeBooking(@PathVariable Long id) {
        bookingService.updateStatus(id, BookingStatus.COMPLETED);
        return "redirect:/provider/dashboard";
    }

    @PostMapping("/booking/{id}/cancel")
    public String cancelBooking(@PathVariable Long id) {
        bookingService.updateStatus(id, BookingStatus.CANCELLED);
        return "redirect:/provider/dashboard";
    }

    @PostMapping("/toggle-availability")
    public String toggleAvailability(@AuthenticationPrincipal UserDetails ud) {
        User user = getUser(ud);
        providerService.findByUser(user).ifPresent(p -> {
            p.setAvailable(!p.isAvailable());
            providerService.save(p);
        });
        return "redirect:/provider/dashboard";
    }
}
