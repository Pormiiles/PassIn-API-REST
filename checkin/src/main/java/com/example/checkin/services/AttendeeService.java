package com.example.checkin.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.checkin.domain.attendee.Attendee;
import com.example.checkin.domain.checkin.CheckIn;
import com.example.checkin.dto.attendee.AttendeeDetail;
import com.example.checkin.dto.attendee.AttendeeListResponseDTO;
import com.example.checkin.repositories.AttendeeRepository;
import com.example.checkin.repositories.CheckInRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendeeService {
    private final AttendeeRepository attendeeRepository;
    private final CheckInRepository checkInRepository;

    public List<Attendee> getAllAttendeesFromEvent(String eventId) {
        return this.attendeeRepository.findByEventId(eventId);
    }

    public AttendeeListResponseDTO getEventsAttendee(String eventId) {
        List<Attendee> attendeeList = this.getAllAttendeesFromEvent(eventId);
    
        List<AttendeeDetail> attendeeDetailsList = attendeeList.stream().map(attendee -> { Optional<CheckIn> checkIn = this.checkInRepository.findByAttendeeId(attendee.getId());
        LocalDateTime checkedInAt = checkIn.<LocalDateTime>map(CheckIn::getCreatedAt).orElse(null);
        
        return new AttendeeDetail(attendee.getId(), attendee.getName(), attendee.getEmail(), attendee.getCreateAt(), checkedInAt);
        }).toList();

        return new AttendeeListResponseDTO(attendeeDetailsList);
    }
}