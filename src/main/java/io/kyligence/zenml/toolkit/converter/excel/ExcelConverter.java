/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.kyligence.zenml.toolkit.converter.excel;

import io.kyligence.zenml.toolkit.converter.MetricsConverter;
import io.kyligence.zenml.toolkit.model.zenml.Metrics;

public class ExcelConverter implements MetricsConverter {
    @Override
    public Metrics convert2Metrics(String filePath) {
        //TODO, convert excel to metrics
        return null;
    }
}
