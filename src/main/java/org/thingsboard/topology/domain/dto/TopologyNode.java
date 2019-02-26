package org.thingsboard.topology.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.thingsboard.topology.domain.DeviceStatus;
import org.thingsboard.topology.domain.DeviceType;

@Data
@AllArgsConstructor
public class TopologyNode {
    private Long id;
    private String label;
    private DeviceType deviceType;
    private DeviceStatus deviceStatus;
}
