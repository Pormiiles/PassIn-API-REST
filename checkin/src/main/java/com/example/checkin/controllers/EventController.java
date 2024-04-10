package com.example.checkin.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.checkin.services.EventService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @GetMapping("/{id}") // Path Variable - recebe o valor que o usuário forneceu
    public ResponseEntity<String> getEvent(@PathVariable String id) {
        this.eventService.getEventDetails(id);

        return ResponseEntity.ok("Sucesso!");
    }
}
