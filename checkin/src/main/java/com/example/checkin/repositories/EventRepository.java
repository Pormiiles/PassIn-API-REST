package com.example.checkin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.checkin.domain.event.Event;

public interface EventRepository extends JpaRepository<Event, String> {
    
}
