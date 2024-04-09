package com.example.checkin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.checkin.domain.checkin.CheckIn;

public interface CheckInRepository extends JpaRepository<CheckIn, Integer> {
    
}
