/**
 * @file ScenarioManager.java
 *
 * @brief La classe ScenarioManager permet d’exécuter des scenarios pré-enregistrés.
 *
 * @author Remy COQUARD, Matthias Pasquier
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

package fr.eseo.i2.prose.ea1.whereisrob.testdata;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import android.database.Cursor;
import android.graphics.Point;


/**
 * La classe ScenarioManager permet d’exécuter des scenarios pré-enregistrés.
 */
public class ScenarioManager {

    private List<Point> scenariosList;

    private static final String TABLE_NAME = "Scenarios";
    private static final String KEY_ID_SCENARIO = "id_Scenario";
    private static final String KEY_SCENARIO = "scenario";

    static final String CREATE_TABLE_SCENARIOS = "CREATE TABLE " +TABLE_NAME+
            " (" +
            " "+KEY_ID_SCENARIO+" INTEGER PRIMARY KEY," +
            " "+KEY_SCENARIO+" text" +
            ");";

    static final String FILL_TABLE_SCENARIOS = "INSERT INTO " +TABLE_NAME+
            " ( "+KEY_SCENARIO + ") VALUES"+
            "('(0, 100)'), " +
            "('(100, 100)'), " +
            "('(200, 0)'), " +
            "('(100, -200)'), " +
            "('(0, -300)'), " +
            "('(-300, -400)'), " +
            "('(-400, 0)'), " +
            "('(-400, 400)')";

    private MySQLite maBaseSQLite; //Gestionnaire de fichier SQLite
    private SQLiteDatabase db;

    //Constructeur
    public ScenarioManager(Context context) {
        scenariosList = new ArrayList<>();
        maBaseSQLite = MySQLite.getInstance(context);
    }

    //Ouvre la table en lecture et en écriture
    public void open() {
        db = maBaseSQLite.getWritableDatabase();
    }

    /**
     * Renvoie une liste des scénarios possibles à faire effectuer par Rob
     */
    public List<Point> getScenarios() throws ParseException {
        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        while (c.moveToNext()) {
            Point point = stringToPoint(c.getString(c.getColumnIndex(KEY_SCENARIO)));
            scenariosList.add(point);
        }
        c.close();
        return scenariosList;

    }


    private Point stringToPoint(String point) {
        //Point(x, y)
        String[] parts = point.split(", ");
        String part1 = parts[0].replace("(","");
        String part2 = parts[1].replace(")","");
        return new Point(Integer.parseInt(part1),Integer.parseInt(part2));

    }

} // End of class
