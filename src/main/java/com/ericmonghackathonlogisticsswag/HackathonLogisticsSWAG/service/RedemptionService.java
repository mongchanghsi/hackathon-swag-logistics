package com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.service;

import com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.entity.Redemption;

import java.util.List;

public interface RedemptionService {
    List<Redemption> getRedemptions();
    Redemption getRedemptionById(Long id);
    void redeemSelfById(Long id);
    void redeemTeamById(Long id);
    void redeemSelfByName(String name);
    void redeemTeamByName(String name);
    void redeemTeamByTeamName(String teamName);
    void redeemSelfByEmail(String email);
    void redeemTeamByEmail(String email);
}
