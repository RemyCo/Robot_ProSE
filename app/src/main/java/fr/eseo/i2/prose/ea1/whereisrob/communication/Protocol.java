/**
 * @file Protocol.java
 *
 * @brief La classe Protocol contient toutes les chaines de caractèrse définies dans le protocole
 * de communication établi pour communiquer avec RobSoft.
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

package fr.eseo.i2.prose.ea1.whereisrob.communication;

/**
 * La classe Protocol contient toutes les chaines de caractèrse définies dans le protocole
 * de communication établi pour communiquer avec RobSoft.
 */
public class Protocol {

    // WhereIsRob à RobSoft
    public static String argumentMoveForward = "Dz\n";
    public static String argumentMoveBackward = "Ds\n";
    public static String argumentMoveRight = "Dd\n";
    public static String argumentMoveLeft = "Dq\n";
    public static String argumentStop = "Da\n";

    public static String startTest = "Td";
    public static String stopTest = "Ta\n";

    // RobSoft à WhereIsRob
    public static String ram = "ram:";
    public static String cpu = "cpu:";

    public static String pwmLeft = "lW:";
    public static String pwmRight = "rW:";

    public static String endTest = "test:ended";

    // Les deux
    public static String positionX = "pX:";
    public static String positionY = "pY:";

}   // End of class