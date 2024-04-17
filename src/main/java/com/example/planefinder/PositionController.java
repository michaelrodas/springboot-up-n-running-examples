package com.example.planefinder;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class PositionController {
    @NonNull
    private final AircraftRepository repository;
//    private final RestClient client = RestClient.create("http://localhost:8443/aircraft");
    private final WebClient client = WebClient.create("http://localhost:8443/aircraft");
    @GetMapping("/aircraft")
    public String getCurrentAircraftPositions(Model model) {
        repository.deleteAll();

//        List<Aircraft> response = client.get().retrieve().body(new ParameterizedTypeReference<>() { });
//
//        assert response != null;
//        response.stream()
//                .filter(plane -> !plane.getReg().isEmpty())
//                .forEach(repository::save);

        client.get().retrieve().bodyToFlux(Aircraft.class).filter(plane -> !plane.getReg().isEmpty()).toStream().forEach(repository::save);

        model.addAttribute("currentPositions", repository.findAll());
        return "positions";
    }
}