package com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.repository;

import com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.entity.Participant;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipantRepository extends CrudRepository<Participant, Long> {
    Optional<Participant> findByName(String name);
    Optional<Participant> findByEmail(String email);
    List<Participant> findByTeamName(String teamName);
}
