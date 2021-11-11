package com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.entity;

import com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.enums.TShirtSizes;
import lombok.*;

import javax.persistence.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name="participant")
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name="email")
    private String email;

    @Column(name="contact_number")
    private String contactNumber;

    @Column(name="team_name")
    private String teamName;

    @Column(name="tshirt_size")
    private String tshirtSize;

    @OneToOne
    @JoinColumn(name="redemption_id", referencedColumnName="id")
    private Redemption redemption;

    public Participant(String name, String email, String contactNumber, String teamName, String tshirtSize) {
        this.name = name;
        this.email = email;
        this.contactNumber = contactNumber;
        this.teamName = teamName;
        this.tshirtSize = tshirtSize;
    }

    public void setRedemption(Redemption redemption) {
        this.redemption = redemption;
    }
}
