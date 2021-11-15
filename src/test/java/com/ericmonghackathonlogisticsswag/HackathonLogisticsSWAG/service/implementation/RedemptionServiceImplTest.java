package com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.service.implementation;

import com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.entity.Participant;
import com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.entity.Redemption;
import com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.enums.TShirtSizes;
import com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.exception.BadRequestException;
import com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.exception.NotFoundException;
import com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.repository.ParticipantRepository;
import com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.repository.RedemptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RedemptionServiceImplTest {

    @Mock
    private RedemptionRepository mockRedemptionRepository;

    @Mock
    private ParticipantRepository mockParticipantRepository;

    private RedemptionServiceImpl redemptionServiceTest;
    private ParticipantServiceImpl mockParticipantService;

    @BeforeEach
    void setUp() {
        redemptionServiceTest = new RedemptionServiceImpl(mockRedemptionRepository, mockParticipantRepository);
        mockParticipantService = new ParticipantServiceImpl(mockParticipantRepository);
    }

    // Basic mock values
    private Long mockParticipantId = 1L;
    private Long mockRedemptionId = 1L;
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

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Test
    void canGetAllRedemptions() {
        redemptionServiceTest.getRedemptions();

        verify(mockRedemptionRepository).findAll();
    }

    @Test
    void canGetRedemptionById() {
        LocalDateTime dateTime = LocalDateTime.parse("1986-04-08 12:30", formatter);
        Redemption redemption = new Redemption(dateTime);

        Optional<Redemption> redemptionOptional = Optional.of(redemption);

        // To allow .findById to return an Optional object
        given(mockRedemptionRepository.findById(mockRedemptionId))
                .willReturn(redemptionOptional);

        redemptionServiceTest.getRedemptionById(mockRedemptionId);

        verify(mockRedemptionRepository).findById(mockRedemptionId);
    }

    @Test
    void cannotGetRedemptionById() {
        Optional<Redemption> redemptionOptional = Optional.empty();

        // To allow .findById to return an Optional object
        given(mockRedemptionRepository.findById(mockRedemptionId))
                .willReturn(redemptionOptional);

        assertThatThrownBy(() -> redemptionServiceTest.getRedemptionById(mockRedemptionId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(String.format("Redemption of id %s does not exist", mockRedemptionId));

        verify(mockRedemptionRepository).findById(mockRedemptionId);
    }

    @Test
    void canRedeemSelfById() {
        Participant participant = new Participant(mockParticipantId, mockName, mockEmail, mockContactNumber, mockTeam, mockTshirtSize);

        Optional<Participant> participantOptional = Optional.of(participant);

        given(mockParticipantRepository.findById(mockParticipantId))
                .willReturn(participantOptional);

        redemptionServiceTest.redeemSelfById(mockParticipantId);

        ArgumentCaptor<Redemption> redemptionArgumentCaptor = ArgumentCaptor.forClass(Redemption.class);

        verify(mockRedemptionRepository).save(redemptionArgumentCaptor.capture());

        verify(mockRedemptionRepository).save(any());
        verify(mockParticipantRepository).save(any());
    }

    @Test
    void cannotRedeemSelfByIdDueToNonExistentId() {
        Optional<Participant> participantOptional = Optional.empty();

        given(mockParticipantRepository.findById(mockParticipantId))
                .willReturn(participantOptional);

        assertThatThrownBy(() -> redemptionServiceTest.redeemSelfById(mockParticipantId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(String.format("Participant of id %s does not exist", mockParticipantId));

        verify(mockParticipantRepository).findById(mockParticipantId);
        verify(mockRedemptionRepository, never()).save(any());
        verify(mockParticipantRepository, never()).save(any());
    }

    @Test
    void cannotRedeemSelfByIdDueClaimed() {
        LocalDateTime dateTime = LocalDateTime.parse("1986-04-08 12:30", formatter);
        Redemption redemption = new Redemption(dateTime);
        Participant participant = new Participant(mockParticipantId, mockName, mockEmail, mockContactNumber, mockTeam, mockTshirtSize, redemption);

        Optional<Participant> participantOptional = Optional.of(participant);

        given(mockParticipantRepository.findById(mockParticipantId))
                .willReturn(participantOptional);

        assertThatThrownBy(() -> redemptionServiceTest.redeemSelfById(mockParticipantId))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining(String.format("%s has already redeemed his/her SWAG", mockName));

        verify(mockParticipantRepository).findById(mockParticipantId);
        verify(mockRedemptionRepository, never()).save(any());
        verify(mockParticipantRepository, never()).save(any());
    }

    @Test
    void canRedeemTeamById() {
        Participant participant1 = new Participant(mockName, mockEmail, mockContactNumber, mockTeam, mockTshirtSize);
        Participant participant2 = new Participant(mockName, mockEmail, mockContactNumber, mockTeam, mockTshirtSize);
        Participant participant3 = new Participant(mockName, mockEmail, mockContactNumber, mockTeam, mockTshirtSize);

        List<Participant> participantList = new ArrayList<Participant>();

        participantList.add(participant1);
        participantList.add(participant2);
        participantList.add(participant3);

        Optional<Participant> participantOptional = Optional.of(participant1);

        given(mockParticipantRepository.findById(mockParticipantId))
                .willReturn(participantOptional);

        given(mockParticipantRepository.findByTeamName(mockTeam))
                .willReturn(participantList);

        redemptionServiceTest.redeemTeamById(mockParticipantId);

        verify(mockRedemptionRepository, times(participantList.size())).save(any());
        verify(mockParticipantRepository, times(participantList.size())).save(any());
    }

    @Test
    void canRedeemTeamPartiallyById() {
        LocalDateTime dateTime = LocalDateTime.parse("1986-04-08 12:30", formatter);
        Redemption redemption = new Redemption(dateTime);
        Participant participant1 = new Participant(mockParticipantId, mockName, mockEmail, mockContactNumber, mockTeam, mockTshirtSize);
        Participant participant2 = new Participant(mockParticipantId, mockName, mockEmail, mockContactNumber, mockTeam, mockTshirtSize);
        Participant participant3 = new Participant(mockParticipantId, mockName, mockEmail, mockContactNumber, mockTeam, mockTshirtSize, redemption);

        List<Participant> participantList = new ArrayList<Participant>();

        participantList.add(participant1);
        participantList.add(participant2);
        participantList.add(participant3);

        Optional<Participant> participantOptional = Optional.of(participant1);

        given(mockParticipantRepository.findById(mockParticipantId))
                .willReturn(participantOptional);

        given(mockParticipantRepository.findByTeamName(mockTeam))
                .willReturn(participantList);

        redemptionServiceTest.redeemTeamById(mockParticipantId);

        verify(mockRedemptionRepository, times(participantList.size() - 1)).save(any());
        verify(mockParticipantRepository, times(participantList.size() - 1)).save(any());
    }

    @Test
    void cannotRedeemTeamByIdDueToNonExistentId() {
        Optional<Participant> participantOptional = Optional.empty();

        given(mockParticipantRepository.findById(mockParticipantId))
                .willReturn(participantOptional);

        assertThatThrownBy(() -> redemptionServiceTest.redeemSelfById(mockParticipantId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(String.format("Participant of id %s does not exist", mockParticipantId));

        verify(mockParticipantRepository).findById(mockParticipantId);
        verify(mockRedemptionRepository, never()).save(any());
        verify(mockParticipantRepository, never()).save(any());
    }

    @Test
    void canRedeemSelfByName() {
        Participant participant = new Participant(mockParticipantId, mockName, mockEmail, mockContactNumber, mockTeam, mockTshirtSize);

        Optional<Participant> participantOptional = Optional.of(participant);

        given(mockParticipantRepository.findByName(mockName))
                .willReturn(participantOptional);

        redemptionServiceTest.redeemSelfByName(mockName);

        ArgumentCaptor<Redemption> redemptionArgumentCaptor = ArgumentCaptor.forClass(Redemption.class);

        verify(mockRedemptionRepository).save(redemptionArgumentCaptor.capture());

        verify(mockRedemptionRepository).save(any());
        verify(mockParticipantRepository).save(any());
    }

    @Test
    void cannotRedeemSelfByNameDueToNonExistentName() {
        Optional<Participant> participantOptional = Optional.empty();

        given(mockParticipantRepository.findByName(mockName))
                .willReturn(participantOptional);

        assertThatThrownBy(() -> redemptionServiceTest.redeemSelfByName(mockName))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(String.format("%s does not exist", mockName));

        verify(mockParticipantRepository).findByName(mockName);
        verify(mockRedemptionRepository, never()).save(any());
        verify(mockParticipantRepository, never()).save(any());
    }

    @Test
    void cannotRedeemSelfByNameDueToClaimed() {
        LocalDateTime dateTime = LocalDateTime.parse("1986-04-08 12:30", formatter);
        Redemption redemption = new Redemption(dateTime);
        Participant participant = new Participant(mockParticipantId, mockName, mockEmail, mockContactNumber, mockTeam, mockTshirtSize, redemption);

        Optional<Participant> participantOptional = Optional.of(participant);

        given(mockParticipantRepository.findByName(mockName))
                .willReturn(participantOptional);

        assertThatThrownBy(() -> redemptionServiceTest.redeemSelfByName(mockName))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining(String.format("%s has already redeemed his/her SWAG", mockName));

        verify(mockParticipantRepository).findByName(mockName);
        verify(mockRedemptionRepository, never()).save(any());
        verify(mockParticipantRepository, never()).save(any());
    }

    @Test
    void canRedeemTeamByName() {
        Participant participant1 = new Participant(mockName, mockEmail, mockContactNumber, mockTeam, mockTshirtSize);
        Participant participant2 = new Participant(mockName, mockEmail, mockContactNumber, mockTeam, mockTshirtSize);
        Participant participant3 = new Participant(mockName, mockEmail, mockContactNumber, mockTeam, mockTshirtSize);

        List<Participant> participantList = new ArrayList<Participant>();

        participantList.add(participant1);
        participantList.add(participant2);
        participantList.add(participant3);

        Optional<Participant> participantOptional = Optional.of(participant1);

        given(mockParticipantRepository.findByName(mockName))
                .willReturn(participantOptional);

        given(mockParticipantRepository.findByTeamName(mockTeam))
                .willReturn(participantList);

        redemptionServiceTest.redeemTeamByName(mockName);

        verify(mockRedemptionRepository, times(participantList.size())).save(any());
        verify(mockParticipantRepository, times(participantList.size())).save(any());
    }

    @Test
    void canRedeemTeamPartiallyByName() {
        LocalDateTime dateTime = LocalDateTime.parse("1986-04-08 12:30", formatter);
        Redemption redemption = new Redemption(dateTime);
        Participant participant1 = new Participant(mockParticipantId, mockName, mockEmail, mockContactNumber, mockTeam, mockTshirtSize);
        Participant participant2 = new Participant(mockParticipantId, mockName, mockEmail, mockContactNumber, mockTeam, mockTshirtSize);
        Participant participant3 = new Participant(mockParticipantId, mockName, mockEmail, mockContactNumber, mockTeam, mockTshirtSize, redemption);

        List<Participant> participantList = new ArrayList<Participant>();

        participantList.add(participant1);
        participantList.add(participant2);
        participantList.add(participant3);

        Optional<Participant> participantOptional = Optional.of(participant1);

        given(mockParticipantRepository.findByName(mockName))
                .willReturn(participantOptional);

        given(mockParticipantRepository.findByTeamName(mockTeam))
                .willReturn(participantList);

        redemptionServiceTest.redeemTeamByName(mockName);

        verify(mockRedemptionRepository, times(participantList.size() - 1)).save(any());
        verify(mockParticipantRepository, times(participantList.size() - 1)).save(any());
    }

    @Test
    void cannotRedeemTeamByNameDueToNonExistentName() {
        Optional<Participant> participantOptional = Optional.empty();

        given(mockParticipantRepository.findByName(mockName))
                .willReturn(participantOptional);

        assertThatThrownBy(() -> redemptionServiceTest.redeemTeamByName(mockName))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(String.format("%s does not exist", mockName));

        verify(mockParticipantRepository).findByName(mockName);
        verify(mockRedemptionRepository, never()).save(any());
        verify(mockParticipantRepository, never()).save(any());
    }

    @Test
    void canRedeemTeamByTeamName() {
        Participant participant1 = new Participant(mockName, mockEmail, mockContactNumber, mockTeam, mockTshirtSize);
        Participant participant2 = new Participant(mockName, mockEmail, mockContactNumber, mockTeam, mockTshirtSize);
        Participant participant3 = new Participant(mockName, mockEmail, mockContactNumber, mockTeam, mockTshirtSize);

        List<Participant> participantList = new ArrayList<Participant>();

        participantList.add(participant1);
        participantList.add(participant2);
        participantList.add(participant3);

        given(mockParticipantRepository.findByTeamName(mockTeam))
                .willReturn(participantList);

        redemptionServiceTest.redeemTeamByTeamName(mockTeam);

        verify(mockRedemptionRepository, times(participantList.size())).save(any());
        verify(mockParticipantRepository, times(participantList.size())).save(any());
    }

    @Test
    void canRedeemTeamPartiallyByTeamName() {
        LocalDateTime dateTime = LocalDateTime.parse("1986-04-08 12:30", formatter);
        Redemption redemption = new Redemption(dateTime);
        Participant participant1 = new Participant(mockParticipantId, mockName, mockEmail, mockContactNumber, mockTeam, mockTshirtSize);
        Participant participant2 = new Participant(mockParticipantId, mockName, mockEmail, mockContactNumber, mockTeam, mockTshirtSize);
        Participant participant3 = new Participant(mockParticipantId, mockName, mockEmail, mockContactNumber, mockTeam, mockTshirtSize, redemption);

        List<Participant> participantList = new ArrayList<Participant>();

        participantList.add(participant1);
        participantList.add(participant2);
        participantList.add(participant3);

        Optional<Participant> participantOptional = Optional.of(participant1);

        given(mockParticipantRepository.findByTeamName(mockTeam))
                .willReturn(participantList);

        redemptionServiceTest.redeemTeamByTeamName(mockTeam);

        verify(mockRedemptionRepository, times(participantList.size() - 1)).save(any());
        verify(mockParticipantRepository, times(participantList.size() - 1)).save(any());
    }

    @Test
    void cannotRedeemTeamByTeamNameDueToNonExistentTeamName() {
        List<Participant> participantList = new ArrayList<>();

        given(mockParticipantRepository.findByTeamName(mockTeam))
                .willReturn(participantList);

        assertThatThrownBy(() -> redemptionServiceTest.redeemTeamByTeamName(mockTeam))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(String.format("Team %s does not exist", mockTeam));

        verify(mockParticipantRepository).findByTeamName(mockTeam);
        verify(mockRedemptionRepository, never()).save(any());
        verify(mockParticipantRepository, never()).save(any());
    }

    @Test
    void canRedeemSelfByEmail() {
        Participant participant = new Participant(mockParticipantId, mockName, mockEmail, mockContactNumber, mockTeam, mockTshirtSize);

        Optional<Participant> participantOptional = Optional.of(participant);

        given(mockParticipantRepository.findByEmail(mockEmail))
                .willReturn(participantOptional);

        redemptionServiceTest.redeemSelfByEmail(mockEmail);

        ArgumentCaptor<Redemption> redemptionArgumentCaptor = ArgumentCaptor.forClass(Redemption.class);

        verify(mockRedemptionRepository).save(redemptionArgumentCaptor.capture());

        verify(mockRedemptionRepository).save(any());
        verify(mockParticipantRepository).save(any());
    }

    @Test
    void cannotRedeemSelfByEmailDueToNonExistentEmail() {
        Optional<Participant> participantOptional = Optional.empty();

        given(mockParticipantRepository.findByEmail(mockEmail))
                .willReturn(participantOptional);

        assertThatThrownBy(() -> redemptionServiceTest.redeemSelfByEmail(mockEmail))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(String.format("Participant of email %s does not exist", mockEmail));

        verify(mockParticipantRepository).findByEmail(mockEmail);
        verify(mockRedemptionRepository, never()).save(any());
        verify(mockParticipantRepository, never()).save(any());
    }

    @Test
    void cannotRedeemSelfByEmailDueToClaimed() {
        LocalDateTime dateTime = LocalDateTime.parse("1986-04-08 12:30", formatter);
        Redemption redemption = new Redemption(dateTime);
        Participant participant = new Participant(mockParticipantId, mockName, mockEmail, mockContactNumber, mockTeam, mockTshirtSize, redemption);

        Optional<Participant> participantOptional = Optional.of(participant);

        given(mockParticipantRepository.findByEmail(mockEmail))
                .willReturn(participantOptional);

        assertThatThrownBy(() -> redemptionServiceTest.redeemSelfByEmail(mockEmail))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining(String.format("%s has already redeemed his/her SWAG", mockName));

        verify(mockParticipantRepository).findByEmail(mockEmail);
        verify(mockRedemptionRepository, never()).save(any());
        verify(mockParticipantRepository, never()).save(any());
    }

    @Test
    void canRedeemTeamByEmail() {
        Participant participant1 = new Participant(mockName, mockEmail, mockContactNumber, mockTeam, mockTshirtSize);
        Participant participant2 = new Participant(mockName, mockEmail, mockContactNumber, mockTeam, mockTshirtSize);
        Participant participant3 = new Participant(mockName, mockEmail, mockContactNumber, mockTeam, mockTshirtSize);

        List<Participant> participantList = new ArrayList<Participant>();

        participantList.add(participant1);
        participantList.add(participant2);
        participantList.add(participant3);

        Optional<Participant> participantOptional = Optional.of(participant1);

        given(mockParticipantRepository.findByEmail(mockEmail))
                .willReturn(participantOptional);

        given(mockParticipantRepository.findByTeamName(mockTeam))
                .willReturn(participantList);

        redemptionServiceTest.redeemTeamByEmail(mockEmail);

        verify(mockRedemptionRepository, times(participantList.size())).save(any());
        verify(mockParticipantRepository, times(participantList.size())).save(any());
    }

    @Test
    void cannotRedeemTeamPartiallyByEmail() {
        LocalDateTime dateTime = LocalDateTime.parse("1986-04-08 12:30", formatter);
        Redemption redemption = new Redemption(dateTime);
        Participant participant1 = new Participant(mockParticipantId, mockName, mockEmail, mockContactNumber, mockTeam, mockTshirtSize);
        Participant participant2 = new Participant(mockParticipantId, mockName, mockEmail, mockContactNumber, mockTeam, mockTshirtSize);
        Participant participant3 = new Participant(mockParticipantId, mockName, mockEmail, mockContactNumber, mockTeam, mockTshirtSize, redemption);

        List<Participant> participantList = new ArrayList<Participant>();

        participantList.add(participant1);
        participantList.add(participant2);
        participantList.add(participant3);

        Optional<Participant> participantOptional = Optional.of(participant1);

        given(mockParticipantRepository.findByEmail(mockEmail))
                .willReturn(participantOptional);

        given(mockParticipantRepository.findByTeamName(mockTeam))
                .willReturn(participantList);

        redemptionServiceTest.redeemTeamByEmail(mockEmail);

        verify(mockRedemptionRepository, times(participantList.size() - 1)).save(any());
        verify(mockParticipantRepository, times(participantList.size() - 1)).save(any());
    }

    @Test
    void cannotRedeemTeamByEmailDueToNonExistentEmail() {
        Optional<Participant> participantOptional = Optional.empty();

        given(mockParticipantRepository.findByEmail(mockEmail))
                .willReturn(participantOptional);

        assertThatThrownBy(() -> redemptionServiceTest.redeemTeamByEmail(mockEmail))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(String.format("Participant of email %s does not exist", mockEmail));

        verify(mockParticipantRepository).findByEmail(mockEmail);
        verify(mockRedemptionRepository, never()).save(any());
        verify(mockParticipantRepository, never()).save(any());
    }
}