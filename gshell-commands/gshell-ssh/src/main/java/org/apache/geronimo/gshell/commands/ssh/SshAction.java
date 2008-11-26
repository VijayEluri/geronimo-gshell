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

import com.google.code.sshd.ClientChannel;
import com.google.code.sshd.ClientSession;
import com.google.code.sshd.SshClient;
import org.apache.geronimo.gshell.clp.Argument;
import org.apache.geronimo.gshell.clp.Option;
import org.apache.geronimo.gshell.command.CommandAction;
import org.apache.geronimo.gshell.command.CommandContext;
import org.apache.geronimo.gshell.i18n.MessageSource;
import org.apache.geronimo.gshell.io.IO;
import org.apache.geronimo.gshell.io.PromptReader;
import org.apache.geronimo.gshell.spring.BeanContainer;
import org.apache.geronimo.gshell.spring.BeanContainerAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Connect to a SSH server.
 *
 * @version $Rev$ $Date$
 */
public class SshAction
    implements CommandAction, BeanContainerAware
{
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Option(name="-l", aliases={"--username"})
    private String username;

    @Option(name="-P", aliases={"--password"})
    private String password;

    @Argument(required=true)
    private String hostname;

    @Option(name="-p", aliases={"--port"})
    private int port = 22;

    private BeanContainer container;

    public void setBeanContainer(final BeanContainer container) {
        assert container != null;
        this.container = container;
    }

    /**
     * Helper to validate that prompted username or password is not null or empty.
     */
    private class UsernamePasswordValidator
        implements PromptReader.Validator
    {
        private String type;

        private int count = 0;

        private int max = 3;

        public UsernamePasswordValidator(final String type) {
            assert type != null;

            this.type = type;
        }

        public boolean isValid(final String value) {
            count++;

            if (value != null && value.trim().length() > 0) {
                return true;
            }

            if (count >= max) {
                throw new RuntimeException("Too many attempts; failed to prompt user for " + type + " after " + max + " tries");
            }

            return false;
        }
    }

    public Object execute(final CommandContext context) throws Exception {
        assert context != null;
        IO io = context.getIo();
        MessageSource messages = context.getCommand().getMessages();

        //
        // TODO: Parse hostname for <username>@<hostname>
        //
        
        io.info(messages.format("info.connecting", hostname, port));

        // If the username/password was not configured via cli, then prompt the user for the values
        if (username == null || password == null) {
            PromptReader prompter = new PromptReader(io);
            String text;

            log.debug("Prompting user for credentials");

            if (username == null) {
                text = messages.getMessage("prompt.username");
                username = prompter.readLine(text + ": ", new UsernamePasswordValidator(text));
            }

            if (password == null) {
                text = messages.getMessage("prompt.password");
                password = prompter.readPassword(text + ": ", new UsernamePasswordValidator(text));
            }
        }

        // Create the client from prototype
        SshClient client = container.getBean(SshClient.class);
        log.debug("Created client: {}", client);

        ClientSession session = client.connect(hostname, port);
        io.info(messages.getMessage("info.connected"));

        session.authPassword(username, password);

        ClientChannel channel = session.createChannel("shell");
        channel.setIn(io.inputStream);
        channel.setOut(io.outputStream);
        channel.setErr(io.errorStream);
        channel.open();
        channel.waitFor(ClientChannel.CLOSED, 0);

        io.verbose(messages.getMessage("verbose.disconnected"));

        return Result.SUCCESS;
    }
}