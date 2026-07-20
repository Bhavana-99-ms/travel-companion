package com.travel.repository;

import com.travel.model.Review;
import com.travel.model.ServiceProvider;
import com.travel.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProviderOrderByCreatedAtDesc(ServiceProvider provider);
    boolean existsByUserAndProvider(User user, ServiceProvider provider);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.provider = :provider")
    Double findAverageRatingByProvider(@Param("provider") ServiceProvider provider);
}
