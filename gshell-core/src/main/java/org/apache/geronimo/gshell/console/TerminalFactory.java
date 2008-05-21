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

package org.apache.geronimo.gshell.console;

import jline.Terminal;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.classworlds.realm.ClassRealm;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.factory.ComponentFactory;
import org.codehaus.plexus.component.factory.ComponentInstantiationException;
import org.codehaus.plexus.component.repository.ComponentDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory for producing {@link Terminal} instances.
 *
 * @version $Rev$ $Date$
 */
@Component(role=ComponentFactory.class, hint="Terminal")
public class TerminalFactory
    implements ComponentFactory
{
    protected Logger log = LoggerFactory.getLogger(getClass());

    public String getId() {
        return getClass().getSimpleName();
    }

    public Object newInstance(ComponentDescriptor d, ClassRealm cr, PlexusContainer c) throws ComponentInstantiationException {
        Terminal term =  Terminal.getTerminal();

        log.debug("Handing out: {}", term);

        return term;
    }
}