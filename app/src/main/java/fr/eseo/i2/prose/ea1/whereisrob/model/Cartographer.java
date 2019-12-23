/**
 * @file Cartographer.java
 *
 * @brief La classe Cartographer reçoit les positions du Robot et crée un tracé du passage de
 * Rob lors d’un test de positionnement. C'est le Model qui comporte les informations tels que la Position actuelle, et la position de destination.
 *
 *
 * @author Remy COQUARD
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
import java.util.ArrayList;

/**
 * La classe Cartographer reçoit les positions du Robot et crée un tracé du passage de
 * Rob lors d’un test de positionnement.
 */
public class Cartographer {

    private Point originPosition;  // La position d’origine
    private Point currentPosition;  // La position courante de Rob
    private Point destinationPosition;  // La destination de Rob
    private ArrayList<Point> mapData = new ArrayList<>();  // Le tracé de Rob sur la carte lors d’un test de positionnement

    /**
     * Cette classe sera utile pour définir une position en 2D. Méthode permettant de modifier la valeur de la position d’origine
     *
     * @param position d'origine
     */
    public void setOriginPosition(Point position) {
        this.originPosition = position;
    }

    /**
     * Permet la mise à jour de la position du robot par Geometer
     *
     */
    public void setCurrentPosition(Point position){
        this.currentPosition = position;
    }

    public void setDestinationPosition(Point destinationPosition) {
        this.destinationPosition = destinationPosition;
    }

    private void clearMapData() {
        this.mapData.clear();
    }

    /**
     * Permet la mise à jour de la position du robot par Geometer
     *
     */
    public Point getWishedPosition(){
        return this.destinationPosition;
    }

    public Point getOriginPosition() {
        return originPosition;
    }

    public Point getCurrentPosition() {
        return currentPosition;
    }

    public Point getDestinationPosition() {
        return destinationPosition;
    }

    /**
     * PErmet de récupérer les points par lesquels le robot est passé
     * @return : mapData
     */
    public ArrayList<Point> getMapData() {
        return mapData;
    }

    /**
     * Permet d'ajouter un point dans la mapData
     *
     * @param point : Le point à ajouter dans la mapData
     */
    public void addToMapData(Point point){
        this.mapData.add(point);
    }

    public void calibrate(){
        clearMapData();
    }
} // End of class
