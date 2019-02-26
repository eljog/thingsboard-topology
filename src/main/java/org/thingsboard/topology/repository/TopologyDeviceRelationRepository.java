package org.thingsboard.topology.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.thingsboard.topology.domain.TopologyDeviceRelation;

@Repository
public interface TopologyDeviceRelationRepository extends CrudRepository<TopologyDeviceRelation, Long> {
}
