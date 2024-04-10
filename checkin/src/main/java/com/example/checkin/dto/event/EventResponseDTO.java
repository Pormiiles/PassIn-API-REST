package com.example.checkin.dto.event;

import com.example.checkin.domain.event.Event;

public class EventResponseDTO { 
    EventDetailDTO event;

    public EventResponseDTO(Event event, Integer numberOfAttendees) {
        this.event = new EventDetailDTO(event.getId(), event.getTitle(), event.getDetails(), event.getSlug(), event.getMaximumAttendees(), numberOfAttendees);
    }
}
