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

package org.apache.geronimo.gshell.remote.client.proxy;

import java.util.List;

import jline.Completor;
import org.apache.geronimo.gshell.remote.client.RshClient;

/**
 * ???
 *
 * @version $Rev: 580765 $ $Date: 2007-09-30 20:52:39 +0200 (Sun, 30 Sep 2007) $
 */
public class RemoteCompleterProxy
    implements Completor
{
    private final RshClient client;

    public RemoteCompleterProxy(final RshClient client) {
        assert client != null;

        this.client = client;
    }

    public int complete(final String buffer, final int cursor, final List candidates) {
        try {
            //noinspection unchecked
            return client.complete(buffer, cursor, candidates);
        }
        catch (Exception e) {
            return -1;
        }
    }
}
