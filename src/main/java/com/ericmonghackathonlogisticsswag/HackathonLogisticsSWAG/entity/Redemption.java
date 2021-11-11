package com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name="redemption")
public class Redemption {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(name="redeem_at")
    private LocalDateTime redeemedAt;

    @JsonIgnore
    @OneToOne(mappedBy="redemption")
    private Participant redemptionBy;

    public Redemption(LocalDateTime redeemedAt) {
        this.redeemedAt = redeemedAt;
    }
}
