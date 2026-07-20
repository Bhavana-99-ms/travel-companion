package com.travel.repository;

import com.travel.enums.ServiceType;
import com.travel.model.ServiceProvider;
import com.travel.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface ServiceProviderRepository extends JpaRepository<ServiceProvider, Long> {
    Optional<ServiceProvider> findByUser(User user);
    List<ServiceProvider> findByAvailableTrue();
    List<ServiceProvider> findByCityContainingIgnoreCaseAndAvailableTrue(String city);
    List<ServiceProvider> findByServiceTypeAndAvailableTrue(ServiceType serviceType);

    @Query("SELECT sp FROM ServiceProvider sp WHERE " +
           "(:city IS NULL OR LOWER(sp.city) LIKE LOWER(CONCAT('%', :city, '%'))) AND " +
           "(:serviceType IS NULL OR sp.serviceType = :serviceType) AND " +
           "sp.available = true ORDER BY sp.averageRating DESC")
    List<ServiceProvider> searchProviders(@Param("city") String city,
                                          @Param("serviceType") ServiceType serviceType);

    @Query("SELECT sp FROM ServiceProvider sp WHERE sp.available = true ORDER BY sp.averageRating DESC")
    List<ServiceProvider> findTopRated();
}
