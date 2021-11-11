package com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.repository;

import com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.entity.Redemption;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RedemptionRepository extends CrudRepository<Redemption, Long> {
}
