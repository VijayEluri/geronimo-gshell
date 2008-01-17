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

package org.apache.geronimo.gshell.commands.bsf;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.List;

import jline.Terminal;
import org.apache.bsf.BSFEngine;
import org.apache.bsf.BSFManager;
import org.apache.geronimo.gshell.clp.Argument;
import org.apache.geronimo.gshell.clp.Option;
import org.apache.geronimo.gshell.command.CommandException;
import org.apache.geronimo.gshell.command.CommandSupport;
import org.apache.geronimo.gshell.command.annotation.CommandComponent;
import org.apache.geronimo.gshell.command.annotation.Requirement;
import org.apache.geronimo.gshell.console.Console;
import org.apache.geronimo.gshell.console.JLineConsole;

/**
 * Provides generic scripting language integration via <a href="http://http://jakarta.apache.org/bsf">BSF</a>.
 *
 * @version $Rev$ $Date$
 */
@CommandComponent(id="gshell-bsf:script", description="Provides generic scripting language execution support")
public class ScriptCommand
    extends CommandSupport
{
    @Requirement
    private Terminal terminal;

    private String language;

    @Option(name="-l", aliases={"--language"}, description="Specify the scripting language")
    private void setLanguage(final String language) {
        assert language != null;
        
        if (!BSFManager.isLanguageRegistered(language)) {
            throw new RuntimeException("Language is not registered: " + language);
        }

        this.language = language;
    }

    @Option(name="-i", aliases={"--interactive"}, description="Run interactive mode")
    private boolean interactive;

    @Option(name="-e", aliases={"--expression"}, description="Evaluate the given expression")
    private String expression;
    
    @Argument(description="A file or URL to execute")
    private List<String> args;

    protected Object doExecute() throws Exception {
        //
        // TODO: When given a file/url, try to figure out language from ext if language not given
        //       https://issues.apache.org/jira/browse/GSHELL-49
        //
        
        String path = null;
        String filename = null;

    	if (args != null) {
    		path = args.get(0); // Only allowing one script for now
    		filename = ((path.lastIndexOf('/')) == -1) ? path : path.substring(path.lastIndexOf('/') + 1) ; // Just the filename, please   		
    	}
    	
    	if (language == null) {
    		if (filename == null) {
    			throw new CommandException("If a file/URL is not provided, language must" +
    					" be specified using the -l (--language) option.");
    		}
    		language = BSFManager.getLangFromFilename(filename);   		
    	}

    	BSFManager manager = new BSFManager();
        final BSFEngine engine = manager.loadScriptingEngine(language);

        if (this.expression != null) {
            log.info("Evaluating expression: " + expression);

            Object obj = engine.eval("<unknown>", 1, 1, expression);

            log.info("Expression result: " + obj);
        }
        else if (path != null){
        	log.info("Evaluating file: " + path);
        	//Make a file.  Is it really a file?  Sweet, execute it.
        	//Is it not a file?  Make a URI, convert that to a URL, or maybe just make a URL, check on that.  Execute it.
        	//Do it in a nifty way so mother would be proud.
        	File pathFile = new File(path);
        	URL pathURL;
        	if (pathFile.isFile()) {
        		pathURL = pathFile.toURL();
        	} else {
        		URI pathURI = new URI(path);
        		pathURL = pathURI.toURL();
        	}
        	
        	engine.exec(path, 1, 1, pathURL.getContent());
        	
        	log.info("Finished executing script: " + filename);
        }

        if (this.interactive) {
            log.debug("Starting interactive console...");
            
            Console.Executor executor = new Console.Executor() {
                public Result execute(final String line) throws Exception {
                    // Execute unless the line is just blank
                    
                    if (!line.trim().equals("")) {
                        engine.exec("<unknown>", 1, 1, line);
                    }

                    return Result.CONTINUE;
                }
            };

            JLineConsole runner = new JLineConsole(executor, io, terminal);

            runner.setErrorHandler(new Console.ErrorHandler() {
                public Result handleError(final Throwable error) {
                    log.error("Script evalutation failed: " + error, error);

                    return Result.CONTINUE;
                }
            });

            runner.setPrompter(new Console.Prompter() {
                public String prompt() {
                    return language + "> ";
                }
            });

            runner.run();
        }

        return SUCCESS;
    }
}
