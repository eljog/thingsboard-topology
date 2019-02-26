/**
 * Copyright © 2016-2019 The Thingsboard Authors
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.thingsboard.topology.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.stereotype.Service;
import org.thingsboard.topology.domain.DeviceStatus;
import org.thingsboard.topology.domain.DeviceType;
import org.thingsboard.topology.domain.TopologyDevice;
import org.thingsboard.topology.domain.TopologyDeviceRelation;
import org.thingsboard.topology.domain.dto.TopologyEdge;
import org.thingsboard.topology.domain.dto.TopologyNode;
import org.thingsboard.topology.repository.TopologyDeviceRepository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class TopologyService {

    private TopologyDeviceRepository topologyDeviceRepository;

    public TopologyService(TopologyDeviceRepository topologyDeviceRepository) {
        this.topologyDeviceRepository = topologyDeviceRepository;
    }

    public TopologyDevice fetchTopology(String macAddress) {
        return topologyDeviceRepository.findByMacAddress(macAddress);
    }

    public Map<String, List> fetchSimplifiedNodesAndEdges(String macAddress) {
        TopologyDevice parent = this.fetchTopology(macAddress);

        List<TopologyNode> nodes = new ArrayList<>();
        List<TopologyEdge> edges = new ArrayList<>();

        nodes.add(generateTopologyNode(parent));
        this.prepareNodesAndEdgesData(parent, nodes, edges);

        Map<String, List> nodeEdgeMap = new HashedMap<>();
        nodeEdgeMap.put("nodes", nodes);
        nodeEdgeMap.put("edges", edges);

        return nodeEdgeMap;
    }

    private void prepareNodesAndEdgesData(TopologyDevice device, List<TopologyNode> nodes, List<TopologyEdge> edges) {
        if(device.getRelatedDevices() != null) {
            for(TopologyDeviceRelation topologyDeviceRelation: device.getRelatedDevices()) {
                nodes.add(generateTopologyNode(topologyDeviceRelation.getChild()));
                edges.add(generateTopologyEdge(device, topologyDeviceRelation));

                prepareNodesAndEdgesData(topologyDeviceRelation.getChild(), nodes,edges);
            }
        }
    }

    private TopologyEdge generateTopologyEdge(TopologyDevice parent, TopologyDeviceRelation childRelation) {
        return new TopologyEdge(parent.getId(), childRelation.getChild().getId(), childRelation.getUplinkSpeed(), childRelation.getDownlinkSpeed());
    }

    private TopologyNode generateTopologyNode(TopologyDevice topologyDevice) {
        return new TopologyNode(topologyDevice.getId(), topologyDevice.getMacAddress(), topologyDevice.getDeviceType(), topologyDevice.getDeviceStatus());
    }

    public TopologyDevice storeTopology(JsonNode topologyJson) {
        Date timestamp = new Date();
        TopologyDevice device = this.parseTopologyJson(topologyJson, timestamp);
        return topologyDeviceRepository.save(device);
    }

    private TopologyDevice parseTopologyJson(JsonNode topologyJson, Date timestamp) {
        TopologyDevice device = createTopologyDeviceFromJson(topologyJson, timestamp);

        if (topologyJson.has(6)) {
            topologyJson.get(6).forEach(jsonNode -> {
                this.processChildren(device, jsonNode, timestamp);
            });
        }

        return device;
    }

    private void processChildren(TopologyDevice device, JsonNode topologyJson, Date timestamp) {
        TopologyDevice child = createTopologyDeviceFromJson(topologyJson, timestamp);

        connectToParent(device, child);

        if (topologyJson.has(6)) {
            topologyJson.get(6).forEach(jsonNode -> {
                this.processChildren(child, jsonNode, timestamp);
            });
        }

    }

    private void connectToParent(TopologyDevice device, TopologyDevice child) {
        TopologyDeviceRelation relation = new TopologyDeviceRelation();
        relation.setParent(device);
        relation.setChild(child);
        relation.setUplinkSpeed(child.getUplinkSpeed());
        relation.setDownlinkSpeed(child.getDownlinkSpeed());

        device.connectDevice(relation);
    }

    private TopologyDevice createTopologyDeviceFromJson(JsonNode topologyJson, Date timestamp) {
        TopologyDevice device = new TopologyDevice(timestamp);

        device.setMacAddress(topologyJson.get(0).asText());
        device.setIpv6Address(topologyJson.get(1).asText());
        device.setUplinkSpeed(Long.parseLong(topologyJson.get(2).asText(),16));
        device.setDownlinkSpeed(Long.parseLong(topologyJson.get(3).asText(),16));
        device.setDeviceType(DeviceType.fromInt(topologyJson.get(4).asInt()));
        device.setDeviceStatus(DeviceStatus.fromInt(topologyJson.get(5).asInt()));
        return device;
    }

    @PostConstruct
    public void test() {
        System.out.println("Found from DB");
//        System.out.println(topologyDeviceRepository.findByMacAddress("ABCD1234"));
    }
}
