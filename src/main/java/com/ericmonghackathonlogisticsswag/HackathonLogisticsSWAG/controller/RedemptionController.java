package com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.controller;

import com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.entity.Redemption;
import com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.exception.BadRequestException;
import com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.service.RedemptionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path="api/v1/redemption")
public class RedemptionController {
    private final RedemptionService redemptionService;

    @GetMapping
    public List<Redemption> getRedemptions() { return redemptionService.getRedemptions(); };

    @GetMapping("/search")
    public Redemption getRedemption(@RequestParam(required = true) Long id) {
        if (id == null) throw new BadRequestException("Please provide a redemption id");

        return redemptionService.getRedemptionById(id);
    }

    @PostMapping("/self")
    @ResponseStatus(HttpStatus.CREATED)
    public void redeemSelf(@RequestParam(required = false) Long id, @RequestParam(required = false) String name, @RequestParam(required = false) String email) {
        if (id == null && name == null && email == null) throw new BadRequestException("Please provide an id or name of the participant");
        if (id != null) {
            redemptionService.redeemSelfById(id);
        } else if (name != null ){
            redemptionService.redeemSelfByName(name);
        } else {
            redemptionService.redeemSelfByEmail(email);
        }
    }

    @PostMapping("/team")
    @ResponseStatus(HttpStatus.CREATED)
    public void redeemTeam(@RequestParam(required = false) Long id, @RequestParam(required = false) String name, @RequestParam(required = false) String teamName, @RequestParam(required = false) String email) {
        if (id == null && name == null && teamName == null && email == null) throw new BadRequestException("Please provide an id, email or name of the participant or the team name that the participant belong to");

        if (id != null) {
            redemptionService.redeemTeamById(id);
        } else if (name != null) {
            redemptionService.redeemTeamByName(name);
        } else if (email != null){
            redemptionService.redeemTeamByEmail(email);
        } else {
            redemptionService.redeemTeamByTeamName(teamName);
        }
    }
}
