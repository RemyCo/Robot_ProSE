/**
 * @file DispatcherAndroid.java
 *
 * @brief La classe DispatcherAndroid permet de donner les bon ordre à GUIActivity en fonction
 * des messages reçus de RobSoft.
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

import android.app.Activity;
import android.graphics.Point;
import android.util.Log;

import com.whereisrob.whereisrob.R;

import java.util.StringTokenizer;
import fr.eseo.i2.prose.ea1.whereisrob.gui.GUIActivity;
import fr.eseo.i2.prose.ea1.whereisrob.model.ModelSingleton;

/**
 * La classe DispatcherAndroid permet de donner les bon ordre à GUIActivity en fonction
 *  * des messages reçus de RobSoft.
 */
public class DispatcherAndroid {

    private static final String TAG = "Debug";

    private GUIActivity guiActivity;

    /**
     * Constructeur de la classse
     *
     * @param activity Paramètre utilisé pour appeler des méthodes de GUIActivity
     */
    public DispatcherAndroid(Activity activity) {
        this.guiActivity = (GUIActivity) activity;
    }

    /**
     * Permet d’appeler les bonnes méthodes de GUIActivity
     * en fonctions des données reçues de RobSoft
     *
     * @param message Le message à dispatcher
     */
    public void dispatch(String message) {
        try {
            if(message.contains(Protocol.cpu)) {
                guiActivity.setCPU(Integer.parseInt(message.substring(4)));
            } else if(message.contains(Protocol.ram)) {
                guiActivity.setRAM(Integer.parseInt(message.substring(4)));
            } else if(message.contains(Protocol.pwmLeft) && message.contains(Protocol.pwmRight)) {
                StringTokenizer stringTokenizer = new StringTokenizer(message , ";");
                guiActivity.setWheelsPower(Integer.parseInt(stringTokenizer.nextToken().substring(3)),
                        Integer.parseInt(stringTokenizer.nextToken().substring(3)));
            } else if(message.contains(Protocol.positionX) && message.contains(Protocol.positionY) && guiActivity.myBooleanVisible == false ) {
                StringTokenizer stringTokenizer = new StringTokenizer(message , ";");
                Point position = new Point(Integer.parseInt(stringTokenizer.nextToken().substring(3)),
                        Integer.parseInt(stringTokenizer.nextToken().substring(3)));
                guiActivity.setCurrentPosition(position);
                ModelSingleton.getInstance().getCartographer().setCurrentPosition(position);
            } else if(message.contains(Protocol.endTest)) {
                guiActivity.showConfirmDialogEndTest(guiActivity.getString(R.string.endTest));
                guiActivity.setStartButtonVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Impossible to dispatch !\n" + e.getMessage());
        }
    }

} // End of class