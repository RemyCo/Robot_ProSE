/**
 * @file CommandPilotFragment.java
 *
 * @brief La classe CommandPilotFragment permet à l’Utilisateur de piloter manuellement Rob
 * à l’aide de flèches directionnelles.
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import com.whereisrob.whereisrob.R;
import fr.eseo.i2.prose.ea1.whereisrob.communication.Protocol;
import fr.eseo.i2.prose.ea1.whereisrob.communication.postman.ConnectionSingleton;

/**
 * La classe CommandPilotFragment permet à l’Utilisateur de piloter manuellement Rob
 *  * à l’aide de flèches directionnelles.
 */
public class CommandPilotFragment extends Fragment {

    //private static final String TAG = "Debug";

    private ImageButton btnLeft;
    private ImageButton btnRight;
    private ImageButton btnUp;
    private ImageButton btnDown;

    /**
     * Le constructeur vide de la classe
     */
    public CommandPilotFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_command_pilot, container, false);
        btnLeft = (ImageButton)view.findViewById(R.id.arrowLeft);
        btnRight = (ImageButton)view.findViewById(R.id.arrowRight);
        btnUp = (ImageButton)view.findViewById(R.id.arrowUp);
        btnDown = (ImageButton)view.findViewById(R.id.arrowDown);

        btnLeft.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                GUIActivity gui = (GUIActivity) getActivity();
                if(gui.myBooleanVisible == false) {
                    if(arg1.getAction() == MotionEvent.ACTION_DOWN || arg1.getAction() == MotionEvent.ACTION_MOVE) {
                        gui.sendDirection(GUIActivity.Direction.LEFT);
                    } else if(arg1.getAction() == MotionEvent.ACTION_UP) {
                        ConnectionSingleton.getInstance().sendData(Protocol.argumentStop);
                        gui.oldDirection = Protocol.argumentStop;
                        //gui.setWheelsPower(0);
                    }
                }
                return true;
            }
        });
        btnRight.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                GUIActivity gui = (GUIActivity) getActivity();
                if(gui.myBooleanVisible == false) {
                    if(arg1.getAction() == MotionEvent.ACTION_DOWN || arg1.getAction() == MotionEvent.ACTION_MOVE) {
                        gui.sendDirection(GUIActivity.Direction.RIGHT);
                    } else if(arg1.getAction() == MotionEvent.ACTION_UP) {
                        ConnectionSingleton.getInstance().sendData(Protocol.argumentStop);
                        gui.oldDirection = Protocol.argumentStop;
                        //gui.setWheelsPower(0);
                    }
                }
                return true;
            }
        });
        btnUp.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                GUIActivity gui = (GUIActivity) getActivity();
                if(gui.myBooleanVisible == false) {
                    if(arg1.getAction() == MotionEvent.ACTION_DOWN || arg1.getAction() == MotionEvent.ACTION_MOVE) {
                        gui.sendDirection(GUIActivity.Direction.FORWARD);
                    } else if(arg1.getAction() == MotionEvent.ACTION_UP) {
                        ConnectionSingleton.getInstance().sendData(Protocol.argumentStop);
                        gui.oldDirection = Protocol.argumentStop;
                        //gui.setWheelsPower(0);
                    }
                }
                return true;
            }
        });
        btnDown.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                GUIActivity gui = (GUIActivity) getActivity();
                if(gui.myBooleanVisible == false) {
                    if(arg1.getAction() == MotionEvent.ACTION_DOWN || arg1.getAction() == MotionEvent.ACTION_MOVE) {
                        gui.sendDirection(GUIActivity.Direction.BACKWARD);
                    } else if(arg1.getAction() == MotionEvent.ACTION_UP) {
                        ConnectionSingleton.getInstance().sendData(Protocol.argumentStop);
                        gui.oldDirection = Protocol.argumentStop;
                        //gui.setWheelsPower(0);
                    }
                }
                return true;
            }
        });

        return view;
    }

}   // End of class