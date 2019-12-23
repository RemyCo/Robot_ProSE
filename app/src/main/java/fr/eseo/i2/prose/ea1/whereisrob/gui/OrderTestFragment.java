/**
 * @file OrderTestFragment.java
 *
 * @brief La classe OrderTestFragment permet de démarrer, d’arrêter ou d’annuler un scénario
 * de test de positionnement.
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
import android.widget.Button;
import com.whereisrob.whereisrob.R;
import fr.eseo.i2.prose.ea1.whereisrob.communication.Protocol;
import fr.eseo.i2.prose.ea1.whereisrob.communication.postman.ConnectionSingleton;

/**
 * La classe OrderTestFragment permet de démarrer, d’arrêter ou d’annuler un scénario
 * de test de positionnement.
 */
public class OrderTestFragment extends Fragment {

    //private static final String TAG = "Debug";

    Button buttonStart, buttonStop, buttonCancel;

    /**
     * Constructeur vide de la classe
     */
    public OrderTestFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_order_test, container, false);
        buttonStart = (Button) view.findViewById(R.id.startTest);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GUIActivity gui = (GUIActivity) getActivity();
                gui.setStartButtonVisible(false);
                buttonStart.setEnabled(false);
                buttonStop.setEnabled(true);
                buttonCancel.setEnabled(true);
                gui.calibrate();
            }
        });
        buttonStop = (Button) view.findViewById(R.id.stopTest);
        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GUIActivity gui = (GUIActivity) getActivity();
                ConnectionSingleton.getInstance().sendData(Protocol.stopTest);
                gui.setStartButtonVisible(true);
                gui.showConfirmDialogEndTest(getString(R.string.stopTest));
                buttonStart.setEnabled(true);
                buttonStop.setEnabled(false);
                buttonCancel.setEnabled(false);
            }
        });
        buttonCancel = (Button) view.findViewById(R.id.cancelTest);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GUIActivity gui = (GUIActivity) getActivity();
                ConnectionSingleton.getInstance().sendData(Protocol.stopTest);
                gui.setStartButtonVisible(true);
                buttonStart.setEnabled(true);
                buttonStop.setEnabled(false);
                buttonCancel.setEnabled(false);
            }
        });
        buttonStart.setEnabled(true);
        buttonStop.setEnabled(false);
        buttonCancel.setEnabled(false);
        return view;
    }

}   // End of class