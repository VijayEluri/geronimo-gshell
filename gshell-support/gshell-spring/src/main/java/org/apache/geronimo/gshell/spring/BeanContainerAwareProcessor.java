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

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * Spring bean post-processor to handle injection of beans which are {@link BeanContainerAware}.
 *
 * @version $Rev$ $Date$
 */
public class BeanContainerAwareProcessor
    implements BeanPostProcessor
{
    private final BeanContainer container;

    public BeanContainerAwareProcessor(final BeanContainer container) {
        assert container != null;

        this.container = container;
    }

    public Object postProcessBeforeInitialization(final Object bean, final String beanName) throws BeansException {
        assert bean != null;

        if (bean instanceof BeanContainerAware) {
            ((BeanContainerAware)bean).setBeanContainer(container);
        }
        
        return bean;
    }

    public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {
        return bean;
    }
}