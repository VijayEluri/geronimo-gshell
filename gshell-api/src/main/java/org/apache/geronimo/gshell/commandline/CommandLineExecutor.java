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

package org.apache.geronimo.gshell.commandline;

import org.apache.geronimo.gshell.shell.ShellContext;

/**
 * Provides the ability to execute commands.
 *
 * @version $Rev$ $Date$
 */
public interface CommandLineExecutor
{
    /**
     * Execute a command-line, parsing out valid recognized syntax.
     *
     * @param context   The executing shell's context.
     * @param line      Raw command-line to parse and execute.
     * @return          Command execution result.
     *
     * @throws Exception    Command-line execution failed.
     */
    Object execute(ShellContext context, String line) throws Exception;

    /**
     * Execute command name/path with the given arguments.
     *
     * @param context   The executing shell's context.
     * @param command   Name of the command/path to execute.
     * @param args      Command arguments.
     * @return          Command execution result.
     *
     * @throws Exception    Command-line execution failed.
     */
    Object execute(ShellContext context, String command, Object[] args) throws Exception;

    /**
     * Execute a pre-processed command-line.
     *
     * @param context   The executing shell's context.
     * @param args      Command arguments, first argument is expected to be the command/path to execute.
     * @return          Command execution result.
     *
     * @throws Exception    Command-line execution failed.
     */
    Object execute(ShellContext context, Object... args) throws Exception;

}