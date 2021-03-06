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

package org.apache.geronimo.gshell.commands.builtin;

import org.apache.geronimo.gshell.clp.Argument;
import org.apache.geronimo.gshell.clp.Option;
import org.apache.geronimo.gshell.command.CommandAction;
import org.apache.geronimo.gshell.command.CommandContext;
import org.apache.geronimo.gshell.command.Variables;
import org.apache.geronimo.gshell.i18n.MessageSource;
import org.apache.geronimo.gshell.io.IO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Properties;

/**
 * Set a variable or property.
 *
 * @version $Rev$ $Date$
 */
public class SetAction
    implements CommandAction
{
    private final Logger log = LoggerFactory.getLogger(getClass());

    private enum Mode
    {
        VARIABLE,
        PROPERTY
    }

    @Option(name="-m", aliases={"--mode"})
    private Mode mode = Mode.VARIABLE;

    @Option(name="-v", aliases={"--verbose"})
    private boolean verbose;

    @Argument(index=0)
    private String name;

    @Argument(index=1)
    private String value;

    public Object execute(final CommandContext context) throws Exception {
        assert context != null;
        IO io = context.getIo();
        MessageSource messages = context.getCommand().getMessages();

        if (name == null) {
            return displayList(context);
        }
        else if (value == null) {
            io.error("Missing required argument: {}", messages.getMessage("command.argument.value.token"));
            return Result.FAILURE;
        }

        switch (mode) {
            case PROPERTY:
                log.debug("Setting system property: {}={}", name, value);
                System.setProperty(name, value);
                break;

            case VARIABLE:
                Variables vars = context.getVariables();
                log.info("Setting variable: {}={}", name, value);
                vars.parent().set(name, value);
                break;
        }

        return Result.SUCCESS;
    }

    private Object displayList(final CommandContext context) throws Exception {
        assert context != null;
        IO io = context.getIo();

        // NOTE: Using io.outputStream to display values to avoid any ANSI encoding or other translation.
        
        switch (mode) {
            case PROPERTY: {
                Properties props = System.getProperties();

                for (Object o : props.keySet()) {
                    String name = (String) o;
                    String value = props.getProperty(name);

                    io.outputStream.print(name);
                    io.outputStream.print("='");
                    io.outputStream.print(value);
                    io.outputStream.print("'");

                    // Value is always a string, so no need to add muck here for --verbose

                    io.outputStream.println();
                }
                break;
            }

            case VARIABLE: {
                Variables variables = context.getVariables();
                Iterator<String> iter = variables.names();

                while (iter.hasNext()) {
                    String name = iter.next();

                    // HACK: Hide some internal muck for now
                    if (name.startsWith("gshell.internal")) {
                        continue;
                    }

                    Object value = variables.get(name);

                    io.outputStream.print(name);
                    io.outputStream.print("='");
                    io.outputStream.print(value);
                    io.outputStream.flush();
                    io.outputStream.print("'");

                    // When --verbose include the class details of the value
                    if (verbose && value != null) {
                        io.outputStream.print(" (");
                        io.outputStream.print(value.getClass());
                        io.outputStream.print(")");
                    }

                    io.outputStream.println();
                }
                break;
            }
        }

        // Manually flush the stream, normally framework only flushes io.out
        io.outputStream.flush();

        return Result.SUCCESS;
    }
}
