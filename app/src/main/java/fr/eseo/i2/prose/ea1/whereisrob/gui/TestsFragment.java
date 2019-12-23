/**
 * @file TestsFragment.java
 *
 * @brief La classe TestsFragment permet de sélectionner un scénario préenregistré à l’aide d’une
 * liste déroulant.
 *
 * @author Timothée GIRARD, Charly JONCHERAY, Matthias PASQUIER
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
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import com.whereisrob.whereisrob.R;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import fr.eseo.i2.prose.ea1.whereisrob.testdata.ScenarioManager;

import static android.R.layout.simple_spinner_item;

/**
 * La classe TestsFragment permet de sélectionner un scénario préenregistré à l’aide d’une
 * liste déroulant.
 */
public class TestsFragment extends Fragment {

    private Spinner spinnerTests;
    private ArrayAdapter adapter;
    private ScenarioManager scenarioManager;
    private List<Point> tests;
    private List<String> testNames;

    /**
     * Le constructeur vide de la classe
     */
    public TestsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tests = new ArrayList();
        testNames = new ArrayList();
        this.scenarioManager = new ScenarioManager(getContext());
        scenarioManager.open();
        try {
            tests = scenarioManager.getScenarios();
            for (Point test : tests){
                testNames.add("("+test.x+", "+test.y+")");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_tests, container, false);
        spinnerTests = (Spinner)view.findViewById(R.id.spinnerTests);
        adapter = new ArrayAdapter(this.getActivity(), simple_spinner_item, testNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTests.setAdapter(adapter);
        spinnerTests.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Point testSelected = stringToPoint((String)spinnerTests.getSelectedItem());
                Toast.makeText(getActivity(), getString(R.string.selectionTest) + " " + spinnerTests.getSelectedItem(), Toast.LENGTH_SHORT).show();
                GUIActivity gui = (GUIActivity) getActivity();
                gui.setPositionScenario(testSelected);
                gui.setWishedPosition(testSelected);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getActivity(), getString(R.string.selectionTest) + " " + testNames.get(0), Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }


    /**
     * Accesseur du spinner pour la liste déroulante des tests
     *
     * @return Le spinner pour la liste déroulante des tests
     */
    public Spinner getSpinnerTests() {
        return spinnerTests;
    }

    private Point stringToPoint(String point) {
        String parts[] = point.split(", ");
        String part1 = parts[0].replace("(","");
        String part2 = parts[1].replace(")","");
        Point myPoint = new Point(Integer.parseInt(part1),Integer.parseInt(part2));
        return myPoint;
    }

} // End of class