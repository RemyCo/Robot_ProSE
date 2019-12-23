/*
 * @file ProxyCopilotTest.java
 *
 * @brief La classe ProxyCopilotTest permet de tester la classe ProxyCopilot
 *
 * @author Rémy Coquard
 *
 * @copyright 2019 ProseA1
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package fr.eseo.i2.prose.ea1.whereisrob.model;

import android.graphics.Point;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import fr.eseo.i2.prose.ea1.whereisrob.communication.postman.ConnectionSingleton;

import static org.mockito.Mockito.verify;


public class ProxyCopilotTest {

    private ConnectionSingleton connectionSingleton = Mockito.mock(ConnectionSingleton.class);
    private ProxyCopilot proxyCopilot = new ProxyCopilot();


    @Test
    public void testBeginScenario(){
        Point point = new Point(100,100);
        proxyCopilot.connectionSingleton = this.connectionSingleton;
        proxyCopilot.beginScenario(point);
        verify(connectionSingleton).sendData(ArgumentMatchers.eq("Td100;100\n"));
    }

}
