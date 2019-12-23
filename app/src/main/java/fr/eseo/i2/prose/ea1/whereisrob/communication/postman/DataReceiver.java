/**
 * @file DataReceiver.java
 *
 * @brief La classe DataReceiver permet de recevoir les données envoyées par RobSoft, elle hérite
 * de la classe Thread car elle doit lire les données en permanence.
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

package fr.eseo.i2.prose.ea1.whereisrob.communication.postman;

import fr.eseo.i2.prose.ea1.whereisrob.communication.DispatcherAndroid;

/**
 * La classe DataReceiver permet de recevoir les données envoyées par RobSoft, elle hérite
 * de la classe Thread car elle doit lire les données en permanence.
 */
public class DataReceiver extends Thread {

    private static final String TAG = "Debug";

    private DispatcherAndroid dispatcher;

    /**
     * Le constructeur de la classe
     *
     * @param dispatcher Le dispatcheur permettant de dispatcher les données reçues
     */
    public DataReceiver(DispatcherAndroid dispatcher) {
        this.dispatcher = dispatcher;
    }

    /**
     * La méthode exécutée lorsqu’on appelle la méthode start du thread, elle lit les
     * données de RobSoft tant que l’application est connectée
     */
    @Override
    public void run() {
        while(ConnectionSingleton.getInstance().getConnectionState() == ConnectionSingleton.ConnectionState.CONNECTED && ConnectionSingleton.getInstance().getSocketState()) {
            String message = ConnectionSingleton.getInstance().readData();
            dispatcher.dispatch(message);
        }
    }

} // End of class