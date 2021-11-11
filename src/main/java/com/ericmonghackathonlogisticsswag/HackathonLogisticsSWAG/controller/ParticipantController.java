package com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.controller;

import com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.entity.Participant;
import com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.exception.BadRequestException;
import com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.service.ParticipantService;
import com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.service.implementation.ParticipantServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path="api/v1/participant")
public class ParticipantController {
    private final ParticipantService participantService;

    @GetMapping
    public List<Participant> getParticipants() {
        return participantService.getAll();
    }

    @GetMapping("/search")
    public Participant getParticipant(@RequestParam(required = false) Long id, @RequestParam(required = false) String name) {
        if (id == null && name == null) throw new BadRequestException("Please provide either participant id or name of the participant");
        Participant participant = null;
        if (id != null) {
            participant = participantService.getById(id);
        } else {
            participant = participantService.getByName(name);
        }
        return participant;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Participant addParticipant(@RequestBody Participant participant) {
        // Check input
        checkParticipantInputInformation(participant);
        return participantService.add(participant);
    }

    public void checkParticipantInputInformation(Participant participant) {
        // Check whether are all input have a positive length except for teamName
        if (participant.getName().length() == 0 || participant.getEmail().length() == 0 || participant.getContactNumber().length() == 0 || participant.getTshirtSize().length() == 0) {
            throw new BadRequestException("Please provide your name, email, contact number and your tshirt size");
        }
    }
}
