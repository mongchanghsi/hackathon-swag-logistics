package com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.service.implementation;

import com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.entity.Participant;
import com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.entity.Redemption;
import com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.exception.BadRequestException;
import com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.exception.NotFoundException;
import com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.repository.ParticipantRepository;
import com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.repository.RedemptionRepository;
import com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.service.RedemptionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RedemptionServiceImpl implements RedemptionService {
    private final RedemptionRepository redemptionRepository;
    private final ParticipantRepository participantRepository;

    @Override
    public List<Redemption> getRedemptions() {
        return (List<Redemption>) redemptionRepository.findAll();
    }

    @Override
    public Redemption getRedemptionById(Long id) {
        Optional<Redemption> redemptionById = redemptionRepository.findById(id);
        if (redemptionById.isEmpty()) {
            throw new NotFoundException(String.format("Redemption of id %s does not exist", id));
        }

        return redemptionById.get();
    }

    @Override
    public void redeemSelfById(Long id) {
        Optional<Participant> participantById = participantRepository.findById(id);
        if (participantById.isEmpty()) {
            throw new NotFoundException(String.format("Participant of id %s does not exist", id));
        }

        Participant participantObj = participantById.get();

        if (participantObj.getRedemption() != null) {
            throw new BadRequestException(String.format("%s has already redeemed his/her SWAG", participantObj.getName()));
        }

        Redemption redemptionRecord = new Redemption(LocalDateTime.now());
        Redemption saved_redemptionRecord = redemptionRepository.save(redemptionRecord);

        participantObj.setRedemption(saved_redemptionRecord);
        participantRepository.save(participantObj);
    }

    @Override
    public void redeemTeamById(Long id) {
        Optional<Participant> participantById = participantRepository.findById(id);
        if (participantById.isEmpty()) {
            throw new NotFoundException(String.format("Participant of id %s does not exist", id));
        }

        List<Participant> participantByTeamName = participantRepository.findByTeamName(participantById.get().getTeamName());
        if (participantByTeamName.isEmpty()) {
            throw new NotFoundException("Team does not exist");
        }

        participantByTeamName.forEach(it -> {
            if (it.getRedemption() == null) {
                Redemption redemptionRecord = new Redemption(LocalDateTime.now());
                Redemption saved_redemptionRecord = redemptionRepository.save(redemptionRecord);

                it.setRedemption(saved_redemptionRecord);
                participantRepository.save(it);
            }
        });
    }

    @Override
    public void redeemSelfByName(String name) {
        Optional<Participant> participantByName = participantRepository.findByName(name);
        if (participantByName.isEmpty()) {
            throw new NotFoundException(String.format("%s does not exist", name));
        }

        Participant participantObj = participantByName.get();

        if (participantObj.getRedemption() != null) {
            throw new BadRequestException(String.format("%s has already redeemed his/her SWAG", name));
        }

        Redemption redemptionRecord = new Redemption(LocalDateTime.now());
        Redemption saved_redemptionRecord = redemptionRepository.save(redemptionRecord);

        participantObj.setRedemption(saved_redemptionRecord);
        participantRepository.save(participantObj);
    }

    @Override
    public void redeemTeamByName(String name) {
        Optional<Participant> participantByName = participantRepository.findByName(name);
        if (participantByName.isEmpty()) {
            throw new NotFoundException(String.format("%s does not exist", name));
        }

        List<Participant> participantByTeamName = participantRepository.findByTeamName(participantByName.get().getTeamName());
        if (participantByTeamName.isEmpty()) {
            throw new NotFoundException("Team does not exist");
        }

        participantByTeamName.forEach(it -> {
            if (it.getRedemption() == null) {
                Redemption redemptionRecord = new Redemption(LocalDateTime.now());
                Redemption saved_redemptionRecord = redemptionRepository.save(redemptionRecord);

                it.setRedemption(saved_redemptionRecord);
                participantRepository.save(it);
            }
        });
    }

    @Override
    public void redeemTeamByTeamName(String teamName) {
        List<Participant> participantByTeamName = participantRepository.findByTeamName(teamName);
        if (participantByTeamName.isEmpty()) {
            throw new NotFoundException(String.format("Team %s does not exist", teamName));
        }

        participantByTeamName.forEach(it -> {
            if (it.getRedemption() == null) {
                Redemption redemptionRecord = new Redemption(LocalDateTime.now());
                Redemption saved_redemptionRecord = redemptionRepository.save(redemptionRecord);

                it.setRedemption(saved_redemptionRecord);
                participantRepository.save(it);
            }
        });
    }

    @Override
    public void redeemSelfByEmail(String email) {
        Optional<Participant> participantByEmail = participantRepository.findByEmail(email);
        if (participantByEmail.isEmpty()) {
            throw new NotFoundException(String.format("Participant of email %s does not exist", email));
        }

        Participant participantObj = participantByEmail.get();

        if (participantObj.getRedemption() != null) {
            throw new BadRequestException(String.format("%s has already redeemed his/her SWAG", participantObj.getName()));
        }

        Redemption redemptionRecord = new Redemption(LocalDateTime.now());
        Redemption saved_redemptionRecord = redemptionRepository.save(redemptionRecord);

        participantObj.setRedemption(saved_redemptionRecord);
        participantRepository.save(participantObj);
    }

    @Override
    public void redeemTeamByEmail(String email) {
        Optional<Participant> participantByEmail = participantRepository.findByEmail(email);
        if (participantByEmail.isEmpty()) {
            throw new NotFoundException(String.format("Participant of email %s does not exist", email));
        }

        List<Participant> participantByTeamName = participantRepository.findByTeamName(participantByEmail.get().getTeamName());
        if (participantByTeamName.isEmpty()) {
            throw new NotFoundException("Team does not exist");
        }

        participantByTeamName.forEach(it -> {
            if (it.getRedemption() == null) {
                Redemption redemptionRecord = new Redemption(LocalDateTime.now());
                Redemption saved_redemptionRecord = redemptionRepository.save(redemptionRecord);

                it.setRedemption(saved_redemptionRecord);
                participantRepository.save(it);
            }
        });
    }
}
