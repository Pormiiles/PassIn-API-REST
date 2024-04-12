package com.example.checkin.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.checkin.domain.attendee.Attendee;
import com.example.checkin.domain.attendee.exceptions.AttendeeAlreadyExistsException;
import com.example.checkin.domain.attendee.exceptions.AttendeeNotFoundException;
import com.example.checkin.domain.checkin.CheckIn;
import com.example.checkin.dto.attendee.AttendeeBadgeResponseDTO;
import com.example.checkin.dto.attendee.AttendeeDetail;
import com.example.checkin.dto.attendee.AttendeeListResponseDTO;
import com.example.checkin.dto.attendee.AttendeeBadgeDTO;
import com.example.checkin.repositories.AttendeeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendeeService {
    private final AttendeeRepository attendeeRepository;
    private final CheckInService checkInService;

    public List<Attendee> getAllAttendeesFromEvent(String eventId) {
        return this.attendeeRepository.findByEventId(eventId);
    }

    public AttendeeListResponseDTO getEventsAttendee(String eventId) {
        List<Attendee> attendeeList = this.getAllAttendeesFromEvent(eventId);
    
        List<AttendeeDetail> attendeeDetailsList = attendeeList.stream().map(attendee -> { Optional<CheckIn> checkIn = this.checkInService.getCheckIn(attendee.getId());
        LocalDateTime checkedInAt = checkIn.<LocalDateTime>map(CheckIn::getCreatedAt).orElse(null);
        
        return new AttendeeDetail(attendee.getId(), attendee.getName(), attendee.getEmail(), attendee.getCreateAt(), checkedInAt);
        }).toList();

        return new AttendeeListResponseDTO(attendeeDetailsList);
    }

    public Attendee registerAttendee(Attendee newAttendee) {
        this.attendeeRepository.save(newAttendee);

        return newAttendee;
    }

    public void verifyAttendeeSubscription(String email, String eventId){
        Optional<Attendee> isAttendeeRegistered = this.attendeeRepository.findByEventIdAndEmail(eventId, email);

        if(isAttendeeRegistered.isPresent()) throw new AttendeeAlreadyExistsException("Attendee is already registered!");
    }

    public AttendeeBadgeResponseDTO getAttendeeBadge(String attendeeId, UriComponentsBuilder uri) {
        Attendee attendee = this.getAttendee(attendeeId);

        var uri2 = UriComponentsBuilder.fromPath("/attendees/{attendeeId}/check-in").buildAndExpand(attendeeId).toUri().toString();

        AttendeeBadgeDTO badgeDTO = new AttendeeBadgeDTO(attendee.getName(), attendee.getEmail(), uri2, attendee.getEvent().getId());

        return new AttendeeBadgeResponseDTO(badgeDTO);
    }

    public void checkInAttendee(String attendeeId) {
        Attendee attendee = this.getAttendee(attendeeId);
        this.checkInService.registerCheckIn(attendee);
    }

    private Attendee getAttendee(String attendeeId) {
        return this.attendeeRepository.findById(attendeeId).orElseThrow(() -> new AttendeeNotFoundException("Attendee not found with ID: " + attendeeId));
    }
}