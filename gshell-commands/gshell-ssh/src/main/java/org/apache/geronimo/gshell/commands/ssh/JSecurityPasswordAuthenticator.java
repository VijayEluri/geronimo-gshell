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

package org.apache.geronimo.gshell.commands.ssh;

import com.google.code.sshd.server.PasswordAuthenticator;
import org.jsecurity.SecurityUtils;
import org.jsecurity.authc.AuthenticationException;
import org.jsecurity.authc.UsernamePasswordToken;
import org.jsecurity.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <a href="http://jsecurity.org">JSecurity</a> {@link PasswordAuthenticator}.
 *
 * @version $Rev$ $Date$
 */
public class JSecurityPasswordAuthenticator
    implements PasswordAuthenticator
{
    private final Logger log = LoggerFactory.getLogger(getClass());

    public Object authenticate(final String username, final String password) {
        assert username != null;
        assert password != null;
        
        Subject currentUser = SecurityUtils.getSubject();

        if (!currentUser.isAuthenticated()) {
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);

            try {
                currentUser.login(token);
                log.info("User [" + currentUser.getPrincipal() + "] logged in successfully");
            }
            catch (AuthenticationException e) {
                log.error("Authentication failed: " + e, e);
                return null;
            }
        }

        return currentUser.getPrincipal();
    }
}