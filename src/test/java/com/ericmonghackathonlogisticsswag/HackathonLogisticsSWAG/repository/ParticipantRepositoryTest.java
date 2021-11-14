package com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.repository;

import com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.entity.Participant;
import com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.enums.TShirtSizes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ParticipantRepositoryTest {
    @Autowired
    private ParticipantRepository participantRepositoryTest;

    // Basic mock values
    private String mockName = "mockName";
    private String mockEmail = "mockEmail";
    private String mockContactNumber = "mockContactNumber";
    private String mockTeam = "mockTeam";
    private String mockTshirtSize = TShirtSizes.M.toString();

    private String mockIncorrectName = "mockIncorrectName";
    private String mockIncorrectEmail = "mockIncorrectEmail";
    private String mockIncorrectTeamName = "mockIncorrectTeamName";

    @BeforeEach
    void setUp() {
        Participant participant = new Participant(mockName, mockEmail, mockContactNumber, mockTeam, mockTshirtSize);
        participantRepositoryTest.save(participant);
    }

    @AfterEach
    void tearDown() {
        participantRepositoryTest.deleteAll();
    }

    @Test
    void itShouldFindParticipantByNameIfExist() {
        Optional<Participant> doesParticipantExist = participantRepositoryTest.findByName(mockName);

        assertThat(doesParticipantExist.isPresent()).isTrue();
    }

    @Test
    void itShouldNotFindParticipantByNameIfDoesNotExist() {
        Optional<Participant> doesParticipantExist = participantRepositoryTest.findByName(mockIncorrectName);

        assertThat(doesParticipantExist.isPresent()).isFalse();
    }

    @Test
    void itShouldFindParticipantByEmailIfExist() {
        Optional<Participant> doesParticipantExist = participantRepositoryTest.findByEmail(mockEmail);

        assertThat(doesParticipantExist.isPresent()).isTrue();
    }

    @Test
    void itShouldNotFindParticipantByEmailIfDoesNotExist() {
        Optional<Participant> doesParticipantExist = participantRepositoryTest.findByEmail(mockIncorrectEmail);

        assertThat(doesParticipantExist.isPresent()).isFalse();
    }

    @Test
    void itShouldFindParticipantsByTeamNameIfExist() {
        List<Participant> doesParticipantExist = participantRepositoryTest.findByTeamName(mockTeam);

        assertThat(doesParticipantExist.isEmpty()).isFalse();
    }

    @Test
    void itShouldNotFindParticipantsByTeamNameIfDoesNotExist() {
        List<Participant> doesParticipantExist = participantRepositoryTest.findByTeamName(mockIncorrectTeamName);

        assertThat(doesParticipantExist.isEmpty()).isTrue();
    }
}