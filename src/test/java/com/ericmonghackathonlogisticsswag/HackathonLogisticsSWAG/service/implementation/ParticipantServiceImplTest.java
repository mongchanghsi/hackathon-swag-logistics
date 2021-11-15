package com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.service.implementation;

import com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.entity.Participant;
import com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.enums.TShirtSizes;
import com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.exception.BadRequestException;
import com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.exception.NotFoundException;
import com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.repository.ParticipantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ParticipantServiceImplTest {

    @Mock
    private ParticipantRepository mockParticipantRepository;

    private ParticipantServiceImpl participantServiceTest;

    @BeforeEach
    void setUp() {
        participantServiceTest = new ParticipantServiceImpl(mockParticipantRepository);
    }

    // Basic mock values
    private Long mockParticipantId = 1L;
    private String mockName = "mockName";
    private String mockEmail = "mockEmail";
    private String mockContactNumber = "mockContactNumber";
    private String mockTeam = "mockTeam";
    private String mockTshirtSize = TShirtSizes.M.toString();

    private String mockIncorrectName = "mockIncorrectName";
    private String mockIncorrectEmail = "mockIncorrectEmail";
    private String mockIncorrectTeamName = "mockIncorrectTeamName";

    private String mockNewContactNumber = "mockNewContactNumber";
    private String mockNewTShirtSize = TShirtSizes.S.toString();

    @Test
    void canGetAllParticipants() {
        participantServiceTest.getAll();

        verify(mockParticipantRepository).findAll();
    }

    @Test
    void canGetParticipantById() {
        Participant participant = new Participant(mockParticipantId, mockName, mockEmail, mockContactNumber, mockTeam, mockTshirtSize);

        Optional<Participant> participantOptional = Optional.of(participant);

        // To allow .findById to return an Optional object
        given(mockParticipantRepository.findById(mockParticipantId))
                .willReturn(participantOptional);

        participantServiceTest.getById(mockParticipantId);

        verify(mockParticipantRepository).findById(mockParticipantId);
    }

    @Test
    void cannotGetParticipantById() {
        Optional<Participant> participantOptional = Optional.empty();

        // To allow .findById to return an Optional object
        given(mockParticipantRepository.findById(mockParticipantId))
                .willReturn(participantOptional);

        assertThatThrownBy(() -> participantServiceTest.getById(mockParticipantId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(String.format("Participant of id %s does not exist", mockParticipantId));


        verify(mockParticipantRepository).findById(mockParticipantId);
    }

    @Test
    void canGetParticipantByName() {
        Participant participant = new Participant(mockParticipantId, mockName, mockEmail, mockContactNumber, mockTeam, mockTshirtSize);

        Optional<Participant> participantOptional = Optional.of(participant);

        // To allow .findByName to return an Optional object
        given(mockParticipantRepository.findByName(mockName))
                .willReturn(participantOptional);

        participantServiceTest.getByName(mockName);

        verify(mockParticipantRepository).findByName(mockName);
    }

    @Test
    void cannotGetParticipantByName() {
        Optional<Participant> participantOptional = Optional.empty();

        // To allow .findByName to return an Optional object
        given(mockParticipantRepository.findByName(mockName))
                .willReturn(participantOptional);

        assertThatThrownBy(() -> participantServiceTest.getByName(mockName))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(String.format("Participant %s does not exist", mockName));


        verify(mockParticipantRepository).findByName(mockName);
    }

    @Test
    void canGetParticipantByEmail() {
        Participant participant = new Participant(mockParticipantId, mockName, mockEmail, mockContactNumber, mockTeam, mockTshirtSize);

        Optional<Participant> participantOptional = Optional.of(participant);

        // To allow .findByEmail to return an Optional object
        given(mockParticipantRepository.findByEmail(mockEmail))
                .willReturn(participantOptional);

        participantServiceTest.getByEmail(mockEmail);

        verify(mockParticipantRepository).findByEmail(mockEmail);
    }

    @Test
    void cannotGetParticipantByEmail() {
        Optional<Participant> participantOptional = Optional.empty();

        // To allow .findByEmail to return an Optional object
        given(mockParticipantRepository.findByEmail(mockEmail))
                .willReturn(participantOptional);

        assertThatThrownBy(() -> participantServiceTest.getByEmail(mockEmail))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(String.format("Participant of email %s does not exist", mockEmail));


        verify(mockParticipantRepository).findByEmail(mockEmail);
    }

    @Test
    void canAddInANewParticipant() {
        Participant participant = new Participant(mockName, mockEmail, mockContactNumber, mockTeam, mockTshirtSize);

        participantServiceTest.add(participant);

        ArgumentCaptor<Participant> participantArgumentCaptor = ArgumentCaptor.forClass(Participant.class);

        verify(mockParticipantRepository).save(participantArgumentCaptor.capture());

        Participant capturedParticipant = participantArgumentCaptor.getValue();

        assertThat(capturedParticipant).isEqualTo(participant);

        verify(mockParticipantRepository).save(participant);
    }

    @Test
    void shouldThrowErrorMessageDueToExistingEmailOnAdding() {
        Participant participant = new Participant(mockName, mockEmail, mockContactNumber, mockTeam, mockTshirtSize);

        Optional<Participant> participantOptional = Optional.of(participant);

        given(mockParticipantRepository.findByEmail(mockEmail))
                .willReturn(participantOptional);

        assertThatThrownBy(() -> participantServiceTest.add(participant))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining(String.format("Participant of email %s already exist", mockEmail));

        verify(mockParticipantRepository, never()).save(any());
    }

    @Test
    void canUpdateParticipantDetails() {
        Participant participant = new Participant(mockName, mockEmail, mockContactNumber, mockTeam, mockTshirtSize);
        Participant updatingParticipantInput = new Participant(mockName, mockEmail, mockNewContactNumber, mockTeam, mockTshirtSize);

        Optional<Participant> participantOptional = Optional.of(participant);

        given(mockParticipantRepository.findById(mockParticipantId))
                .willReturn(participantOptional);

        Participant returnParticipant = participantServiceTest.updateDetails(mockParticipantId, updatingParticipantInput);

        verify(mockParticipantRepository).findById(mockParticipantId);

        assertThat(returnParticipant.getName()).isEqualTo(updatingParticipantInput.getName());
    }

    @Test
    void shouldThrowErrorMessageDueToNonExistantParticipantDuringUpdateDetails() {
        Participant updatingParticipantInput = new Participant(mockName, mockEmail, mockNewContactNumber, mockTeam, mockTshirtSize);

        Optional<Participant> participantOptional = Optional.empty();

        given(mockParticipantRepository.findById(mockParticipantId))
                .willReturn(participantOptional);

        assertThatThrownBy(() -> participantServiceTest.updateDetails(mockParticipantId, updatingParticipantInput))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(String.format("Participant of id %s does not exist", mockParticipantId));

        verify(mockParticipantRepository, never()).save(any());
    }

    @Test
    void canUpdateParticipantTShirtSize() {
        Participant participant = new Participant(mockName, mockEmail, mockContactNumber, mockTeam, mockTshirtSize);

        Optional<Participant> participantOptional = Optional.of(participant);

        given(mockParticipantRepository.findById(mockParticipantId))
                .willReturn(participantOptional);

        Participant returnParticipant = participantServiceTest.updateTShirtSize(mockParticipantId, mockNewTShirtSize);

        verify(mockParticipantRepository).findById(mockParticipantId);

        assertThat(returnParticipant.getTshirtSize()).isEqualTo(mockNewTShirtSize);
    }

    @Test
    void shouldThrowErrorMessageDueToNonExistantParticipantDuringUpdateTshirtSize() {
        Optional<Participant> participantOptional = Optional.empty();

        given(mockParticipantRepository.findById(mockParticipantId))
                .willReturn(participantOptional);

        assertThatThrownBy(() -> participantServiceTest.updateTShirtSize(mockParticipantId, mockNewTShirtSize))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(String.format("Participant of id %s does not exist", mockParticipantId));

        verify(mockParticipantRepository, never()).save(any());
    }
}