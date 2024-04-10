package com.example.checkin.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.checkin.domain.event.Event;
import com.example.checkin.repositories.EventRepository;

import lombok.RequiredArgsConstructor;

@Service // Classe que representa um componente de Service (regra de negócios)
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository; // Instância que acessa o repositório de eventos

    public void getEventDetails(String eventId) { // método de buscar o evento a partir do seu ID
        Event event = this.eventRepository.findById(eventId).orElseThrow(() -> new RuntimeException("Event not found with Id: " + eventId)); // retorna um optional (pode ou n existir). Para tratar, basta usar o método OrElseThrow
        
        return;
    }
}
