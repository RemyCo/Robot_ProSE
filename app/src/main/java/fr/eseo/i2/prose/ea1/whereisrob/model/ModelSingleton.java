/**
 * @file ModelSingleton.java
 *
 * @brief La classe ModelSingleton contient les objets dont on n'a besoin d'instancier qu'une seule fois.
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

/**
 * La classe ModelSingleton contient les objets dont on n'a besoin d'instancier qu'une seule fois.
 */
public class ModelSingleton {

    /**
     * Instance du modèle
     */
    private static final ModelSingleton modelInstance = new ModelSingleton();

    /**
     * Accesseur de l'instance du modèle
     *
     * @return L'instance du modèle
     */
    public static ModelSingleton getInstance() {
        return modelInstance;
    }

    private ProxyPilot proxyPilot;
    private ProxyCopilot proxyCopilot;
    private Cartographer cartographer;

    /**
     * Constructeur de la classe
     */
    private ModelSingleton() {
        proxyPilot = new ProxyPilot();
        proxyCopilot = new ProxyCopilot();
        cartographer = new Cartographer();;
    }

    /**
     * Accesseur du proxy pilot
     *
     * @return Le proxy pilot
     */
    public ProxyPilot getProxyPilot() {
        return proxyPilot;
    }

    /**
     * Accesseur du proxy copilot
     *
     * @return Le proxy copilot
     */
    public ProxyCopilot getProxyCopilot() {
        return proxyCopilot;
    }

    /**
     * Accesseur du cartographe
     *
     * @return Le cartographe
     */
    public Cartographer getCartographer() {
        return cartographer;
    }
}   // End of class
