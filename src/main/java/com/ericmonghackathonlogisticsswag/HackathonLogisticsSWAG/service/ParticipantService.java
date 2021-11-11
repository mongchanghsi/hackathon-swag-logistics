package com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.service;

import com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.entity.Participant;

import java.util.List;

public interface ParticipantService {
    List<Participant> getAll();
    Participant getById(Long id);
    Participant getByName(String name);
    Participant getByEmail(String email);
    Participant add(Participant participantInfo);
    void updateDetails(Long id, Participant participantInfo);
    void updateTShirtSize(Long id, String tShirtSize);
}
