package com.example.checkin.dto.attendee;

import java.time.LocalDateTime;

public record AttendeeDetail(String id, String name, String email, LocalDateTime createdAt, LocalDateTime checkedInAt) {
    
}
