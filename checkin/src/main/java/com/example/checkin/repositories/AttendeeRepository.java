package com.example.checkin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.checkin.domain.attendee.Attendee;

public interface AttendeeRepository extends JpaRepository<Attendee, String> {
    
}
