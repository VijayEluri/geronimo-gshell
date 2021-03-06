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

package org.apache.geronimo.gshell.vfs.config;

import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.operations.FileOperationProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Configures a {@link FileOperationProvider}.
 *
 * @version $Rev$ $Date$
 */
public class FileOperationProviderConfigurer
    extends FileSystemManagerConfigurerSupport
{
    private FileOperationProvider provider;

    private List<String> schemes;

    public void setProvider(final FileOperationProvider provider) {
        this.provider = provider;
    }

    public void setSchemes(final List<String> schemes) {
        this.schemes = schemes;
    }
    
    public void setScheme(final String scheme) {
        schemes = new ArrayList<String>(1);
        schemes.add(scheme);
    }

    // @PostConstruct
    public void init() throws FileSystemException {
        // TODO: Handle nulls & exceptions

        log.debug("Adding file operation provider: {} -> {}", schemes, provider);
        ConfigurableFileSystemManager fsm = getFileSystemManager();
        for (String scheme: schemes) {
            if (fsm.hasProvider(scheme)) {
                fsm.addOperationProvider(scheme, provider);
            }
        }
    }
}