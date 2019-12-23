/*
 * @file ProxyPilotTest.java
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
import fr.eseo.i2.prose.ea1.whereisrob.gui.GUIActivity;

import static org.mockito.Mockito.verify;


public class ProxyPilotTest {

    private ConnectionSingleton connectionSingleton = Mockito.mock(ConnectionSingleton.class);
    private ProxyPilot proxyPilot = new ProxyPilot();

    @Test
    public void testSetDirectionForward(){
        proxyPilot.connectionSingleton = this.connectionSingleton;
        proxyPilot.setDirection(GUIActivity.Direction.FORWARD);
        verify(connectionSingleton).sendData(ArgumentMatchers.eq("Dz\n"));
    }

    @Test
    public void testSetDirectionBackward(){
        proxyPilot.connectionSingleton = this.connectionSingleton;
        proxyPilot.setDirection(GUIActivity.Direction.BACKWARD);
        verify(connectionSingleton).sendData(ArgumentMatchers.eq("Ds\n"));
    }

    @Test
    public void testSetDirectionLeft(){
        proxyPilot.connectionSingleton = this.connectionSingleton;
        proxyPilot.setDirection(GUIActivity.Direction.LEFT);
        verify(connectionSingleton).sendData(ArgumentMatchers.eq("Dq\n"));
    }

    @Test
    public void testSetDirectionRight(){
        proxyPilot.connectionSingleton = this.connectionSingleton;
        proxyPilot.setDirection(GUIActivity.Direction.RIGHT);
        verify(connectionSingleton).sendData(ArgumentMatchers.eq("Dd\n"));
    }

}