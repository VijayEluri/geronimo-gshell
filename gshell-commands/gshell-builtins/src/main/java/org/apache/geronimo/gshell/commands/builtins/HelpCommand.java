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
import org.apache.geronimo.gshell.application.ApplicationManager;
import org.apache.geronimo.gshell.clp.Argument;
import org.apache.geronimo.gshell.command.CommandContainer;
import org.apache.geronimo.gshell.command.CommandSupport;
import org.apache.geronimo.gshell.command.annotation.CommandComponent;
import org.apache.geronimo.gshell.command.annotation.Requirement;
import org.apache.geronimo.gshell.layout.LayoutManager;
import org.apache.geronimo.gshell.model.layout.AliasNode;
import org.apache.geronimo.gshell.model.layout.CommandNode;
import org.apache.geronimo.gshell.model.layout.GroupNode;
import org.apache.geronimo.gshell.model.layout.Node;
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
    private ApplicationManager applicationManager;

    @Requirement
    private CommandContainer.Locator commandContainerLocator;

    @Requirement
    private LayoutManager layoutManager;

    @Argument(metaVar="COMMAND", description="Display help for COMMAND")
    private String command;

    private Renderer renderer = new Renderer();

    public HelpCommand() {}

    public HelpCommand(final CommandContainer.Locator commandContainerLocator, final LayoutManager layoutManager) {
        assert commandContainerLocator != null;
        assert layoutManager != null;

        this.commandContainerLocator = commandContainerLocator;
        this.layoutManager = layoutManager;
    }

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
        String about = applicationManager.getContext().getApplication().getBranding().getAboutMessage();

        if (about != null) {
            io.out.print(about);
            io.out.println();
        }

        io.out.println("Available commands:");

        GroupNode group = layoutManager.getLayout();

        displayGroupCommands(group);
    }

    private void displayGroupCommands(final GroupNode group) throws Exception {
        int maxNameLen = 20; // FIXME: Figure this out dynamically

        // First display command/aliases nodes
        for (Node child : group.nodes()) {
            if (child instanceof CommandNode) {
                try {
                    CommandNode node = (CommandNode) child;
                    String name = StringUtils.rightPad(node.getName(), maxNameLen);

                    CommandContainer container = commandContainerLocator.locate(node.getId());

                    // FIXME:
                    String desc = container.toString(); // command.getDescription();

                    io.out.print("  ");
                    io.out.print(renderer.render(Renderer.encode(name, Code.BOLD)));

                    if (desc != null) {
                        io.out.print("  ");
                        io.out.println(desc);
                    }
                    else {
                        io.out.println();
                    }
                } catch (/*NotRegistered*/Exception e) {
                    // Ignore those exceptions (command will not be displayed)
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

                String path = node.getPath();
                
                //
                // HACK: Until we get / and ../ stuff working, we have to strip off the leading "/"
                //
                //       https://issues.apache.org/jira/browse/GSHELL-86
                //
                if (path != null && path.startsWith("/")) {
                    path = path.substring(1, path.length());
                }
                
                io.out.print("  ");
                io.out.println(renderer.render(Renderer.encode(path, Code.BOLD)));

                io.out.println();
                displayGroupCommands(node);
                io.out.println();
            }
        }
    }

    private void displayCommandHelp(final String path) throws Exception {
        assert path != null;

        // FIXME:

        log.error("Unable to display command specific help for: {}", path);

        /*
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
        */
    }
}
