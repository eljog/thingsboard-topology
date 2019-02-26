/**
 * Copyright © 2016-2019 The Thingsboard Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.thingsboard.topology.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thingsboard.topology.domain.TopologyDevice;
import org.thingsboard.topology.service.TopologyService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TopologyController {

    private TopologyService topologyService;

    public TopologyController(TopologyService topologyService) {
        this.topologyService = topologyService;
    }

    @GetMapping("/topology/{macAddress}")
    @ResponseBody
    public ResponseEntity<TopologyDevice> getToplogy(@PathVariable("macAddress") String macAddress) {
        TopologyDevice topologyDevice = topologyService.fetchTopology(macAddress);
        System.out.println(topologyDevice);
        return ResponseEntity.status(HttpStatus.OK).body(topologyDevice);
    }

    @GetMapping("/simple-topology/{macAddress}")
    @ResponseBody
    public ResponseEntity<Map<String, List>> fetchSimplifiedNodesAndEdges(@PathVariable("macAddress") String macAddress) {
        return ResponseEntity.status(HttpStatus.OK).body(topologyService.fetchSimplifiedNodesAndEdges(macAddress));
    }

    @PostMapping("/topology")
    @ResponseBody
    public TopologyDevice storeTopology(@RequestBody JsonNode topologyJson) {
        return topologyService.storeTopology(topologyJson);
    }
}
