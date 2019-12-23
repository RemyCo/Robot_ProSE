/*
 * @file ProxyCopilot.java
 *
 * @brief La classe ProxyCopilot permet d'envoyer les positions à atteindre par Rob lors de l'exécution d'un test de positionnement.
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

import android.graphics.Point;

import fr.eseo.i2.prose.ea1.whereisrob.communication.Protocol;
import fr.eseo.i2.prose.ea1.whereisrob.communication.postman.ConnectionSingleton;

/**
 * La classe ProxyCopilot permet d'envoyer les positions à atteindre par Rob lors de l'exécution d'un test de positionnement.
 */
public class ProxyCopilot {

    ConnectionSingleton connectionSingleton = ConnectionSingleton.getInstance(); // For Testing

    /**
     * Démarre le scénario de test choisit par l’Utilisateur
     * en envoyant sa position de destination
     *
     * @param position Le scénario de test (la position à atteindre)
     */
    public void beginScenario(Point position) {
        connectionSingleton.sendData(Protocol.startTest + position.x + ";" + position.y + "\n"); // For Testing
        //ConnectionSingleton.getInstance().sendData(Protocol.startTest + position.x + ";" + position.y + "\n"); // For Production
    }

} // End of class
