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

import jline.ConsoleReader;
import org.apache.geronimo.gshell.ansi.ANSI;
import org.apache.geronimo.gshell.command.annotation.CommandComponent;

import java.io.PrintWriter;

/**
 * Clear the terminal screen.
 *
 * @version $Rev$ $Date$
 */
@CommandComponent(id="gshell-builtins:clear", description="Clear the terminal screen")
public class ClearCommand
    extends CommandSupport
{
    protected Object doExecute() throws Exception {
        ConsoleReader reader = new ConsoleReader(io.inputStream, new PrintWriter(io.outputStream, true), /*bindings*/ null, io.getTerminal());
        
        if (!ANSI.isEnabled()) {
        	io.out.println("ANSI is not enabled.  The clear command is not functional");
        }
        else {
        	reader.clearScreen();
        	return SUCCESS;
        }
        
        return SUCCESS;
    }
}
