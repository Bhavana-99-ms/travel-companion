package com.travel.repository;

import com.travel.enums.BookingStatus;
import com.travel.model.Booking;
import com.travel.model.ServiceProvider;
import com.travel.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserOrderByCreatedAtDesc(User user);
    List<Booking> findByProviderOrderByCreatedAtDesc(ServiceProvider provider);
    List<Booking> findByUserAndStatus(User user, BookingStatus status);
    boolean existsByUserAndProviderAndStatus(User user, ServiceProvider provider, BookingStatus status);
}
