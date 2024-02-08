package com.example.partypurse.services;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LinkService {
    public String createInvitationLink() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
