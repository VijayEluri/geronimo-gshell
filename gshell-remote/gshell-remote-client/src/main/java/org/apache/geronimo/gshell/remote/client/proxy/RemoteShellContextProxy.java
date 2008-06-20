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

package org.apache.geronimo.gshell.remote.client.proxy;

import org.apache.geronimo.gshell.io.IO;
import org.apache.geronimo.gshell.command.Variables;
import org.apache.geronimo.gshell.remote.client.RshClient;
import org.apache.geronimo.gshell.shell.ShellContext;

/**
 * ???
 *
 * @version $Rev$ $Date$
 */
public class RemoteShellContextProxy
    implements ShellContext
{
    private final RshClient client;

    public RemoteShellContextProxy(final RshClient client) {
        assert client != null;

        this.client = client;
    }

    public IO getIo() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Variables getVariables() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}