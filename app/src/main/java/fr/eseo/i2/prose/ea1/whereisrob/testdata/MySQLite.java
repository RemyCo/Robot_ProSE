/**
 * @file MySQLite.java
 *
 * @brief La classe MySQLite permet de gérer le fichier SQLite de la base de données WhereIsRob.
 *
 * @author Charly Joncheray
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
import android.database.sqlite.SQLiteOpenHelper;

/**
 * La classe MySQLite qui hérite de SQLiteOpenHelper est la classe qui permet de gérer le fichier SQLite correspondant
 * à la base de données de l'application WhereIsRob
 */

public class MySQLite extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "db.sqlite"; //nom de la base du fichier créer pour la base de donnée SQLite
    private static final int DATABASE_VERSION = 1; //Version 1 de la base de données
    private static MySQLite sInstance;

    /**
     * Accesseur de l'instance de la base de données
     *
     * @param context
     * @return l'instance de la base de données
     */
    public static synchronized MySQLite getInstance(Context context){
        if (sInstance == null) {
            sInstance = new MySQLite(context);
        }
        return sInstance;
    }

    /**
     * Constructeur de la classe
     *
     * @param context
     */
    private MySQLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     *Création de la base de données. Exécute les requête de création des tables
     *
     * @param db Base de données SQLite
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Historian.CREATE_TABLE_TEST_RESULTS);
        db.execSQL(ScenarioManager.CREATE_TABLE_SCENARIOS);
        db.execSQL(ScenarioManager.FILL_TABLE_SCENARIOS);
    }

    /**
     * Mise à jour de la base de données. Méthode appelée sur incrémentation de DATABASE_VERSION
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
}
