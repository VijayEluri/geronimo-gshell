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

package org.apache.geronimo.gshell.whisper.request;

import org.apache.geronimo.gshell.whisper.message.Message;
import org.apache.geronimo.gshell.yarn.Yarn;

//
// NOTE: Snatched and massaged from Apache Mina
//

/**
 * Represents a response to a request message.
 *
 * @version $Rev$ $Date$
 */
public class ResponseHandle
{
    private final RequestHandle request;

    private final Type type;

    private final Message message;

    public ResponseHandle(final RequestHandle request, final Message message, final Type type) {
        this.request = request;
        this.type = type;
        this.message = message;
    }

    public int hashCode() {
        return getRequest().getId().hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        else if (obj == null) {
            return false;
        }
        else if (!(obj instanceof ResponseHandle)) {
            return false;
        }

        ResponseHandle resp = (ResponseHandle) obj;

        return getRequest().equals(resp.getRequest()) && getType().equals(resp.getType());
    }

    public String toString() {
        return Yarn.render(this);
    }

    public RequestHandle getRequest() {
        return request;
    }

    public Type getType() {
        return type;
    }

    public Message getMessage() {
        return message;
    }

    public Message.ID getId() {
        return getMessage().getId();
    }

    //
    // Response Type
    //

    public static enum Type
    {
        WHOLE,
        PARTIAL,
        PARTIAL_LAST
    }
}
