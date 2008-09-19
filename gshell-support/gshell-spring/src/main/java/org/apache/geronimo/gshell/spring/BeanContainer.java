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


package org.apache.geronimo.gshell.spring;

import org.codehaus.plexus.classworlds.realm.DuplicateRealmException;

import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * An abstraction of a container of beans.
 *
 * @version $Rev$ $Date$
 */
public interface BeanContainer
{
    BeanContainer getParent();

    void start();

    void stop();

    void close();
    
    <T> T getBean(Class<T> type);

    <T> T getBean(String name, Class<T> requiredType);

    <T> Map<String,T> getBeans(Class<T> type);

    String[] getBeanNames();

    String[] getBeanNames(Class type);

    BeanContainer createChild(String id, List<URL> classPath);

    BeanContainer createChild(String id);
}
