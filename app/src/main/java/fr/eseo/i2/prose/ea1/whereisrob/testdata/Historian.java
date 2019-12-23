/**
 * @file HistorianFragment.java
 *
 * @brief La classe HistorianFragment permet de stocker les données des tests effectués avec l’application.
 *
 * @author Charly Joncheray, Rémy Coquart
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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * La classe HistorianFragment permet de stocker les données des tests effectués avec l’application.
 */

public class Historian {

    List<TestResults> testResultsList;

    private static final String TABLE_NAME = "TestResults";
    private static final String KEY_ID_TEST = "id_TestResult";
    private static final String KEY_REAL_POSITION = "realPosition";
    private static final String KEY_CALCULATED_POSITION = "calculatedPosition";
    private static final String KEY_SCOPE_ERROR = "scopeError";
    private static final String KEY_DATE = "date";

    public static final String CREATE_TABLE_TEST_RESULTS = "CREATE TABLE " +TABLE_NAME+
            " (" +
            " "+KEY_ID_TEST+" INTEGER PRIMARY KEY," +
            " "+KEY_REAL_POSITION+" text," +
            " "+KEY_CALCULATED_POSITION+ " text," +
            " "+KEY_SCOPE_ERROR+ " float," +
            " "+KEY_DATE+ " text" +
            ");";

    private MySQLite maBaseSQLite; //Gestionnaire de fichier SQLite
    private SQLiteDatabase db;

    /**
     * Constructeur de la classe Historian
     * @param context
     */
    public Historian(Context context) {
        testResultsList = new ArrayList<>();
        maBaseSQLite = MySQLite.getInstance(context);
    }

    /**
     * Ouvre la table en lecture et en écriture
     */
    public void open() {
        db = maBaseSQLite.getWritableDatabase();
    }

    /**
     * Permet de sauvegarder les données de test en insérant les informations dans la base de données
     *
     */
    public long recordTestResults(TestResults testResults) {
        ContentValues values = new ContentValues();
        values.put(KEY_REAL_POSITION, testResults.realPosition.toString());
        values.put(KEY_CALCULATED_POSITION, testResults.calculatedPosition.toString());
        values.put(KEY_SCOPE_ERROR, testResults.scopeError);

        //Tue Jun 04 07:14:58 UTC 2019 -> 2019-06-04 07:14:58
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dbDate = format.format(testResults.date);
        values.put(KEY_DATE, dbDate);

        //insert() retourne l'id du nouvel enregistrement inséré, ou -1 en cas d'erreur
        return db.insert(TABLE_NAME,null,values);
    }

    /**
     * Permet de visualiser les résultats des tests de positionnement effectués par Rob, cette méthode retourne un curseur contenant les données de la base de données
     *
     * @return Tableau contenant les données des tests enregistrés
     */
    public List<TestResults> getAllTestResults() throws ParseException {

        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        while (c.moveToNext()) {
            TestResults testResults = new TestResults();
            testResults.setIdTestResult(c.getLong(c.getColumnIndex(KEY_ID_TEST)));
            testResults.setCalculatedPosition(stringToPoint(c.getString(c.getColumnIndex(KEY_CALCULATED_POSITION))));
            testResults.setRealPosition(stringToPoint(c.getString(c.getColumnIndex(KEY_REAL_POSITION))));
            testResults.setScopeError(c.getFloat(c.getColumnIndex(KEY_SCOPE_ERROR)));
            testResults.setDate(stringToDate(c.getString(c.getColumnIndex(KEY_DATE))));
            testResultsList.add(testResults);
        }
        return testResultsList;
    }

    /**
     * Supprime une donnée de test présente dans la base de données
     *
     * @int id du résultat de test à supprimer
     */
    public int deleteTestResults(TestResults testResults) {

        String where = KEY_ID_TEST+" = ?";
        String[] whereArgs = {testResults.getIdTestResult()+""};

        return db.delete(TABLE_NAME, where, whereArgs);
    }


    private Point stringToPoint(String point) {
        //Point(x, y)
        String parts[] = point.split(", ");
        String part1 = parts[0].replace("Point(","");
        String part2 = parts[1].replace(")","");
        Point myPoint = new Point(Integer.parseInt(part1),Integer.parseInt(part2));
        return myPoint;
    }

    private Date stringToDate(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date formatedDate = sdf.parse(date);

         return formatedDate;
    }

} // End of class
