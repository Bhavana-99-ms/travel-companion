package com.travel.service;

import com.travel.dto.ReviewDto;
import com.travel.model.Booking;
import com.travel.model.Review;
import com.travel.model.ServiceProvider;
import com.travel.model.User;
import com.travel.repository.BookingRepository;
import com.travel.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;
    private final ProviderService providerService;

    public List<Review> getProviderReviews(ServiceProvider provider) {
        return reviewRepository.findByProviderOrderByCreatedAtDesc(provider);
    }

    public boolean hasReviewed(User user, ServiceProvider provider) {
        return reviewRepository.existsByUserAndProvider(user, provider);
    }

    public Review addReview(User user, ReviewDto dto) {
        ServiceProvider provider = providerService.findById(dto.getProviderId())
                .orElseThrow(() -> new RuntimeException("Provider not found"));

        Booking booking = bookingRepository.findById(dto.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        Review review = Review.builder()
                .user(user)
                .provider(provider)
                .booking(booking)
                .rating(dto.getRating())
                .comment(dto.getComment())
                .build();

        reviewRepository.save(review);

        // Update provider average rating
        Double avg = reviewRepository.findAverageRatingByProvider(provider);
        providerService.updateRating(provider, avg != null ? avg : 0.0);

        return review;
    }
}
