package com.travel.service;

import com.travel.dto.ProviderProfileDto;
import com.travel.enums.ServiceType;
import com.travel.model.ServiceProvider;
import com.travel.model.User;
import com.travel.repository.ServiceProviderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProviderService {

    private final ServiceProviderRepository providerRepository;

    public Optional<ServiceProvider> findByUser(User user) {
        return providerRepository.findByUser(user);
    }

    public Optional<ServiceProvider> findById(Long id) {
        return providerRepository.findById(id);
    }

    public List<ServiceProvider> findAll() {
        return providerRepository.findByAvailableTrue();
    }

    public List<ServiceProvider> search(String city, String serviceType) {
        ServiceType type = null;
        if (serviceType != null && !serviceType.isBlank()) {
            try { type = ServiceType.valueOf(serviceType); } catch (Exception ignored) {}
        }
        return providerRepository.searchProviders(
                (city == null || city.isBlank()) ? null : city, type);
    }

    public List<ServiceProvider> topRated() {
        return providerRepository.findTopRated();
    }

    public ServiceProvider saveProfile(User user, ProviderProfileDto dto) {
        ServiceProvider provider = providerRepository.findByUser(user)
                .orElse(ServiceProvider.builder().user(user).build());

        provider.setBio(dto.getBio());
        provider.setLanguages(dto.getLanguages());
        provider.setLocation(dto.getLocation());
        provider.setCity(dto.getCity());
        provider.setCountry(dto.getCountry());
        provider.setServiceType(dto.getServiceType());
        provider.setPricePerDay(dto.getPricePerDay());
        provider.setYearsOfExperience(dto.getYearsOfExperience());
        provider.setCertifications(dto.getCertifications());

        return providerRepository.save(provider);
    }

    public void updateRating(ServiceProvider provider, double newRating) {
        provider.setAverageRating(newRating);
        providerRepository.save(provider);
    }

    public ServiceProvider save(ServiceProvider provider) {
        return providerRepository.save(provider);
    }
}
