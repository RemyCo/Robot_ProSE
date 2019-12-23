/**
 * @file RecyclerViewAdapter.java
 *
 * @brief La classe RecyclerViewAdapter permet la gestion du RecyclerView de l'historique.
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

package fr.eseo.i2.prose.ea1.whereisrob.testdata.RecyclerView;

import android.view.ViewGroup;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import java.text.SimpleDateFormat;
import java.util.List;

import com.whereisrob.whereisrob.R;

import fr.eseo.i2.prose.ea1.whereisrob.testdata.Historian;
import fr.eseo.i2.prose.ea1.whereisrob.testdata.TestResults;

/**
 * La classe RecyclerViewAdapter qui hérite de RecyclerView est la classe qui permet la gestion du RecyclerView de l'historique.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    Context mContext;
    List<TestResults> mData;
    Historian historian;

    /**
     * Constructeur de la classe
     * @param context
     * @param data
     * @param historian
     */
    public RecyclerViewAdapter(Context context, List<TestResults> data, Historian historian) {
        mContext = context;
        mData = data;
        this.historian = historian;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_history,viewGroup,false);
        MyViewHolder vHolder = new MyViewHolder(v);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = format.format(mData.get(i).getDate());

        String parts[] = date.split(" ");
        String partDate = parts[0];
        String partHour = parts[1];

        myViewHolder.et_date.setText(partDate);
        myViewHolder.et_hour.setText(partHour);

        myViewHolder.et_desiredPosition.setText(mData.get(i).getRealPosition().toString());
        myViewHolder.et_calculatedPosition.setText(mData.get(i).getCalculatedPosition().toString());
        myViewHolder.et_positionDifference.setText("" + mData.get(i).getScopeError());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private EditText et_date;
        private EditText et_hour;
        private EditText et_desiredPosition;
        private EditText et_calculatedPosition;
        private EditText et_positionDifference;

        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            et_date = (EditText) itemView.findViewById(R.id.date_history);
            et_hour = (EditText) itemView.findViewById(R.id.hour_history);
            et_desiredPosition = (EditText) itemView.findViewById(R.id.desiredPosition_history);
            et_calculatedPosition = (EditText) itemView.findViewById(R.id.calculatedPosition_history);
            et_positionDifference = (EditText) itemView.findViewById(R.id.positionDifference_history);

            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);

        }
    }

    /**
     * Permet la suppression du RecyclerView du test ayant la position passé en paramètre. Le test est également supprimé de la base de donnée
     * @param position la position du résultat de test
     */
    public void removeItem(int position) {
        historian.deleteTestResults(mData.get(position));
        mData.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);

    }

    /**
     * Permet de restaurer le résultat de test précédemment supprimer quand l'Utilisateur appuie sur undo à sa position d'origine
     * @param testResults le résultat de test
     * @param position la position dans le RecyclerView du résultat de test
     */
    public void restoreItem(TestResults testResults, int position) {
        historian.recordTestResults(testResults);
        mData.add(position, testResults);
        notifyItemInserted(position);
    }

    /**
     * Permet d'ajouter un résulat de test dans le RecyclerView
     * @param testResults le résultat de test
     */
    public void addItem(TestResults testResults) {
        historian.recordTestResults(testResults);
        mData.add(testResults);
        notifyItemInserted(mData.size());
    }

}
