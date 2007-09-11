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

package org.apache.geronimo.gshell.command;

import org.apache.geronimo.gshell.clp.CommandLineProcessor;
import org.apache.geronimo.gshell.clp.Option;
import org.apache.geronimo.gshell.clp.Printer;
import org.apache.geronimo.gshell.common.Arguments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides support for {@link Command} implemenations.
 *
 * @version $Rev$ $Date$
 */
public abstract class CommandSupport
    implements Command
{
    protected Logger log = LoggerFactory.getLogger(getClass());

    protected CommandContext context;

    protected IO io;

    protected Variables variables;
    
    @Option(name="-h", aliases={"--help"}, description="Display this help message")
    private boolean displayHelp;

    public void init(final CommandContext context) {
        assert context != null;

        this.context = context;
        this.io = context.getIO();
        this.variables = context.getVariables();

        // Re-setup logging using our id
        String id = context.getCommandDescriptor().getId();
        log = LoggerFactory.getLogger(getClass().getName() + "." + id);
    }

    public Object execute(final Object... args) throws Exception {
        assert args != null;

        log.info("Executing w/args: [{}]", Arguments.asString(args));

        CommandLineProcessor clp = new CommandLineProcessor(this);
        clp.process(Arguments.toStringArray(args));

        // Handle --help/-h automatically for the command
        if (displayHelp) {
            //
            // TODO: Make a special PrinterHandler to abstrat this muck from having to process it by hand
            //
            
            displayHelp(clp);
            
            return SUCCESS;
        }

        assert context != null;
        assert io != null;
        assert variables != null;

        return doExecute();
    }

    protected abstract Object doExecute() throws Exception;

    protected void displayHelp(final CommandLineProcessor clp) {
        assert clp != null;

        //
        // TODO: Need to ask the LayoutManager what the real name is for our command's ID
        //

        io.out.println(context.getCommandDescriptor().getId());
        io.out.println(" -- ");
        io.out.println();

        Printer printer = new Printer(clp);
        printer.printUsage(io.out);
        io.out.println();
    }
}
