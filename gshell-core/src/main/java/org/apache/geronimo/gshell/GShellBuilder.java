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

package org.apache.geronimo.gshell;

import org.apache.geronimo.gshell.application.ApplicationConfiguration;
import org.apache.geronimo.gshell.application.ApplicationManager;
import org.apache.geronimo.gshell.artifact.ArtifactManager;
import org.apache.geronimo.gshell.artifact.monitor.ProgressSpinnerMonitor;
import org.apache.geronimo.gshell.io.IO;
import org.apache.geronimo.gshell.model.application.Application;
import org.apache.geronimo.gshell.model.settings.Settings;
import org.apache.geronimo.gshell.plexus.GShellPlexusContainer;
import org.apache.geronimo.gshell.settings.SettingsManager;
import org.apache.geronimo.gshell.shell.Environment;
import org.codehaus.plexus.ContainerConfiguration;
import org.codehaus.plexus.DefaultContainerConfiguration;
import org.codehaus.plexus.PlexusContainerException;
import org.codehaus.plexus.classworlds.ClassWorld;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Builds {@link GShell} instances.
 *
 * @version $Rev$ $Date$
 */
public class GShellBuilder
{
    private final Logger log = LoggerFactory.getLogger(getClass());

    private static final String DEFAULT_REALM_ID = "gshell";

    private static final String DEFAULT_CONTAINER_NAME = "gshell";

    private GShellPlexusContainer container;

    private ClassWorld classWorld;

    private Settings settings;

    private SettingsManager settingsManager;

    private ApplicationManager applicationManager;

    private ApplicationConfiguration applicationConfig = new ApplicationConfiguration();

    private ArtifactManager artifactManager;

    public GShellBuilder() {}

    private GShellPlexusContainer createContainer() throws PlexusContainerException {
        ContainerConfiguration config = new DefaultContainerConfiguration();
        
        config.setName(DEFAULT_CONTAINER_NAME);
        config.setClassWorld(getClassWorld());
        // config.addComponentDiscoverer(new PluginDiscoverer());
        // config.addComponentDiscoveryListener(new PluginCollector());

        return new GShellPlexusContainer(config);
    }

    public GShellPlexusContainer getContainer() throws PlexusContainerException {
        if (container == null) {
            container = createContainer();
        }
        return container;
    }

    public void setContainer(final GShellPlexusContainer container) {
        this.container = container;
    }

    private ClassWorld createClassWorld() {
        return new ClassWorld(DEFAULT_REALM_ID, Thread.currentThread().getContextClassLoader());
    }

    public ClassWorld getClassWorld() {
        if (classWorld == null) {
            classWorld = createClassWorld();
        }
        return classWorld;
    }

    public void setClassWorld(final ClassWorld classWorld) {
        this.classWorld = classWorld;
    }

    public IO getIo() {
        return applicationConfig.getIo();
    }

    public void setIo(final IO io) {
        applicationConfig.setIo(io);
    }

    public Environment getEnvironment() {
        return applicationConfig.getEnvironment();
    }

    public void setEnvironment(final Environment environment) {
        applicationConfig.setEnvironment(environment);
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(final Settings settings) {
        this.settings = settings;
    }

    private SettingsManager createSettingsManager() throws ComponentLookupException {
        return container.lookupComponent(SettingsManager.class);
    }

    public SettingsManager getSettingsManager() throws ComponentLookupException {
        if (settingsManager == null) {
            settingsManager = createSettingsManager();
        }
        return settingsManager;
    }

    public void setSettingsManager(final SettingsManager settingsManager) {
        this.settingsManager = settingsManager;
    }
    
    public Application getApplication() {
        return applicationConfig.getApplication();
    }

    public void setApplication(final Application application) {
        applicationConfig.setApplication(application);
    }

    private ApplicationManager createApplicationManager() throws ComponentLookupException {
        return container.lookupComponent(ApplicationManager.class);
    }

    public ApplicationManager getApplicationManager() throws ComponentLookupException {
        if (applicationManager == null) {
            applicationManager = createApplicationManager();
        }
        return applicationManager;
    }

    public void setApplicationManager(final ApplicationManager applicationManager) {
        this.applicationManager = applicationManager;
    }

    private ArtifactManager createArtifactManager() throws PlexusContainerException, ComponentLookupException {
        return getContainer().lookupComponent(ArtifactManager.class);
    }

    public ArtifactManager getArtifactManager() throws ComponentLookupException, PlexusContainerException {
        if (artifactManager == null) {
            artifactManager = createArtifactManager();
        }
        return artifactManager;
    }

    public void setArtifactManager(final ArtifactManager artifactManager) {
        this.artifactManager = artifactManager;
    }

    //
    // Building
    //

    public GShell build() throws Exception {
        log.debug("Building");

        // Initialize the container & components
        getContainer();

        // Configure download monitor
        getArtifactManager().setDownloadMonitor(new ProgressSpinnerMonitor(getIo()));

        // Configure settings (settings)
        if (settings != null) {
            getSettingsManager().setSettings(settings);
        }
        getSettingsManager().configure();

        // Configure application
        getApplicationManager().configure(applicationConfig);

        // TODO: Get application shell
        // return getApplicationManager().createShell();
        
        return null;
    }
}