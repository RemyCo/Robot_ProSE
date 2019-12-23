/**
 * @file PWMFragment.java
 *
 * @brief La classe PositionTestFragment permet d’afficher la position en temps réel calculée par
 * RobSoft ainsi que la position d’arrivée souhaitée lors de la sélection d’un scénario.
 *
 * @author Timothée GIRARD, Charly JONCHERAY
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

package fr.eseo.i2.prose.ea1.whereisrob.gui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.whereisrob.whereisrob.R;

/**
 * La classe PWMFragment permet d’afficher les PWM sur les moteurs de Rob avec des
 * flèches sur l’image du robot.
 */
public class PWMFragment extends Fragment {

    //private static final String TAG = "Debug";

    private PWMView pwmView;

    /**
     * Le constructeur vide de la classe
     */
    public PWMFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_pwm, container, false);
        pwmView = view.findViewById(R.id.pwmView);
        return view;
    }

    /**
     * Méthode appelant le dessin des lignes sur
     * le fragment
     *
     * @param pwmLeft La puissance PWM sur le moteur gauche de Rob
     * @param pwmRight La puissance PWM sur le moteur droit de Rob
     */
    public void drawLines(int pwmLeft, int pwmRight) {
        pwmView.drawlines(-pwmLeft, -pwmRight);
    }

}   // End of class
