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

package org.apache.geronimo.gshell.commands.builtins;

import org.apache.geronimo.gshell.ansi.Code;
import org.apache.geronimo.gshell.ansi.Renderer;
import org.apache.geronimo.gshell.branding.Branding;
import org.apache.geronimo.gshell.clp.Argument;
import org.apache.geronimo.gshell.command.Command;
import org.apache.geronimo.gshell.command.CommandSupport;
import org.apache.geronimo.gshell.command.annotation.CommandComponent;
import org.apache.geronimo.gshell.command.annotation.Requirement;
import org.apache.geronimo.gshell.layout.LayoutManager;
import org.apache.geronimo.gshell.layout.model.AliasNode;
import org.apache.geronimo.gshell.layout.model.CommandNode;
import org.apache.geronimo.gshell.layout.model.GroupNode;
import org.apache.geronimo.gshell.layout.model.Node;
import org.apache.geronimo.gshell.registry.CommandRegistry;
import org.codehaus.plexus.util.StringUtils;

/**
 * Display help
 *
 * @version $Rev$ $Date$
 */
@CommandComponent(id="gshell-builtins:help", description="Show command help")
public class HelpCommand
    extends CommandSupport
{
    @Requirement
    private CommandRegistry commandRegistry;

    @Requirement
    private LayoutManager layoutManager;

    @Requirement
    private Branding branding;

    @Argument(description = "Command name")
    private String command;

    private Renderer renderer = new Renderer();

    protected Object doExecute() throws Exception {
        io.out.println();

        if (command == null) {
            displayAvailableCommands();
        }
        else {
            displayCommandHelp(command);
        }

        return SUCCESS;
    }

    private void displayAvailableCommands() throws Exception {
        io.out.print(branding.getAbout());
        io.out.println();
        io.out.println("Available commands:");

        GroupNode group = layoutManager.getLayout();

        displayGroupCommands(group);
    }

    private void displayGroupCommands(final GroupNode group) throws Exception {
        int maxNameLen = 20; // FIXME: Figure this out dynamically

        // First display command/aliases nodes
        for (Node child : group.nodes()) {
            if (child instanceof CommandNode) {
                CommandNode node = (CommandNode) child;
                String name = StringUtils.rightPad(node.getName(), maxNameLen);

                io.out.print("  ");
                io.out.print(renderer.render(Renderer.encode(name, Code.BOLD)));

                Command command = commandRegistry.lookup(node.getId());
                String desc = command.getDescription();

                if (desc != null) {
                    io.out.print("  ");
                    io.out.println(desc);
                }
                else {
                    io.out.println();
                }
            }
            else if (child instanceof AliasNode) {
                AliasNode node = (AliasNode) child;
                String name = StringUtils.rightPad(node.getName(), maxNameLen);

                io.out.print("  ");
                io.out.print(renderer.render(Renderer.encode(name, Code.BOLD)));
                io.out.print("  ");

                io.out.print("Alias to: ");
                io.out.println(renderer.render(Renderer.encode(node.getCommand(), Code.BOLD)));
            }
        }

        io.out.println();

        // Then groups
        for (Node child : group.nodes()) {
            if (child instanceof GroupNode) {
                GroupNode node = (GroupNode) child;

                io.out.print("  ");
                io.out.println(renderer.render(Renderer.encode(node.getPath(), Code.BOLD)));

                io.out.println();
                displayGroupCommands(node);
                io.out.println();
            }
        }
    }

    private void displayCommandHelp(final String path) throws Exception {
        assert path != null;

        Command cmd = commandRegistry.lookup(path);

        if (cmd == null) {
            io.out.println("Command " + Renderer.encode(path, Code.BOLD) + " not found.");
            io.out.println("Try " + Renderer.encode("help", Code.BOLD) + " for a list of available commands.");
        }
        else {
            io.out.println("Command " + Renderer.encode(path, Code.BOLD));
            io.out.println("   " + cmd.getDescription());
        }

        io.out.println();
    }
}