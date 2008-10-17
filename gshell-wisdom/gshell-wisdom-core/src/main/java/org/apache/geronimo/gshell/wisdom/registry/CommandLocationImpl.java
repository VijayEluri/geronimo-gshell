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
package org.apache.geronimo.gshell.wisdom.registry;

import org.apache.geronimo.gshell.command.CommandLocation;

public class CommandLocationImpl
    implements CommandLocation
{
    private final String name;

    private final String path;

    public CommandLocationImpl(final String name) {
        assert name != null;

        int i = name.lastIndexOf("/");
        if (i != -1) {
            this.name = name.substring(i + 1, name.length());
            this.path = name.substring(0, i);
        }
        else {
            this.name = name;
            this.path = null;
        }
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getFullPath() {
        if (path != null) {
            return path + "/" + name;
        }
        return name;
    }

    public String toString() {
        return getFullPath();
    }
}
