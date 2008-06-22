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

import java.io.PrintWriter;

/**
 * Provides access to a commands documentation.
 *
 * @version $Rev$ $Date$
 */
public interface CommandDocumenter
{
    /**
     * Get the name of the command as it has been configured in the layout.
     *
     * @return The configured name of the command.
     */
    String getName(CommandInfo info);

    /**
     * Get the terse description of the command.
     *
     * @return The configured description of hte command.
     */
    String getDescription(CommandInfo info);

    /**
     * Render the command-line usage, as expected from <tt>--help</tt>.
     *
     * @param out   Write the usage to this writer.
     */
    void renderUsage(CommandInfo info, PrintWriter out);

    /**
     * Render the full command manual.
     *
     * @param out   Write the manual to this writer.
     */
    void renderManual(CommandInfo info, PrintWriter out);

    //
    // TODO: Consider making this a factory, returning a CommandDocumentation for a CommandInfo
    //
}