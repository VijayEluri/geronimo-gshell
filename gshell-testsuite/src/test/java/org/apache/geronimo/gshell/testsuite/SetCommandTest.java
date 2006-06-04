/*
 * Copyright 2006 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.geronimo.gshell.testsuite;

import junit.framework.TestCase;
import org.apache.geronimo.gshell.Shell;
import org.apache.geronimo.gshell.console.IO;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

/**
 * ???
 *
 * @version $Id$
 */
public class SetCommandTest
    extends TestCase
{
    public void testSimple() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IO io = new IO(System.in, out);

        Shell shell = new Shell(io);
        shell.execute("set", "a=b");

        assertEquals("b", shell.getVariables().get("a"));
    }
}