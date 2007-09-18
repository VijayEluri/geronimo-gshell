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

package org.apache.geronimo.gshell.remote.message;

import org.apache.geronimo.gshell.command.CommandExecutor;
import org.apache.mina.common.ByteBuffer;

/**
 * Execute a command.  This supports all flavors of the {@link CommandExecutor} execution methods.
 *
 * @version $Rev$ $Date$
 */
public class ExecuteMessage
    extends MessageSupport
{
    private Flavor flavor;
    
    private String path;

    private Object[] args;

    private ExecuteMessage(final Flavor flavor, final String path, final Object[] args) {
        super(MessageType.EXECUTE);

        this.flavor = flavor;
        this.path = path;
        this.args = args;
    }

    public ExecuteMessage(final String commandLine) {
        this(Flavor.STRING, null, new Object[] { commandLine });
    }

    public ExecuteMessage(final Object[] args) {
        this(Flavor.OBJECTS, null, args);
    }

    public ExecuteMessage(final String path, final Object[] args) {
        this(Flavor.STRING_OBJECTS, path, args);
    }

    public ExecuteMessage() {
        this(null, null, null);
    }

    public Object execute(final CommandExecutor executor) throws Exception {
        assert executor != null;

        return flavor.execute(this, executor);
    }

    //
    // TODO: Optimize this
    //
    
    public void readExternal(final ByteBuffer in) throws Exception {
        assert in != null;

        super.readExternal(in);

        this.flavor = in.getEnum(Flavor.class);

        this.path = readString(in);

        this.args = (Object[]) in.getObject();
    }

    public void writeExternal(final ByteBuffer out) throws Exception {
        assert out != null;

        super.writeExternal(out);

        out.putEnum(flavor);

        writeString(out, path);
        
        out.putObject(args);
    }

    public void process(final MessageVisitor visitor) throws Exception {
        assert visitor != null;

        visitor.visitExecute(this);
    }

    //
    // Flavor
    //
    
    private enum Flavor
    {
        STRING,         // execute(String)
        OBJECTS,        // execute(Object[])
        STRING_OBJECTS  // execute(String, Object[])
        ;

        public Object execute(final ExecuteMessage msg, final CommandExecutor executor) throws Exception {
            assert msg != null;
            assert executor != null;

            switch (this) {
                case STRING:
                    return executor.execute((String)msg.args[0]);
                
                case OBJECTS:
                    return executor.execute(msg.args);

                case STRING_OBJECTS:
                    return executor.execute(msg.path, msg.args);
            }

            // This should never happen
            throw new Error();
        }
    }
}