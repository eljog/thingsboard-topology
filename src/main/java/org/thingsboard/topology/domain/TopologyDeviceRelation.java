package org.thingsboard.topology.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.neo4j.ogm.annotation.*;

@Getter
@Setter
@RelationshipEntity(type = "CONNECTS")
public class TopologyDeviceRelation {

        @GraphId
        private Long relationshipId;

        @Property
        private Long uplinkSpeed;
        @Property
        private Long downlinkSpeed;

        @StartNode
        @JsonIgnore
        private TopologyDevice parent;
        @EndNode
//        @JsonIgnore
        private TopologyDevice child;
}
