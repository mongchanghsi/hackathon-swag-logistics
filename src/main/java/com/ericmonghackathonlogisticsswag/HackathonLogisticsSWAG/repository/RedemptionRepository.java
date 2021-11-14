package com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.repository;

import com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.entity.Redemption;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedemptionRepository extends CrudRepository<Redemption, Long> {
}
