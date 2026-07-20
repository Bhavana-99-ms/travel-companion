package com.travel.model;

import com.travel.enums.ServiceType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "service_providers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceProvider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String bio;
    private String languages;
    private String location;
    private String city;
    private String country;

    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

    private double pricePerDay;
    private int yearsOfExperience;
    private String certifications;
    private String coverImage;
    private boolean verified = false;
    private boolean available = true;

    @Column(columnDefinition = "DOUBLE DEFAULT 0.0")
    private double averageRating;

    private int totalBookings;

    @Column(updatable = false)
    private LocalDateTime joinedAt;

    @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL)
    private List<Booking> bookings;

    @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL)
    private List<Review> reviews;

    @PrePersist
    public void prePersist() {
        joinedAt = LocalDateTime.now();
    }
}
