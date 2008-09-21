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

package org.apache.geronimo.gshell.remote.client.auth;

import org.apache.geronimo.gshell.yarn.Yarn;

import java.io.Serializable;
import java.security.Principal;

/**
 * ???
 *
 * @version $Rev$ $Date$
 */
public class ClientPrincipal
    implements Principal, Serializable
{
    private static final long serialVersionUID = 1;

    private final String name;

    private final Object identity;

    public ClientPrincipal(final String name, final Object identity) {
        assert name != null;
        assert identity != null;

        this.name = name;
        this.identity = identity;
    }

    public String getName() {
        return name;
    }

    public Object getIdentityToken() {
        return identity;
    }

    public String toString() {
        return Yarn.render(this);
    }
}
