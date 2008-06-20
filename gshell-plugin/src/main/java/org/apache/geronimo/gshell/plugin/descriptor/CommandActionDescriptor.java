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

package org.apache.geronimo.gshell.plugin.descriptor;

import org.apache.geronimo.gshell.command.CommandAction;
import org.apache.geronimo.gshell.model.command.CommandModel;
import org.apache.geronimo.gshell.model.command.Parameter;
import org.apache.geronimo.gshell.model.command.Requirement;
import org.apache.geronimo.gshell.yarn.ReflectionToStringBuilder;
import org.apache.geronimo.gshell.yarn.ToStringStyle;
import org.codehaus.plexus.component.repository.ComponentRequirement;
import org.codehaus.plexus.configuration.xml.XmlPlexusConfiguration;

/**
 * Descriptor for a GShell command action's plexus component.
 *
 * @version $Rev$ $Date$
 */
public class CommandActionDescriptor
    extends ComponentDescriptorSupport
{
    private final CommandModel model;

    public CommandActionDescriptor(final CommandModel model) {
        assert model != null;

        this.model = model;

        setDescription(model.getDescription());
        setRole(CommandAction.class);
        setRoleHint(model.getId());
        setImplementation(model.getImplementation());
        setVersion(model.getVersion());
        setIsolatedRealm(false);
        setInstantiationStrategy("per-lookup");

        if (model.hasParameters()) {
            XmlPlexusConfiguration root = new XmlPlexusConfiguration("configuration");

            for (Parameter param : model.getParameters()) {
                root.addChild(new XmlPlexusConfiguration(param.getName(), param.getValue()));
            }

            setConfiguration(root);
        }

        if (model.hasRequirements()) {
            for (Requirement requirement : model.getRequirements()) {
                addRequirement(translate(requirement));
            }
        }
    }

    public CommandModel getCommand() {
        return model;
    }

    private ComponentRequirement translate(final Requirement source) {
        assert source != null;

        ComponentRequirement requirement = new ComponentRequirement();

        requirement.setRole(source.getType());
        requirement.setRoleHint(source.getId());
        requirement.setFieldName(source.getName());
        requirement.setFieldMappingType(null);

        return requirement;
    }
}