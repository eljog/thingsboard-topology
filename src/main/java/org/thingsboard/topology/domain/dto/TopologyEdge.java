package org.thingsboard.topology.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopologyEdge {
    private Long fromId;
    private Long toId;
    private Long uplinkSpeed;
    private Long downlinkSpeed;
}
