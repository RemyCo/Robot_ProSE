/**
 * @file TestResults.java
 *
 * @brief La classe TestResults compare la position renvoyée par Rob et la position réelle de Rob.
 *
 * @author Charly Joncheray, Rémy COQUARD
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

package fr.eseo.i2.prose.ea1.whereisrob.testdata;

import android.graphics.Point;
import java.util.Date;

/**
 *    La classe TestResults définit comment sont sauvegardés les tests effectués dans la base de donné et compare la position renvoyée par Rob et la position réelle de Rob.
 */
public class TestResults {

    long idTestResult; //Identifiant du test
    Point realPosition;  // La position réelle de Rob, qu’on entre à la fin du test
    Point calculatedPosition; // La position calculée par Rob grâce à la technologie utilisée
    Float scopeError = 0.0f;  // L’erreur entre la position calculée et la position réelle
    Date date;  // La date du test

    /**
     * Constructeur vide de la classe
     */
    public TestResults() {

    }

    /**
     * Constructeur de la classe
     *
     * @param realPosition la position réelle de Rob, qu’on entre à la fin du test
     * @param calculatedPosition la position calculée par Rob grâce à la technologie utilisée
     * @param scopeError l'erreur entre la position calculée et la position réelle
     * @param date la date du test
     */
    public TestResults(long idTestResult, Point realPosition, Point calculatedPosition, Float scopeError, Date date) {
        this.idTestResult = idTestResult;
        this.realPosition = realPosition;
        this.calculatedPosition = calculatedPosition;
        this.scopeError = scopeError;
        this.date = date;
    }

    /**
     * Accesseur de l'id_TestResult
     *
     * @return L'identifiant du test
     */
    public long getIdTestResult() {
        return this.idTestResult;
    }

    /**
     * Permet la mise à jour de l'id_TestResult
     *
     * @param idTestResult
     */
    public void setIdTestResult(long idTestResult) {
        this.idTestResult = idTestResult;
    }

    /**
     * Retourne la valeur de la position calculée par Rob
     *
     * @return position calculée par Rob grâce à la technologie utilisée
     */
    public Point getCalculatedPosition (){
        return this.calculatedPosition;
    }

    /**
     * Retourne la valeur de la position réelle de Rob entrée par l’utilisateur à la fin du test
     *
     * @return position réelle de Rob, qu’on entre à la fin du test
     */
    public Point getRealPosition (){
        return this.realPosition;
    }

    /**
     * Modifie la valeur calculée par Rob
     *
     * @param calculatedPosition position calculée par Rob grâce à la technologie utilisée
     */
    public void setCalculatedPosition (Point calculatedPosition){
        this.calculatedPosition = calculatedPosition;
    }

    /**
     * Modifie la valeur de la position réelle de Rob entrée par l’utilisateur à la fin du test
     *
     * @param realPosition position réelle de Rob, qu’on entre à la fin du test
     */
    public void setRealPosition (Point realPosition){
        this.realPosition = realPosition;
    }

    /**
     * Retourne la précision du résultat du test
     *
     * @return Renvoie la précision du résultat du test
     */
    public Float getScopeError() {
        return this.scopeError;
    }

    /**
     * Modifie la valeur de la présition du résultat de test
     * @param scopeError précision du résultat du test
     */
    public void setScopeError(Float scopeError) {
        this.scopeError = scopeError;
    }

    /**
     * Retourne la date du test
     *
     * @return Date du test
     */
    public Date getDate (){
        return this.date;
    }

    /**
     * Modifie la valeur de la date du test
     * @param date la date du test
     */
    public void setDate(Date date) {
        this.date = date;
    }

} // End of class
