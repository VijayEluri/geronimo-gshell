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

package org.apache.geronimo.gshell.plugin;

import java.io.Reader;

import com.thoughtworks.xstream.XStreamException;
import org.apache.geronimo.gshell.descriptor.CommandSetDescriptor;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.discovery.AbstractComponentDiscoverer;
import org.codehaus.plexus.component.discovery.ComponentDiscoverer;
import org.codehaus.plexus.component.repository.ComponentSetDescriptor;
import org.codehaus.plexus.configuration.PlexusConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Plexus component discovery for GShell commands.
 *
 * @version $Rev$ $Date$
 */
@Component(role=ComponentDiscoverer.class, hint="command")
public class CommandDiscoverer
    extends AbstractComponentDiscoverer
{
    private final Logger log = LoggerFactory.getLogger(getClass());

    protected String getComponentDescriptorLocation() {
        return "META-INF/gshell/commands.xml";
    }

    protected ComponentSetDescriptor createComponentDescriptors(final Reader reader, final String source) throws PlexusConfigurationException {
        assert reader != null;
        assert source != null;

        log.debug("Loading descriptors from: {}", source);

        try {
            CommandSetDescriptor commands = CommandSetDescriptor.fromXML(reader);

            log.debug("Loaded command set: {}", commands.getId());

            return new ComponentSetDescriptorAdapter(commands);
        }
        catch (XStreamException e) {
            throw new PlexusConfigurationException("Failed to load descriptors from: " + source, e);
        }
    }
}