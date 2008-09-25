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

package org.apache.geronimo.gshell.wisdom.alias;

import org.apache.geronimo.gshell.command.CommandResult;
import org.apache.geronimo.gshell.shell.ShellContext;
import org.apache.geronimo.gshell.wisdom.command.CommandSupport;

/**
 * Alias {@link org.apache.geronimo.gshell.command.Command} component.
 *
 * @version $Rev$ $Date$
 */
public class AliasCommand
    extends CommandSupport
{
    private String name;

    private String target;

    public AliasCommand(final String name, final String target) {
        assert name != null;
        assert target != null;

        this.name = name;
        this.target = target;
    }
    
    @Override
    public CommandResult execute(final ShellContext context, final Object[] args) {
        assert context != null;
        assert args != null;

        log.debug("Executing");

        throw new Error("not implemented");
    }
}
