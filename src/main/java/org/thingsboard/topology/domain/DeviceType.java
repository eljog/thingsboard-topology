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
package org.thingsboard.topology.domain;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum DeviceType {
    Master(4),
    Slave(1),
    Repeater(6),
    Unknown(0);

    private final int id;

    DeviceType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    private static Map<Integer, DeviceType> reverseLookup =
            Arrays.stream(DeviceType.values()).collect(Collectors.toMap(DeviceType::getId, Function.identity()));

    public static DeviceType fromInt(final int id) {
        return reverseLookup.getOrDefault(id, Unknown);
    }
}
