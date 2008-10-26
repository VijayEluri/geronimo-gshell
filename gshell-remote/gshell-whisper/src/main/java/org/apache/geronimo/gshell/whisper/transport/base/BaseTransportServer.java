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

package org.apache.geronimo.gshell.whisper.transport.base;

import org.apache.geronimo.gshell.whisper.transport.TransportServer;
import org.apache.mina.common.IoAcceptor;
import org.apache.mina.common.IoHandler;

import java.net.SocketAddress;
import java.net.URI;

/**
 * Support for {@link TransportServer} implementations.
 *
 * @version $Rev$ $Date$
 */
public abstract class BaseTransportServer<T extends IoAcceptor>
    extends BaseService
    implements TransportServer
{
    protected URI location;

    protected SocketAddress address;

    protected T acceptor;

    protected BaseTransportServer(final AddressFactory addressFactory) {
        super(addressFactory);
    }

    //
    // Acceptor
    //
    
    protected abstract T createAcceptor() throws Exception;

    synchronized void bind(final URI location) throws Exception {
        assert location != null;

        this.location = location;
        this.address = addressFactory.create(location);

        bind();
    }

    synchronized void bind() throws Exception {
        acceptor = createAcceptor();
        log.debug("Acceptor: {}", acceptor);

        configure(acceptor);

        IoHandler handler = getHandler();
        log.debug("Handler: {}", handler);

        log.info("Binding to: {}", address);
        acceptor.bind(address, handler);
        
        log.info("Listening on: {}", address);
    }

    public URI getLocation() {
        return location;
    }

    public T getAcceptor() {
        return acceptor;
    }

    public synchronized void close() {
        try {
            acceptor.unbind(address);
        }
        finally {
            super.close();
        }
    }
}