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

package org.apache.geronimo.gshell.remote.jaas;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

/**
 * JAAS username+password callback handler.
 *
 * @version $Rev$ $Date$
 */
public class UsernamePasswordCallbackHandler
    implements CallbackHandler
{
    private final String username;

    private final char[] password;

    public UsernamePasswordCallbackHandler(final String username, final char[] password) {
        this.username = username;
        this.password = password;
    }

    public UsernamePasswordCallbackHandler(final String username, final String password) {
        this.username = username;

        if (password != null) {
            this.password = password.toCharArray();
        }
        else {
            this.password = null;
        }
    }

    public void handle(final Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for (Callback callback : callbacks) {
            if (callback instanceof NameCallback) {
                NameCallback nc = (NameCallback)callback;

                nc.setName(username);
            }
            else if (callback instanceof PasswordCallback) {
                PasswordCallback pc = (PasswordCallback)callback;

                if (password != null) {
                    pc.setPassword(password);
                }
                else {
                    pc.setPassword(null);
                }
            }
            else {
                throw new UnsupportedCallbackException(callback);
            }
        }
    }
}
