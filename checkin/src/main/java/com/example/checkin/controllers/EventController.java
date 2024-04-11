package com.example.checkin.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.checkin.dto.attendee.AttendeeListResponseDTO;
import com.example.checkin.dto.event.EventIdDTO;
import com.example.checkin.dto.event.EventRequestDTO;
import com.example.checkin.dto.event.EventResponseDTO;
import com.example.checkin.services.AttendeeService;
import com.example.checkin.services.EventService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;
    private final AttendeeService attendeeService;

    @GetMapping("/{id}") // Path Variable - recebe o valor que o usuário forneceu
    public ResponseEntity<EventResponseDTO> getEvent(@PathVariable String id) {
        EventResponseDTO event = this.eventService.getEventDetails(id);

        return ResponseEntity.ok(event);
    }

    @PostMapping
    public ResponseEntity<EventIdDTO> createEvent(@RequestBody EventRequestDTO body, UriComponentsBuilder uri) {
        EventIdDTO eventIdDTO = this.eventService.createEvent(body);

        var uri2 = UriComponentsBuilder.fromPath("/events/{id}").buildAndExpand(eventIdDTO.eventId()).toUri();

        return ResponseEntity.created(uri2).body(eventIdDTO);
    }

    @GetMapping("/attendees/{id}")
    public ResponseEntity<AttendeeListResponseDTO> getEventAttendees(@PathVariable String id) {
        AttendeeListResponseDTO attendeesListResponse = this.attendeeService.getEventsAttendee(id);

        return ResponseEntity.ok(attendeesListResponse);
    }

}
