/**
 * @file PositionPilotFragment.java
 *
 * @brief La classe PositionPilotFragment permet d’afficher la position calculée par RobSoft.
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

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.whereisrob.whereisrob.R;

/**
 * La classe PositionPilotFragment permet d’afficher la position calculée par RobSoft.
 */
public class PositionPilotFragment extends Fragment {

    //private static final String TAG = "Debug";

    EditText posX, posY;

    /**
     * Constructeur vide de la classe
     */
    public PositionPilotFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_position_pilotage, container, false);
        posX = (EditText)view.findViewById(R.id.posX);
        posY = (EditText)view.findViewById(R.id.posY);
        setPosition(new Point(0,0));
        return view;
    }

    /**
     * Permet de modifier la position à afficher
     *
     * @param position La position à afficher
     */
    public void setPosition(Point position) {
        posX.setText(position.x + " cm");
        posY.setText(position.y + " cm");
    }

    /**
     * Affiche "erreur" dans les champs de position
     */
    public void setError(String message) {
        posX.setText(message);
        posY.setText(message);
    }

}   // End of class
