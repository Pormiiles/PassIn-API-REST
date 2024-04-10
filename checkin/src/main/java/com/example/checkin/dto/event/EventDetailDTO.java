package com.example.checkin.dto.event;

public record EventDetailDTO(String id, String title, String details, String slug, Integer maximunAttendees, Integer attendeesAmount) {
    
} // Objeto usado para transferência de dados
