/**
 * @file CartographerFragment.java
 *
 * @brief La classe CartographerFragment est le Fragment qui gère la carte. Sur celle-ci, est compris la Vue CartographerView.
 * Cette classe permet aussi de gérer le toucher sur la grille, et ainsi permettre de gérer la position souhaitée.
 *
 * @author Rémy Coquard
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

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.whereisrob.whereisrob.R;

import java.util.ArrayList;

import static android.graphics.Color.rgb;

public class CartographerFragment extends Fragment {

    private CartographerView cartographerView;
    public CartographerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_cartographer, container, false);
        cartographerView = view.findViewById(R.id.cartographerview);
        view.setBackgroundColor(rgb(232, 232, 232));
        final GUIActivity guiActivity = (GUIActivity) getActivity();
        view.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch(View v, MotionEvent event) {
                boolean ret = false;
                assert guiActivity != null;
                if(guiActivity.myBooleanVisible) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        ret = cartographerView.changeBluePoint(event);
                        cartographerView.changeView();
                    } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                        ret = cartographerView.changeBluePoint(event);
                        cartographerView.changeView();
                    } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                        //do something
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        Point position = cartographerView.setBluePointToIntersections(event);
                        setDestinationPosition(position.x, position.y);
                        ret = false;
                    }
                }
                return ret;
            }
        });
        return view;
    }

    public void calibrate() {
        cartographerView.calibrate();
    }

    public void setCurrentPosition(Point robotPosition){
        cartographerView.setCurrentPosition(robotPosition);
    }

    public void setDestinationPosition(int x, int y){
        Point currentPoint = cartographerView.calibrateToXAndY(x, y);
        GUIActivity gui = (GUIActivity) getActivity();
        gui.setWishedPositionFromCartographer(new Point(Math.round((float)currentPoint.x/100)*100, Math.round((float)currentPoint.y/100)*100));
    }

    public void setWishedPositionFromSpinner(Point position) {
        position = cartographerView.calibrateToCanvas(position.x,position.y);
        cartographerView.setWishedPositionFromSpinner(position);
    }

    public void setMapData(ArrayList<Point> mapData) {
        cartographerView.setMapData(mapData);
    }
}   // End of class