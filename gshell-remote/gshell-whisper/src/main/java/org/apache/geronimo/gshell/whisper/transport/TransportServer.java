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

package org.apache.geronimo.gshell.whisper.transport;

import org.apache.mina.common.IoAcceptor;
import org.apache.mina.common.IoHandler;
import org.apache.mina.common.ThreadModel;

import java.io.Closeable;
import java.net.URI;

/**
 * Provides the server-side protocol interface.
 *
 * @version $Rev$ $Date$
 */
public interface TransportServer<T extends IoAcceptor>
    extends Closeable
{
    URI getLocation();

    T getAcceptor();

    void close();

    //
    // Configuration
    //

    void setConfiguration(Configuration config);

    Configuration getConfiguration();

    interface Configuration
    {
        IoHandler getHandler();

        void setHandler(IoHandler hanlder);

        ThreadModel getThreadModel();

        void setThreadModel(ThreadModel threadModel);
    }
}