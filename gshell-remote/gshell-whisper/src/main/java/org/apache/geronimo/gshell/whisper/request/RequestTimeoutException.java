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

/**
 * Thrown to indicate that a request has been timed out.
 *
 * @version $Rev$ $Date$
 */
public class RequestTimeoutException
    extends RequestException
{
    private static final long serialVersionUID = 1;

    private final Message.ID id;

    public RequestTimeoutException(final Message.ID id) {
        super("Request timed out: " + id);

        this.id = id;
    }

    public Message.ID getId() {
        return id;
    }
}