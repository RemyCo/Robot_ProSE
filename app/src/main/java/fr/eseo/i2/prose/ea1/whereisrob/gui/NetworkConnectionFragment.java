/**
 * @file NetworkConnectionFragment.java
 *
 * @brief La classe NetworkConnectionFragment permet d’afficher les champs de connexion de
 * Rob.
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
 * La classe NetworkConnectionFragment permet d’afficher les champs de connexion de
 * Rob.
 */
public class NetworkConnectionFragment extends Fragment {

    //private static final String TAG = "Debug";

    private EditText ipAndPort;

    /**
     * Le constructeur vide de la classe
     */
    public NetworkConnectionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_network_connection, container, false);
        ipAndPort = (EditText)view.findViewById(R.id.ipAndPort);
        ipAndPort.setText(ConnectionFragment.myIp + " : " + ConnectionFragment.myPort);
        return view;
    }

}   // End of class
