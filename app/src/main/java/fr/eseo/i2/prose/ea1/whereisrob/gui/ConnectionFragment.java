/**
 * @file ConnectionFragment.java
 *
 * @brief La classe ConnectionFragment permet à l’Utilisateur de se connecter à WhereIsRob en
 * entrant l’adresse ip et le numéro de port de celui-ci.
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

package fr.eseo.i2.prose.ea1.whereisrob.gui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.whereisrob.whereisrob.R;

/**
 * La classe ConnectionFragment permet à l’Utilisateur de se connecter à WhereIsRob en
 *  * entrant l’adresse ip et le numéro de port de celui-ci.
 */
public class ConnectionFragment extends Fragment {

    private static final String TAG = "Debug";

    private EditText editTextIp;
    public static String myIp;
    private EditText editTextPort;
    public static int myPort;
    private Button buttonConnect;
    private SharedPreferences myPrefs;

    /**
     * Le constructeur vide de la classe
     */
    public ConnectionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // https://appsandbiscuits.com/saving-data-with-sharedpreferences-android-9-9fecae19896a
        myPrefs = getActivity().getSharedPreferences("prefID", Context.MODE_PRIVATE);

        myIp = myPrefs.getString("ipSaved","172.25.1.45");
        myPort = myPrefs.getInt("portSaved",23456);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_connection, container, false);

        editTextIp = (EditText) view.findViewById(R.id.ip_address);
        editTextPort = (EditText) view.findViewById(R.id.port_number);

        buttonConnect = (Button) view.findViewById(R.id.connect);

        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GUIActivity gui = (GUIActivity) getActivity();
                gui.connect(editTextIp.getText().toString(), Integer.parseInt(editTextPort.getText().toString()));

                // Set up SharedPreferences
                myPrefs = getActivity().getSharedPreferences("prefID", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = myPrefs.edit();
                editor.putString("ipSaved", editTextIp.getText().toString());
                editor.putInt("portSaved", Integer.parseInt(editTextPort.getText().toString()));
                editor.apply();

                if(gui.checkWifi() && gui.validateConnectionFields(editTextIp.getText().toString(), Integer.parseInt(editTextPort.getText().toString()))) {

                    setButtonConnect(false);
                }

                myIp = editTextIp.getText().toString();
                myPort = Integer.parseInt(editTextPort.getText().toString());
            }
        });

        editTextIp.setText(this.myIp);
        editTextPort.setText("" + this.myPort);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG,"On resume instance state : " + getActivity().getLocalClassName() + "\n");
        editTextIp.setText(this.myIp);
        editTextPort.setText("" + this.myPort);
        Log.i(TAG,"IPv4 : " + myIp + " Port : " + myPort);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG,"On save instance state : " + getActivity().getLocalClassName() + "\n");
        // https://appsandbiscuits.com/saving-data-with-sharedpreferences-android-9-9fecae19896a
        myPrefs = getActivity().getSharedPreferences("prefID", Context.MODE_PRIVATE);
        myIp = myPrefs.getString("ipSaved","172.25.1.45");
        myPort = myPrefs.getInt("portSaved",23456);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.i(TAG, "On view state restored : " + getActivity().getLocalClassName() + "\n");
        editTextIp.setText(this.myIp);
        editTextPort.setText("" + this.myPort);
    }

    /**
     * Permet de rendre le bouton cliquable ou non
     * selon l’état de la connexion
     *
     * @param visibility La visibilité du bouton
     */
    public void setButtonConnect(boolean visibility) {
        if(visibility) buttonConnect.setEnabled(true);
        else buttonConnect.setEnabled(false);
    }

}   // End of class
