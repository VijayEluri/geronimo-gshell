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

package org.apache.geronimo.gshell.vfs.provider.meta;

import org.apache.commons.vfs.FileContent;
import org.apache.commons.vfs.FileContentInfo;
import org.apache.commons.vfs.FileContentInfoFactory;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.impl.DefaultFileContentInfo;
import org.apache.geronimo.gshell.vfs.provider.meta.data.MetaData;

/**
 * Meta file content info factory.
 *
 * @version $Rev$ $Date$
 */
public class MetaFileContentInfoFactory
    implements FileContentInfoFactory
{
    public FileContentInfo create(final FileContent content) throws FileSystemException {
        assert content != null;

        MetaFileObject file = (MetaFileObject)content.getFile();
        MetaData data = file.getData();

        return new DefaultFileContentInfo(data.getContentType(), data.getContentEncoding());
    }
}