package com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.service;

import com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.entity.Participant;

import java.util.List;

public interface ParticipantService {
    List<Participant> getAll();
    Participant getById(Long id);
    Participant getByName(String name);
    Participant getByEmail(String email);
    Participant add(Participant participantInfo);
    Participant updateDetails(Long id, Participant participantInfo);
    Participant updateTShirtSize(Long id, String tShirtSize);
}
