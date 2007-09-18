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

import java.security.PublicKey;

import org.apache.mina.common.ByteBuffer;

/**
 * ???
 *
 * @version $Rev$ $Date$
 */
public class HandShakeMessage
    extends MessageSupport
{
    private PublicKey publicKey;

    public HandShakeMessage(final PublicKey publicKey) {
        super(MessageType.HANDSHAKE);

        setPublicKey(publicKey);
    }
    
    public HandShakeMessage() {
        this(null);
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(final PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public void readExternal(final ByteBuffer buff) throws Exception {
        assert buff != null;

        super.readExternal(buff);

        publicKey = (PublicKey) buff.getObject();
    }

    public void writeExternal(final ByteBuffer buff) throws Exception {
        assert buff != null;

        super.writeExternal(buff);

        buff.putObject(publicKey);
    }

    public void process(final MessageVisitor visitor) throws Exception {
        assert visitor != null;

        visitor.visitHandShake(this);
    }
}