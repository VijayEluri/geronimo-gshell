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

import org.apache.geronimo.gshell.common.tostring.ReflectionToStringBuilder;
import org.apache.geronimo.gshell.common.tostring.ToStringStyle;
import org.apache.geronimo.gshell.model.command.Command;
import org.apache.geronimo.gshell.model.command.CommandSet;
import org.codehaus.plexus.component.repository.ComponentDescriptor;
import org.codehaus.plexus.component.repository.ComponentSetDescriptor;

/**
 * ???
 *
 * @version $Rev$ $Date$
 */
public class ComponentSetDescriptorAdapter
    extends ComponentSetDescriptor
{
    private final CommandSet commands;

    public ComponentSetDescriptorAdapter(final CommandSet commands) {
        assert commands != null;

        this.commands = commands;

        setId(commands.getId());

        setIsolatedRealm(false);

        if (!commands.isEmpty()) {
            for (Command command : commands.getCommands()) {
                ComponentDescriptor component = new ComponentDescriptorAdapter(command);
                
                addComponentDescriptor(component);

                //
                // TODO: Should we attach our selves?
                //
                // component.setComponentSetDescriptor(this);
            }
        }

        //
        // TODO: Need to figure out dependencies
        //
        
        setDependencies(null);
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public CommandSet getCommands() {
        return commands;
    }
}
