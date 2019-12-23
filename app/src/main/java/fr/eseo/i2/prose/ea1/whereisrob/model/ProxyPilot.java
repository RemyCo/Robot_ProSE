/**
 * @file ProxyPilot.java
 *
 * @brief La classe ProxyPilot permet d'envoyer les ordres de déplacement à RobSoft lors du pilotage manuel.
 *
 * @author Timothée GIRARD
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

import fr.eseo.i2.prose.ea1.whereisrob.communication.Protocol;
import fr.eseo.i2.prose.ea1.whereisrob.communication.postman.ConnectionSingleton;
import fr.eseo.i2.prose.ea1.whereisrob.gui.GUIActivity;

/**
 * La classe ProxyPilot permet d'envoyer les ordres de déplacement à RobSoft lors du pilotage manuel.
 */
public class ProxyPilot {

    ConnectionSingleton connectionSingleton = ConnectionSingleton.getInstance();

    /**
     * Modifie la direction que doit prendre le robot pour
     * aller vers la position d’arrivée
     *
     * @param direction La direction que doit prendre le robot pour aller vers la position d’arrivée
     */
    public void setDirection(GUIActivity.Direction direction) {
        switch (direction) {
            case FORWARD:
                connectionSingleton.sendData(Protocol.argumentMoveForward);
                break;
            case BACKWARD:
                connectionSingleton.sendData(Protocol.argumentMoveBackward);
                break;
            case LEFT:
                connectionSingleton.sendData(Protocol.argumentMoveLeft);
                break;
            case RIGHT:
                connectionSingleton.sendData(Protocol.argumentMoveRight);
                break;
        }
    }

}   // End of class