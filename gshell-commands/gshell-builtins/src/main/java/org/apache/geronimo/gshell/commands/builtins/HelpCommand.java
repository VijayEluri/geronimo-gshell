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
import org.apache.geronimo.gshell.command.*;
import org.apache.geronimo.gshell.command.annotation.CommandComponent;
import org.apache.geronimo.gshell.command.annotation.Requirement;
import org.apache.geronimo.gshell.io.IO;
import org.apache.geronimo.gshell.layout.LayoutManager;
import org.apache.geronimo.gshell.model.layout.AliasNode;
import org.apache.geronimo.gshell.model.layout.CommandNode;
import org.apache.geronimo.gshell.model.layout.GroupNode;
import org.apache.geronimo.gshell.model.layout.Node;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Display help
 *
 * @version $Rev$ $Date$
 */
@CommandComponent(id="gshell-builtins:help", description="Show command help")
public class HelpCommand
    implements CommandAction
{
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Requirement
    private ApplicationManager applicationManager;

    @Requirement
    private CommandResolver commandResolver;

    @Requirement
    private CommandContainerFactory commandContainerFactory;

    @Requirement
    private LayoutManager layoutManager;

    @Argument(metaVar="COMMAND", required=true, description="Display help for COMMAND")
    private String command;

    private Renderer renderer = new Renderer();

    public HelpCommand() {}

    public HelpCommand(final CommandResolver commandResolver, final LayoutManager layoutManager) {
        assert commandResolver != null;
        assert layoutManager != null;

        this.commandResolver = commandResolver;
        this.layoutManager = layoutManager;
    }

    public Object execute(final CommandContext context) throws Exception {
        assert context != null;

        if (command == null) {
            displayAvailableCommands(context);
        }
        else {
            displayCommandManual(context.getIo(), command, context.getVariables());
        }

        return Result.SUCCESS;
    }

    private void displayAvailableCommands(final CommandContext context) throws Exception {
        assert context != null;

        IO io = context.getIo();
        String about = applicationManager.getContext().getApplication().getBranding().getAboutMessage();

        if (about != null) {
            io.out.print(about);
            io.out.println();
        }

        io.out.println("Available commands:");

        GroupNode group = layoutManager.getLayout();

        displayGroupCommands(io, group);
    }

    private void displayGroupCommands(final IO io, final GroupNode group) throws Exception {
        assert io != null;
        int maxNameLen = 20; // FIXME: Figure this out dynamically

        // First display command/aliases nodes
        for (Node child : group.nodes()) {
            if (child instanceof CommandNode) {
                try {
                    CommandNode node = (CommandNode) child;
                    String name = StringUtils.rightPad(node.getName(), maxNameLen);

                    CommandContainer command = commandContainerFactory.create(node.getId());

                    // FIXME:
                    String desc = command.toString(); // command.getDescription();

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
                displayGroupCommands(io, node);
                io.out.println();
            }
        }
    }

    private void displayCommandManual(final IO io, final String path, final Variables variables) throws CommandException {
        assert path != null;
        assert variables != null;

        Command command = commandResolver.resolve(variables, path);

        CommandDocumenter documenter = command.getContainer().getDocumenter();
        CommandInfo info = command.getInfo();

        log.debug("Rendering help for command: {}", info.getName());

        documenter.renderManual(info, io.out);

    }
}
