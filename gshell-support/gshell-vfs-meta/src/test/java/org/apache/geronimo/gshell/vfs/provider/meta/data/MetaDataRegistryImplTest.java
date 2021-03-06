/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.geronimo.gshell.vfs.provider.meta.data;

import org.apache.commons.vfs.FileName;
import org.apache.geronimo.gshell.spring.SpringTestSupport;
import org.apache.geronimo.gshell.vfs.provider.meta.AccessibleMetaDataRegistry;

import java.util.Map;

/**
 * Unit tests for the {@link MetaDataRegistryImpl} class.
 *
 * @version $Rev$ $Date$
 */
public class MetaDataRegistryImplTest
    extends SpringTestSupport
{
    private AccessibleMetaDataRegistry registry;

    protected void setUp() throws Exception {
        super.setUp();

        registry = getBeanContainer().getBean(AccessibleMetaDataRegistry.class);
        assertNotNull(registry);
    }

    public void testInitialState() throws Exception {
        Map<FileName, MetaData> nodes = registry.getNodes();
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
    }
}