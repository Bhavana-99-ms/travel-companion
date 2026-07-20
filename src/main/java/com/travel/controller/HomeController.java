package com.travel.controller;

import com.travel.enums.ServiceType;
import com.travel.service.ProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProviderService providerService;

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("topProviders", providerService.topRated());
        model.addAttribute("serviceTypes", ServiceType.values());
        return "home/index";
    }

    @GetMapping("/providers")
    public String providers(@RequestParam(required = false) String city,
                            @RequestParam(required = false) String serviceType,
                            Model model) {
        model.addAttribute("providers", providerService.search(city, serviceType));
        model.addAttribute("serviceTypes", ServiceType.values());
        model.addAttribute("selectedCity", city);
        model.addAttribute("selectedType", serviceType);
        return "home/providers";
    }

    @GetMapping("/providers/{id}")
    public String providerDetail(@PathVariable Long id, Model model) {
        return providerService.findById(id).map(provider -> {
            model.addAttribute("provider", provider);
            return "home/provider-detail";
        }).orElse("redirect:/providers");
    }
}
