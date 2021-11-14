package com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.service.implementation;

import com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.entity.Participant;
import com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.exception.BadRequestException;
import com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.exception.NotFoundException;
import com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.repository.ParticipantRepository;
import com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.service.ParticipantService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ParticipantServiceImpl implements ParticipantService {

    @Autowired
    private ParticipantRepository participantRepository;

    @Override
    public List<Participant> getAll() {
        return (List<Participant>) participantRepository.findAll();
    }

    @Override
    public Participant getById(Long id) {
        Optional<Participant> participantById = participantRepository.findById(id);
        if(participantById.isEmpty()) {
            throw new NotFoundException(String.format("Participant of id %s does not exist", id));
        }

        return participantById.get();
    }

    @Override
    public Participant getByName(String name) {
        Optional<Participant> participantByName = participantRepository.findByName(name);
        if(participantByName.isEmpty()) {
            throw new NotFoundException(String.format("Participant %s does not exist", name));
        }

        return participantByName.get();
    }

    @Override
    public Participant getByEmail(String email) {
        Optional<Participant> participantByEmail = participantRepository.findByEmail(email);
        if(participantByEmail.isEmpty()) {
            throw new NotFoundException(String.format("Participant of email %s does not exist", email));
        }

        return participantByEmail.get();
    }

    @Override
    public Participant add(Participant participantInfo) {
        Optional<Participant> participantByEmail = participantRepository.findByEmail(participantInfo.getEmail());
        if(participantByEmail.isPresent()) {
            throw new BadRequestException(String.format("Participant of email %s already exist", participantInfo.getEmail()));
        }

        return participantRepository.save(participantInfo);
    }

    @Override
    public Participant updateDetails(Long id, Participant participantInfo) {
        Optional<Participant> participantById = participantRepository.findById(id);
        if(participantById.isEmpty()) {
            throw new NotFoundException(String.format("Participant of id %s does not exist", id));
        }

        Participant participantObj = participantById.get();

        participantObj.setName(participantInfo.getName());
        participantObj.setEmail(participantInfo.getEmail());
        participantObj.setContactNumber(participantInfo.getContactNumber());
        participantObj.setTeamName(participantInfo.getTeamName());
        participantObj.setTshirtSize(participantInfo.getTshirtSize());

        participantRepository.save(participantObj);

        return participantById.get();
    }

    @Override
    public Participant updateTShirtSize(Long id, String tShirtSize) {
        Optional<Participant> participantById = participantRepository.findById(id);
        if(participantById.isEmpty()) {
            throw new NotFoundException(String.format("Participant of id %s does not exist", id));
        }

        Participant participantObj = participantById.get();
        participantObj.setTshirtSize(tShirtSize);

        participantRepository.save(participantObj);

        return participantById.get();
    }
}
