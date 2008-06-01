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

package org.apache.geronimo.gshell.application;

import junit.framework.TestCase;
import org.apache.geronimo.gshell.command.Variables;

import java.util.Iterator;

/**
 * Unit tests for the {@link DefaultVariables} class.
 *
 * @version $Rev$ $Date$
 */
public class DefaultVariablesTest
    extends TestCase
{
    public void testSet() throws Exception {
        DefaultVariables vars = new DefaultVariables();
        String name = "a";
        Object value = new Object();

        assertFalse(vars.contains(name));
        vars.set(name, value);
        assertTrue(vars.contains(name));

        Object obj = vars.get(name);
        assertEquals(value, obj);

        String str = vars.names().next();
        assertEquals(name, str);
    }

    public void testSetAsImmutable() throws Exception {
        DefaultVariables vars = new DefaultVariables();
        String name = "a";
        Object value = new Object();

        assertTrue(vars.isMutable(name));
        vars.set(name, value, false);
        assertFalse(vars.isMutable(name));

        try {
            vars.set(name, value);
            fail("Set an immutable variable");
        }
        catch (Variables.ImmutableVariableException expected) {
            // ignore
        }
    }

    public void testSetAsImmutableInParent() throws Exception {
        Variables parent = new DefaultVariables();
        DefaultVariables vars = new DefaultVariables(parent);
        String name = "a";
        Object value = new Object();

        parent.set(name, value, false);
        assertFalse(parent.isMutable(name));
        assertFalse(vars.isMutable(name));

        try {
            vars.set(name, value);
            fail("Set an immutable variable");
        }
        catch (Variables.ImmutableVariableException expected) {
            // ignore
        }
    }

    public void testSetParentFromChild() throws Exception {
        Variables parent = new DefaultVariables();
        DefaultVariables vars = new DefaultVariables(parent);
        String name = "a";
        Object value = new Object();

        // Make sure we can add to parent's scope from child
        vars.parent().set(name, value);
        assertEquals(value, parent.get(name));

        // Make sure the iter sees it
        assertTrue(vars.names().hasNext());
    }

    public void testGet() throws Exception {
        DefaultVariables vars = new DefaultVariables();
        String name = "a";
        Object value = new Object();

        Object obj1 = vars.get(name);
        assertNull(obj1);

        vars.set(name, value);
        Object obj2 = vars.get(name);
        assertSame(value, obj2);
    }

    public void testGetUsingDefault() throws Exception {
        DefaultVariables vars = new DefaultVariables();
        String name = "a";
        Object value = new Object();

        Object obj1 = vars.get(name);
        assertNull(obj1);

        Object obj2 = vars.get(name, value);
        assertSame(value, obj2);
    }

    public void testGetCloaked() throws Exception {
        Variables parent = new DefaultVariables();
        DefaultVariables vars = new DefaultVariables(parent);
        String name = "a";
        Object value = new Object();

        parent.set(name, value);
        Object obj1 = vars.get(name);
        assertEquals(value, obj1);

        Object value2 = new Object();
        vars.set(name, value2);

        Object obj2 = vars.get(name);
        assertSame(value2, obj2);
        assertNotSame(value, obj2);
    }

    public void testUnsetAsImmutable() throws Exception {
        DefaultVariables vars = new DefaultVariables();
        String name = "a";
        Object value = new Object();

        assertTrue(vars.isMutable(name));
        vars.set(name, value, false);
        assertFalse(vars.isMutable(name));

        try {
            vars.unset(name);
            fail("Unset an immutable variable");
        }
        catch (Variables.ImmutableVariableException expected) {
            // ignore
        }
    }

    public void testUnsetAsImmutableInParent() throws Exception {
        Variables parent = new DefaultVariables();
        DefaultVariables vars = new DefaultVariables(parent);
        String name = "a";
        Object value = new Object();

        parent.set(name, value, false);
        assertFalse(parent.isMutable(name));
        assertFalse(vars.isMutable(name));

        try {
            vars.unset(name);
            fail("Unset an immutable variable");
        }
        catch (Variables.ImmutableVariableException expected) {
            // ignore
        }
    }

    public void testCloaking() throws Exception {
        Variables parent = new DefaultVariables();
        DefaultVariables vars = new DefaultVariables(parent);
        String name = "a";
        Object value = new Object();

        parent.set(name, value);
        assertFalse(parent.isCloaked(name));
        assertFalse(vars.isCloaked(name));

        vars.set(name, new Object());
        assertTrue(vars.isCloaked(name));
    }

    public void testParent() throws Exception {
        Variables parent = new DefaultVariables();
        assertNull(parent.parent());

        DefaultVariables vars = new DefaultVariables(parent);
        assertNotNull(vars.parent());

        assertEquals(parent, vars.parent());
    }

    public void testNames() throws Exception {
        DefaultVariables vars = new DefaultVariables();
        Iterator<String> iter = vars.names();
        assertNotNull(iter);
        assertFalse(iter.hasNext());
    }

    public void testNamesImmutable() throws Exception {
        DefaultVariables vars = new DefaultVariables();
        vars.set("a", "b");

        Iterator<String> iter = vars.names();
        iter.next();

        try {
            iter.remove();
        }
        catch (UnsupportedOperationException expected) {
            // ignore
        }
    }
}
