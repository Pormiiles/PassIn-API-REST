package com.example.checkin.services;

import java.text.Normalizer;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.checkin.domain.attendee.Attendee;
import com.example.checkin.domain.event.Event;
import com.example.checkin.domain.event.exceptions.EventNotFoundException;
import com.example.checkin.dto.event.EventIdDTO;
import com.example.checkin.dto.event.EventRequestDTO;
import com.example.checkin.dto.event.EventResponseDTO;
import com.example.checkin.repositories.AttendeeRepository;
import com.example.checkin.repositories.EventRepository;

import lombok.RequiredArgsConstructor;

@Service // Classe que representa um componente de Service (regra de negócios)
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository; // Instância que acessa o repositório de eventos

    private final AttendeeRepository attendeeRepository;

    public EventResponseDTO getEventDetails(String eventId) { // método de buscar o evento a partir do seu ID
        Event event = this.eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("Event not found with Id: " + eventId)); // retorna um optional (pode ou n existir). Para tratar, basta usar o método OrElseThrow
        List<Attendee> attendeeList = this.attendeeRepository.findByEventId(eventId);
        
        return new EventResponseDTO(event, attendeeList.size());
    }

    public EventIdDTO createEvent(EventRequestDTO eventDTO) {
        Event newEvent = new Event();
        newEvent.setTitle(eventDTO.title());
        newEvent.setDetails(eventDTO.details());
        newEvent.setMaximumAttendees(eventDTO.maximumAttendees());
        newEvent.setSlug(createSlug(eventDTO.title()));

        this.eventRepository.save(newEvent);

        return new EventIdDTO(newEvent.getId());
    }

    private String createSlug(String text) { // decomposição canônica da String - removendo espaços, acentuações
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        return normalized.replaceAll("[\\p{InCOMBINING_DIACRITICAL_MARKS}]", "").replaceAll("[^\\w\\s]", "").replaceAll("\\s+", "-").toLowerCase();
    }
}
