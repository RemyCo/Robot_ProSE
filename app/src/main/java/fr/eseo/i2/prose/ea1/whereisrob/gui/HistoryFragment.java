/**
 * @file HistoryFragment.java
 *
 * @brief La classe HistoryFragment permet d'afficher l'ensemble des résultats de test enregistrés.
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

package fr.eseo.i2.prose.ea1.whereisrob.gui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.whereisrob.whereisrob.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import fr.eseo.i2.prose.ea1.whereisrob.testdata.Historian;
import fr.eseo.i2.prose.ea1.whereisrob.testdata.RecyclerView.RecyclerItemTouchHelper;
import fr.eseo.i2.prose.ea1.whereisrob.testdata.RecyclerView.RecyclerViewAdapter;
import fr.eseo.i2.prose.ea1.whereisrob.testdata.TestResults;

/**
 *     La classe HistoryFragment permet d'afficher l'ensemble des résultats de test enregistrés.
 */

public class HistoryFragment extends Fragment  implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    View v;
    private RecyclerView myRecyclerView;
    private List<TestResults> testResultsList;
    private RecyclerViewAdapter mRecyclerViewAdapter;
    private LinearLayout mLinearLayout;
    private Historian historian;

    /**
     * Constructeur vide de la classe
     */
    public HistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Accesseur du RecyclerViewAdapter du fragment
     *
     * @return Le RecyclerViewAdapter du fragment
     */
    public RecyclerViewAdapter getRecyclerViewAdapter() {
        return mRecyclerViewAdapter;
    }

    /**
     * Méthode appelé à la création de la vue
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_history,container,false);

        mLinearLayout = v.findViewById(R.id.linear_layout);

        myRecyclerView = v.findViewById(R.id.history_recyclerview);
        mRecyclerViewAdapter = new RecyclerViewAdapter(getContext(),testResultsList,historian);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myRecyclerView.setAdapter(mRecyclerViewAdapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(myRecyclerView);

        return v;

    }

    /**
     * Méthode appelé à la création du fragment
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.historian = new Historian(getContext());
        historian.open();

        try {
            testResultsList = historian.getAllTestResults();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gère le mouvement de swipe pour la suppression d'un résultat de test
     *
     * @param viewHolder
     * @param direction direction du mouvement de swipe
     * @param position position du résultat de test à supprimer
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        if (viewHolder instanceof RecyclerViewAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            Date dateFormat = testResultsList.get(viewHolder.getAdapterPosition()).getDate();

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = format.format(dateFormat);

            String parts[] = date.split(" ");
            String partDate = parts[0];
            String partHour = parts[1];

            // backup of removed item for undo purpose
            final TestResults deletedItem = testResultsList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            mRecyclerViewAdapter.removeItem(viewHolder.getAdapterPosition());


            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(mLinearLayout, "Le test du " + partDate + " à " + partHour + " supprimé de vos tests!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // undo is selected, restore the deleted item
                    mRecyclerViewAdapter.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();

        }
    }

}
