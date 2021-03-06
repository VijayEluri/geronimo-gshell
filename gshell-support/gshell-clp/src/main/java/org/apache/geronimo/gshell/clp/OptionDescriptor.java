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

package org.apache.geronimo.gshell.clp;

/**
 * Descriptor for {@link Option} annotations.
 *
 * @version $Rev$ $Date$
 */
public class OptionDescriptor
    extends Descriptor
{
    private final String name;
    
    private final String[] aliases;

    private final boolean argumentRequired;

    private final boolean requireOverride;

    public OptionDescriptor(final String id, final Option option, final boolean forceMultiValued) {
        super(id, option.description(), option.token(), option.required(), option.handler(), option.multiValued() || forceMultiValued);
    	
    	this.name = option.name();
    	this.aliases = option.aliases();
        this.argumentRequired = option.argumentRequired();
        this.requireOverride = option.requireOverride();
    }

    public String getName() {
    	return name;
    }
    
    public String[] getAliases() {
    	return aliases;
    }

    public boolean isArgumentRequired() {
        return argumentRequired;
    }

    public boolean isRequireOverride() {
        return requireOverride;
    }

    @Override
    public String toString() {
    	if (aliases != null && aliases.length > 0) {
            String str = "";

            for (String alias : aliases) {
                if (str.length() > 0) {
                    str += ", ";
                }
                str += alias;
            }
            
            return getName() + " (" + str + ")";
    	}
        
        return getName();
    }
}
