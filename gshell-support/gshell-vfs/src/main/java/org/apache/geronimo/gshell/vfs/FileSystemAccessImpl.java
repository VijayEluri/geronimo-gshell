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

package org.apache.geronimo.gshell.vfs;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.provider.local.LocalFile;
import org.apache.geronimo.gshell.application.ApplicationManager;
import org.apache.geronimo.gshell.command.Variables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Field;

/**
 * {@link FileSystemAccess} component.
 *
 * @version $Rev$ $Date$
 */
public class FileSystemAccessImpl
    implements FileSystemAccess
{
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final ApplicationManager applicationManager;

    private final FileSystemManager fileSystemManager;

    public FileSystemAccessImpl(final ApplicationManager applicationManager, final FileSystemManager fileSystemManager) {
        assert applicationManager != null;
        this.applicationManager = applicationManager;
        assert fileSystemManager != null;
        this.fileSystemManager = fileSystemManager;
    }

    public FileSystemManager getManager() {
        return fileSystemManager;
    }

    public FileObject getCurrentDirectory(final Variables vars) throws FileSystemException {
        assert vars != null;

        FileObject cwd = null;

        Object var = vars.get(CWD);
        if (var instanceof String) {
            log.trace("Resolving CWD from string: {}", var);

            cwd = getManager().resolveFile((String)var);
        }
        else if (var instanceof FileObject) {
            cwd = (FileObject)var;
        }
        else if (var != null) {
            throw new RuntimeException("Invalid variable type for '" + CWD + "'; expected String or FileObject; found: " + var.getClass());
        }

        if (cwd == null) {
            log.trace("CWD not set, resolving from user.dir");

            // TODO: May need to ask the Application for this, as it might be different depending on the context (ie. remote user, etc)
            String userDir = "file://" + System.getProperty("user.dir");
            cwd = getManager().resolveFile(userDir);
        }

        return cwd;
    }

    public FileObject getCurrentDirectory() throws FileSystemException {
        log.trace("Resolving CWD from application variables");

        return getCurrentDirectory(applicationManager.getApplication().getVariables());
    }

    public void setCurrentDirectory(final Variables vars, final FileObject dir) throws FileSystemException {
        assert vars != null;
        assert dir != null;

        log.trace("Setting CWD: {}", dir);

        // Make sure that the given file object exists and is really a directory
        if (!dir.exists()) {
            throw new RuntimeException("Directory not found: " + dir.getName());
        }
        else if (!dir.getType().hasChildren()) {
            throw new RuntimeException("File can not contain children: " + dir.getName());
        }

        vars.parent().set(CWD, dir);
    }

    public void setCurrentDirectory(final FileObject dir) throws FileSystemException {
        assert dir != null;

        log.trace("Setting CWD to application variables");

        setCurrentDirectory(applicationManager.getApplication().getVariables(), dir);
    }

    public FileObject resolveFile(final FileObject baseFile, final String name) throws FileSystemException {
        return getManager().resolveFile(baseFile, name);
    }

    public FileObject resolveFile(final String name) throws FileSystemException {
        return getManager().resolveFile(getCurrentDirectory(), name);
    }

    public boolean isLocalFile(final FileObject file) {
        return file instanceof LocalFile;
    }

    public File getLocalFile(final FileObject file) throws FileSystemException {
        if (!isLocalFile(file)) {
            throw new FileSystemException("Unable to get local file from: " + file.getClass());
        }

        try {
            file.refresh();
            Field field = LocalFile.class.getDeclaredField("file");
            
            try {
                return (File)field.get(file);
            }
            catch (IllegalAccessException ignore) {
                // try again
                field.setAccessible(true);
                return (File)field.get(file);
            }
        }
        catch (Exception e) {
            throw new FileSystemException(e);
        }
    }
}