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

package org.apache.geronimo.gshell.lookup;

import org.apache.geronimo.gshell.command.IO;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.factory.ComponentFactory;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;

/**
 * Lookup factory for {@link IO} instances.
 *
 * @version $Rev$ $Date$
 */
@Component(role=ComponentFactory.class, hint="IOLookup")
public class IOLookup
    extends LookupFactorySupport<IO>
{
    public static void set(final PlexusContainer container, final IO io) throws ComponentLookupException {
        assert container != null;

        IOLookup lookup = (IOLookup) container.lookup(ComponentFactory.class, IOLookup.class.getSimpleName());
        lookup.set(io);
    }
}
