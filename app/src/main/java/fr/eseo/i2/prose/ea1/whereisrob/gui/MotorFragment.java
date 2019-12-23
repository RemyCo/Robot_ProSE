/**
 * @file MotorFragment.java
 *
 * @brief La classe MotorFragment permet à l’Utilisateur de voir les PWM des roues de Rob.
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
import android.widget.EditText;
import com.whereisrob.whereisrob.R;

/**
 * La classe MotorFragment permet à l’Utilisateur de voir les PWM des roues de Rob.
 */
public class MotorFragment extends Fragment {

    //private static final String TAG = "Debug";

    private EditText pwmLeft, pwmRight;

    /**
     * Le constructeur vide de la classe
     */
    public MotorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_motor, container, false);
        pwmLeft = (EditText)view.findViewById(R.id.pwmLeft);
        pwmRight = (EditText)view.findViewById(R.id.pwmRight);
        return view;
    }

    /**
     * Permet de modifier la valeur de la PWM gauche
     *
     * @param valuePwmLeft La valeur de la PWM gauche
     */
    public void setPwmLeft(int valuePwmLeft) {
        pwmLeft.setText(valuePwmLeft + " %");
    }

    /**
     * Permet de modifier la valeur de la PWM droite
     *
     * @param valuePwmRight La valeur de la PWM droite
     */
    public void setPwmRight(int valuePwmRight) {
        pwmRight.setText(valuePwmRight + " %");
    }

}   // End of class
