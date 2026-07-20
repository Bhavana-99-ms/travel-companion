package com.travel.service;

import com.travel.dto.BookingDto;
import com.travel.enums.BookingStatus;
import com.travel.model.Booking;
import com.travel.model.ServiceProvider;
import com.travel.model.User;
import com.travel.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ProviderService providerService;

    public List<Booking> getUserBookings(User user) {
        return bookingRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public List<Booking> getProviderBookings(ServiceProvider provider) {
        return bookingRepository.findByProviderOrderByCreatedAtDesc(provider);
    }

    public Optional<Booking> findById(Long id) {
        return bookingRepository.findById(id);
    }

    public Booking createBooking(User user, BookingDto dto) {
        ServiceProvider provider = providerService.findById(dto.getProviderId())
                .orElseThrow(() -> new RuntimeException("Provider not found"));

        double total = provider.getPricePerDay() * dto.getNumberOfDays();

        Booking booking = Booking.builder()
                .user(user)
                .provider(provider)
                .bookingDate(dto.getBookingDate())
                .numberOfDays(dto.getNumberOfDays())
                .totalAmount(total)
                .specialRequests(dto.getSpecialRequests())
                .status(BookingStatus.PENDING)
                .build();

        Booking saved = bookingRepository.save(booking);

        provider.setTotalBookings(provider.getTotalBookings() + 1);
        providerService.save(provider);

        return saved;
    }

    public Booking updateStatus(Long bookingId, BookingStatus status) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus(status);
        return bookingRepository.save(booking);
    }

    public boolean hasCompletedBooking(User user, ServiceProvider provider) {
        return bookingRepository.existsByUserAndProviderAndStatus(user, provider, BookingStatus.COMPLETED);
    }
}
