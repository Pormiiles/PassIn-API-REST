package com.example.checkin.services;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.checkin.domain.attendee.Attendee;
import com.example.checkin.domain.event.Event;
import com.example.checkin.domain.event.exceptions.EventFullException;
import com.example.checkin.domain.event.exceptions.EventNotFoundException;
import com.example.checkin.dto.attendee.AttendeRequestDTO;
import com.example.checkin.dto.attendee.AttendeeIdDTO;
import com.example.checkin.dto.event.EventIdDTO;
import com.example.checkin.dto.event.EventRequestDTO;
import com.example.checkin.dto.event.EventResponseDTO;
import com.example.checkin.repositories.EventRepository;

import lombok.RequiredArgsConstructor;

@Service // Classe que representa um componente de Service (regra de negócios)
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository; // Instância que acessa o repositório de eventos

    private final AttendeeService attendeeService;

    public EventResponseDTO getEventDetails(String eventId) { // método de buscar o evento a partir do seu ID
        Event event = this.eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("Event not found with Id: " + eventId)); // retorna um optional (pode ou n existir). Para tratar, basta usar o método OrElseThrow
        List<Attendee> attendeeList = this.attendeeService.getAllAttendeesFromEvent(eventId);
        
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

    public AttendeeIdDTO registerAttendeeOnEvent(String eventId, AttendeRequestDTO attendeRequestDTO) {
        this.attendeeService.verifyAttendeeSubscription(attendeRequestDTO.email(), eventId);

        Event event = this.getEventById(eventId);
        List<Attendee> attendeeList = this.attendeeService.getAllAttendeesFromEvent(eventId);

        if(event.getMaximumAttendees() <= attendeeList.size()) throw new EventFullException("The event is full!");

        Attendee newAttendee = new Attendee();
        newAttendee.setName(attendeRequestDTO.name());
        newAttendee.setEmail(attendeRequestDTO.email());
        newAttendee.setEvent(event);
        newAttendee.setCreateAt(LocalDateTime.now());

        this.attendeeService.registerAttendee(newAttendee);

        return new AttendeeIdDTO(newAttendee.getId());
    }

    private Event getEventById(String eventId) {
        return this.eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("Event not found with Id: " + eventId));
    }
}
