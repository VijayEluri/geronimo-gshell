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

package org.apache.geronimo.gshell.wisdom.command;

import org.apache.geronimo.gshell.clp.Argument;
import org.apache.geronimo.gshell.command.CommandAction;
import org.apache.geronimo.gshell.command.CommandContext;
import org.apache.geronimo.gshell.commandline.CommandLineExecutor;
import org.apache.geronimo.gshell.i18n.MessageSource;
import org.apache.geronimo.gshell.i18n.ResourceBundleMessageSource;
import org.apache.geronimo.gshell.shell.ShellContext;
import org.apache.geronimo.gshell.shell.ShellContextHolder;
import org.apache.geronimo.gshell.wisdom.registry.CommandLocationImpl;

import java.util.Iterator;
import java.util.List;

/**
 * Alias {@link org.apache.geronimo.gshell.command.Command} component.
 *
 * @version $Rev$ $Date$
 */
public class AliasCommand
    extends CommandSupport
{
    private final CommandLineExecutor executor;

    private String name;

    private String alias;

    public AliasCommand(final CommandLineExecutor executor) {
        assert executor != null;

        this.executor = executor;

        setAction(new AliasCommandAction());
        setDocumenter(new AliasCommandDocumenter());
        setMessages(new AliasCommandMessageSource());
    }
    
    public String getName() {
        if (name == null) {
            throw new IllegalStateException("Missing property: name");
        }
        return name;
    }

    public void setName(final String name) {
        this.name = name;
        if (name != null) {
            setLocation(new CommandLocationImpl(name));
        }
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(final String alias) {
        if (alias == null) {
            throw new IllegalStateException("Missing property: alias");
        }

        this.alias = alias;
    }

    @Override
    protected void prepareAction(final ShellContext context, final Object[] args) {
        // HACK: Reset state for proper appendArgs muck
        setAction(new AliasCommandAction());
        super.prepareAction(context, args);
    }

    /**
     * Action to handle invocation of the alias target + additional arguments.
     */
    private class AliasCommandAction
        implements CommandAction
    {    
        @Argument
        private List<String> appendArgs = null;

        public Object execute(final CommandContext context) throws Exception {
            assert context != null;

            StringBuilder buff = new StringBuilder();
            buff.append(getAlias());

            // If we have args to append, then do it
            if (appendArgs != null && !appendArgs.isEmpty()) {
                buff.append(" ");

                // Append args quoted as they have already been processed by the parser
                Iterator iter = appendArgs.iterator();
                while (iter.hasNext()) {
                    //
                    // HACK: Using double quote instead of single quote for now as the parser's handling of single quote is broken
                    //

                    buff.append('"').append(iter.next()).append('"');
                    if (iter.hasNext()) {
                        buff.append(" ");
                    }
                }
            }

            log.debug("Executing alias: {}", buff);

            Object result = executor.execute(ShellContextHolder.get(), buff.toString());

            log.debug("Alias result: {}", result);

            return result;
        }
    }

    /**
     * Alias command documenter.
     */
    private class AliasCommandDocumenter
        extends MessageSourceCommandDocumenter
    {
        @Override
        public String getDescription() {
            return getMessages().format(COMMAND_DESCRIPTION, getAlias());
        }
    }

    /**
     * Alias message source.
     */
    private class AliasCommandMessageSource
        implements MessageSource
    {
        private final MessageSource messages = new ResourceBundleMessageSource(new Class[] { AliasCommand.class, HelpSupport.class });

        public String getMessage(final String code) {
            return messages.getMessage(code);
        }

        public String format(final String code, final Object... args) {
            return messages.format(code, args);
        }
    }
}
