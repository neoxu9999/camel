/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.dsl.java.joor;

import org.apache.camel.CamelContext;
import org.apache.camel.main.Main;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JavaMainBindToRegistryTest {

    @Test
    public void testMainBindToRegistry() throws Exception {
        Main main = new Main();
        main.configure().withRoutesIncludePattern("routes/MyEchoRoute.java,routes/MyEcho.java");
        main.start();

        CamelContext camelContext = main.getCamelContext();
        assertNotNull(camelContext);

        String out = main.getCamelTemplate().requestBody("direct:start", "Hello", String.class);
        assertEquals("HelloHello", out);

        main.stop();
    }

}
