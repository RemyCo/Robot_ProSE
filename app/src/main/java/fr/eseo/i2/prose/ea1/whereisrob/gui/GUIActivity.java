/*
 * @file GUIActivity.java
 *
 * @brief La classe GUIActivity, interface Utilisateur, centralise toutes les caractéristiques ren-
 * dant possible l’interaction entre l’Utilisateur et WhereIsRob, afin de contrôler Rob. Celle-ci
 * récupère les données de Rob et les affiche. Cette classe permet de démarrer l’application,
 * elle est déclarée comme étant le "LAUNCHER" de l’application grâce au fichier xml "An-
 * droidManifest.xml". C’est l’objet "Starter" de l’application.
 * Les écrans que comporte cette classe GUIActivity sont décrits dans le dossier de spé-
 * cification.
 *
 * @author Timothée GIRARD, Charly JONCHERAY, Rémy Coquard
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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.whereisrob.whereisrob.R;
import java.io.IOException;
import java.util.Date;
import fr.eseo.i2.prose.ea1.whereisrob.model.Cartographer;
import fr.eseo.i2.prose.ea1.whereisrob.communication.IPAddressValidator;
import fr.eseo.i2.prose.ea1.whereisrob.communication.Protocol;
import fr.eseo.i2.prose.ea1.whereisrob.communication.postman.ConnectionSingleton;
import fr.eseo.i2.prose.ea1.whereisrob.model.ModelSingleton;
import fr.eseo.i2.prose.ea1.whereisrob.testdata.RecyclerView.RecyclerViewAdapter;
import fr.eseo.i2.prose.ea1.whereisrob.testdata.TestResults;

/**
 * La classe GUIActivity, interface Utilisateur, centralise toutes les caractéristiques ren-
 * dant possible l’interaction entre l’Utilisateur et WhereIsRob, afin de contrôler Rob. Celle-ci
 * récupère les données de Rob et les affiche. Cette classe permet de démarrer l’application,
 * elle est déclarée comme étant le "LAUNCHER" de l’application grâce au fichier xml "An-
 * droidManifest.xml". C’est l’objet "Starter" de l’application.
 * Les écrans que comporte cette classe GUIActivity sont décrits dans le dossier de spé-
 * cification.
 */
public class GUIActivity extends AppCompatActivity implements ConnectionSingleton.ICallbackConnection, NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "Debug";

    // Attributs utiles pour chercker la connexion Wi-Fi
    NetworkInfo networkInfoWifi;
    ConnectivityManager connectivityManager;

    // Fragment de connexion
    private ConnectionFragment connectionFragment;

    // Fragments communs au mode pilotage manuel et test
    private NetworkConnectionFragment mNetworkConnectionFragment;
    private MotorFragment mMotorFragment;
    private ProcessorFragment mProcessorFragment;
    private PWMFragment mPWMFragment;

    // Fragments du mode de test
    private OrderTestFragment mOrderTestFragment;
    private TestsFragment mTestsFragment;
    private PositionTestFragment mPositionTestFragment;

    // Fragments du mode pilotage manuel
    private OrderPilotFragment mOrderPilotFragment;
    private CommandPilotFragment mCommandPilotFragment;
    private PositionPilotFragment mPositionPilotFragment;

    // Fragment de l'historique
    private HistoryFragment mHistoryFragment;

    // Fragment du cartographe
    private CartographerFragment mCartographerFragment;

    // Attribut permettant de gérer le menu avec le NavigationDrawer
    NavigationView navigationView;
    Menu nav_Menu;

    // Attributs de communication
    private int myConnectionDelay;
    private int myTimerReachabilityDelay;

    // Attribut du pilotage manuel utile pour ne pas envoyer la donnée de commande en permanence
    public static String oldDirection = "";

    public enum IdScreen {
        START(new boolean[]{false, false, false, true, false}),TEST(new boolean[]{false, true, true, true, true}),PILOT(new boolean[]{true, false, false, true, true}),HISTORIAN(new boolean[]{true, false, false, true, true});
        private boolean tab[];
        IdScreen(boolean tab[]) {
            this.tab = tab;
        }
        public boolean[] getTab() {
            return tab;
        }
    }
    public static IdScreen idScreen;
    public enum Direction{
        LEFT (Protocol.argumentMoveLeft), RIGHT (Protocol.argumentMoveRight), FORWARD (Protocol.argumentMoveForward), BACKWARD (Protocol.argumentMoveBackward);/*, FORWARD_RIGHT, BACKWARD_RIGHT, BACKWARD_LEFT, FORWARD_LEFT*/
        private String protocol;
        Direction(String protocol){
            this.protocol = protocol;
        }
        public String getProtocol(){
            return protocol;
        }
    }
    private Direction myDirection;
    public Point myScenario;
    public boolean myBooleanVisible;
    private int myCPU;
    private int myRAM;
    private int myWheelsPower;
    private EditText endTestPosX;
    private EditText endTestPosY;

    // Méthodes de l'interface de callback de la classe ConnectionSingleton
    @Override
    public void onConnectSuccess() {
        runOnUiThread(new Runnable(){
            public void run() {
            showProgressBar(false, false);
            handleConnexionView();
            }
        });
    }

    @Override
    public void onConnectTimeoutException() {
        runOnUiThread(new Runnable(){
            public void run() {
            showProgressBar(false, true);
            handleConnexionView();
            }
        });
    }

    @Override
    public void onConnectIOException() {
        runOnUiThread(new Runnable(){
            public void run() {
                showProgressBar(false, false);
                Toast.makeText(GUIActivity.this, getString(R.string.ioException), Toast.LENGTH_LONG).show();
                handleConnexionView();
            }
        });
    }

    @Override
    public void onConnectionLost() {
        runOnUiThread(new Runnable(){
            public void run() {
                showMessageDialog(getString(R.string.connectionLostTitle), getString(R.string.connectionLostMessage));
                handleConnexionView();
            }
        });
    }
    // Fin des méthodes de l'interface de callback de la classe ConnectionSingleton

    // Méthodes propres à Android
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gui);

        idScreen = IdScreen.START;
        myConnectionDelay = 10;
        myTimerReachabilityDelay = 1;

        myScenario = new Point(0,0);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        nav_Menu = navigationView.getMenu();

        instanciateFragments();
        handleConnexionView();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.nav_test:
                if(ConnectionSingleton.getInstance().getConnectionState() == ConnectionSingleton.ConnectionState.CONNECTED) {
                    idScreen = IdScreen.TEST;
                    displayScreen(idScreen);
                }
                break;
            case R.id.nav_manual:
                if(ConnectionSingleton.getInstance().getConnectionState() == ConnectionSingleton.ConnectionState.CONNECTED) {
                    idScreen = IdScreen.PILOT;
                    displayScreen(idScreen);
                }
                break;
            case R.id.nav_historian:
                if(ConnectionSingleton.getInstance().getConnectionState() == ConnectionSingleton.ConnectionState.CONNECTED) {
                    idScreen = IdScreen.HISTORIAN;
                    displayScreen(idScreen);
                }
                break;
            case R.id.nav_disconnection:
                if(ConnectionSingleton.getInstance().getConnectionState() == ConnectionSingleton.ConnectionState.CONNECTED) {
                    showConfirmDialogConnection(getString(R.string.deconnectTitle), getString(R.string.deconnectMessageYes));
                }
                break;
            case R.id.nav_help:
                displayHelpScreen(idScreen);
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }
    // Fin des méthodes propres à Android

    /**
     * Affiche l’écran en fonction du paramètre passé. Cette
     * méthode gère l’état de la connection et le mode dans lequel on dirige Rob. Elle ferat
     * aussi appel à des dialogues de messages, annulables ou non pour prévenir en cas de
     * déconnexion voulues ou non ou en cas de calibration par exemple
     *
     *@param idScreen l'id de l'écran à afficher
     */
    private void displayScreen(IdScreen idScreen) {
        switch (idScreen) {
            case START:
                setTitle(getString(R.string.app_name));
                configureAndShowConnectionFragments();
                break;
            case TEST:
                setTitle(getString(R.string.testLayoutTitle));
                configureAndShowTestFragments();
                break;
            case PILOT:
                setTitle(getString(R.string.pilotLayoutTitle));
                configureAndShowPilotFragments();
                break;
            case HISTORIAN:
                setTitle(getString(R.string.historianLayoutTitle));
                configureAndShowHistoryFragment();
                break;
        }
        resetValues();
        setStartButtonVisible(true);
        handleMenuItem(idScreen.getTab());
    }

    /**
     * Gère la visibilité des items du menu en fonction de l'écran
     *
     * @param tab La visibilité des items du menu en fonction de l'écran
     */
    private void handleMenuItem(boolean tab[]) {
        nav_Menu.findItem(R.id.nav_test).setEnabled(tab[0]);
        nav_Menu.findItem(R.id.nav_historian).setEnabled(tab[1]);
        nav_Menu.findItem(R.id.nav_manual).setEnabled(tab[2]);
        nav_Menu.findItem(R.id.nav_help).setEnabled(tab[3]);
        nav_Menu.findItem(R.id.nav_disconnection).setEnabled(tab[4]);
    }

    /**
     * L’application prend en compte la destination
     * de que Rob devrat atteindre lors du test de positionnement
     *
     * @param position la position de destination de Rob
     */
    public void setPositionScenario(Point position) {
        myScenario = position;
    }

    /**
     L’application met en place les scénarios possibles
     à faire effectuer à Rob
     *
     *@param myScenario le nom du scénario
     */
    public void startScenario(Point myScenario) {
        Toast.makeText(this, getString(R.string.destination) + myScenario, Toast.LENGTH_SHORT).show();
        ModelSingleton.getInstance().getProxyCopilot().beginScenario(myScenario);
    }

    /**
     * Permet d’envoyer la direction que Rob doit
     * prendre lors du pilotage manuel
     *
     *@param direction la valeur de la direction
     */
    public void sendDirection(Direction direction) {
        String newDirection;
        if(!myBooleanVisible) {
            if(direction!=null){
                newDirection = direction.getProtocol();
                if(oldDirection != newDirection)
                    ModelSingleton.getInstance().getProxyPilot().setDirection(direction);
                oldDirection = direction.getProtocol();
                myDirection = direction;
            }else{
                newDirection = Protocol.argumentStop;
                if(oldDirection != newDirection)
                    ModelSingleton.getInstance().getProxyPilot().setDirection(direction);
                oldDirection = Protocol.argumentStop;
                myDirection = null;
            }
        }
    }

    /**
     * L’application remet les champs à zéro et place le robot au centre de la
     * grille. Elle informe que l’Utilisateur doit placer Rob au centre du Terrain
     */
    void calibrate() {
        setCurrentPosition(new Point(0,0));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(getString(R.string.calibrateTitle));
        builder.setPositiveButton(getString(R.string.validate), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(idScreen == IdScreen.TEST) startScenario(myScenario);
            }
        });
        builder.setMessage(getString(R.string.calibrateMessage));
        builder.show();
        ModelSingleton.getInstance().getCartographer().calibrate();
        mCartographerFragment.calibrate();
    }

    /**
     * L’utilisateur clique sur le bouton pour valider les données rentrées
     * précédement lors de la complétion des champs
     */
    public void validateTestData() {
        Point calculatedPosition = new Point();
        try {
            calculatedPosition = new Point(Integer.parseInt(endTestPosX.getText().toString()), Integer.parseInt(endTestPosY.getText().toString()));
        } catch (Exception e) {
            Log.d(TAG, "calculatedPosition null !\n" + e .getMessage());
            Toast.makeText(GUIActivity.this, getString(R.string.errorMeasuredPosition), Toast.LENGTH_LONG).show();
        }
        //float scopeError = (float) Math.sqrt(((calculatedPosition.x-myScenario.x)*(calculatedPosition.x-myScenario.x))+((calculatedPosition.y-myScenario.y)*(calculatedPosition.y-myScenario.y)));
        float scopeError = (float) Math.round(Math.sqrt(((calculatedPosition.x-myScenario.x)*(calculatedPosition.x-myScenario.x))+((calculatedPosition.y-myScenario.y)*(calculatedPosition.y-myScenario.y))));
        Date date = new Date();
        TestResults testResults = new TestResults(0, myScenario, calculatedPosition, scopeError, date);
        RecyclerViewAdapter recyclerAdapter = mHistoryFragment.getRecyclerViewAdapter();
        try {
            recyclerAdapter.addItem(testResults);
        } catch (Exception e) {
            Log.d(TAG, "recyclerAdapter.addItem(testResults) !\n" + e.getMessage());
            Toast.makeText(GUIActivity.this, getString(R.string.errorRecyclerAdaapter), Toast.LENGTH_LONG).show();
        }
        idScreen = IdScreen.HISTORIAN;
        displayScreen(idScreen);
    }

    /**
     Permet à GUIActivity d’afficher la consommation CPU de Rob
     *
     *@param cpu la valeur du cpu
     */
    public void setCPU(final int cpu) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mProcessorFragment != null) mProcessorFragment.setCpuPilot(cpu);
                else Log.e(TAG, "mProcessorFragment is null !\n");
            }
        });
        this.myCPU = cpu;
    }

    /**
     * Permet à GUIActivity d’afficher la consommation RAM de Rob
     *
     *@param ram la valeur de la ram
     */
    public void setRAM(final int ram) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mProcessorFragment != null) mProcessorFragment.setRamPilot(ram);
                else Log.e(TAG, "mProcessorFragment is null !\n");
            }
        });
        this.myRAM = ram;
    }

    /**
     * Permet à GUIActivity d’afficher la puissance sur les roues de Rob
     *
     * @param lW le rapport cyclique sur la roue gauche
     * @param rW le rapport cyclique sur la roue droite
     */
    public void setWheelsPower(final int lW, final int rW) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mMotorFragment != null ) {
                    mMotorFragment.setPwmLeft(lW);
                    mMotorFragment.setPwmRight(rW);
                }
                if(mPWMFragment != null) {
                    mPWMFragment.drawLines(lW, rW);
                }
                myWheelsPower = (lW + rW)/2;
            }
        });
    }

    /**
     * Affiche l’écran d’aide en fonction du paramètre
     * passé
     *
     * @param idScreen l'id de l'écran à afficher
     */
    public void displayHelpScreen(IdScreen idScreen) {
        switch(idScreen) {
            case START:
                showMessageDialog(getString(R.string.helpConnectionTitle), getString(R.string.helpConnectionMessage));
                break;
            case TEST:
                showMessageDialog(getString(R.string.helpTestTitle), getString(R.string.helpTestMessage));
                break;
            case PILOT:
                showMessageDialog(getString(R.string.helpPilotTitle), getString(R.string.helpPilotMessage));
                break;
            case HISTORIAN:
                showMessageDialog(getString(R.string.helpHistorianTitle), getString(R.string.helpHistorianMessage));
                break;
        }
    }

    /**
     * Permet de rendre cliquable ou non le bouton
     * de démarrage d’un test ou d’un pilotage manuel
     *
     * @param visibility la visibilité du bouton, c'est aussi le fait qu'il soit cliquable ou non
     */
    public void setStartButtonVisible(boolean visibility) {
        myBooleanVisible = visibility;
        if(mTestsFragment.getSpinnerTests() != null) {
            if(visibility) mTestsFragment.getSpinnerTests().setEnabled(true);
            else mTestsFragment.getSpinnerTests().setEnabled(false);
        }
    }

    /**
     * Méthode permettant de se déconnecter à RobSoft
     */
    public void disconnect() {
        try {
            idScreen = IdScreen.START;
            ConnectionSingleton.getInstance().disconnect();
            handleConnexionView();
            Log.d(TAG, "Success disconnecting !\n");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "Fail disconnecting !\n");
        }
        Log.d(TAG, "Connection state : " + ConnectionSingleton.getInstance().getConnectionState() + "\n");
    }

    /**
     * Méthode permettant de gérer la vue IHM en fonction de
     * l’état de la connexion
     */
    public void handleConnexionView() {
        if(ConnectionSingleton.getInstance().getConnectionState() == ConnectionSingleton.ConnectionState.DISCONNECTED) {
            idScreen = IdScreen.START;
            displayScreen(idScreen);
            (findViewById(R.id.progressBar_connexion)).setVisibility(View.GONE);
        } else if (ConnectionSingleton.getInstance().getConnectionState() == ConnectionSingleton.ConnectionState.CONNECTING) {
            setTitle(getString(R.string.connecting));
            (findViewById(R.id.progressBar_connexion)).setVisibility(View.VISIBLE);
        } else {
            idScreen = IdScreen.TEST;
            displayScreen(idScreen);
            (findViewById(R.id.progressBar_connexion)).setVisibility(View.GONE);
        }
        Log.d(TAG, "IdScreen : " + idScreen);
    }

    /**
     * Méthode utilisée pour vérifier la va-
     * lidité des champs de connexion tels que le format de l’IPv4 ou le numéro de port
     * (compris entre 1024 et 49151)
     *
     * @param ip L'adresse IPv4
     * @param port Le numéro de port
     * @return boolean true if good, or false if not
     */
    public boolean validateConnectionFields(String ip, int port) {
        return (new IPAddressValidator().validate(ip) && port <= 65535 && port >= 0);
    }

    /**
     * Méthode permettant de se connecter à RobSoft
     *
     * @param ip L'addresse IPv4
     * @param port Le numéro de port
     */
    public void connect(String ip, int port) {
        if(checkWifi()) {
            if(validateConnectionFields(ip, port)) {
                ConnectionSingleton.getInstance().setConnectionState(ConnectionSingleton.ConnectionState.CONNECTING);
                handleConnexionView();
                Log.d(TAG, "Connecting...\n");
                Log.d(TAG, "Connection state : " + ConnectionSingleton.getInstance().getConnectionState() + "\n");
                ConnectionSingleton.getInstance().intitSingletonConnection(this, myConnectionDelay, myTimerReachabilityDelay);
                ConnectionSingleton.getInstance().connect(ip, port);
            } else {
                showMessageDialog(getString(R.string.connectionFieldsErrorTitle), getString(R.string.connectionFieldsErrorMessage));
            }
        } else {
            Toast.makeText(GUIActivity.this, getString(R.string.errorWiFi), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Permet d'instancier tous les fragments
     */
    private void instanciateFragments() {
        connectionFragment = new ConnectionFragment();
        mNetworkConnectionFragment = new NetworkConnectionFragment();
        mMotorFragment = new MotorFragment();
        mProcessorFragment = new ProcessorFragment();
        mPWMFragment = new PWMFragment();
        mOrderTestFragment = new OrderTestFragment();
        mTestsFragment = new TestsFragment();
        mPositionTestFragment = new PositionTestFragment();
        mOrderPilotFragment = new OrderPilotFragment();
        mCommandPilotFragment = new CommandPilotFragment();
        mPositionPilotFragment = new PositionPilotFragment();
        mCartographerFragment = new CartographerFragment();
        mHistoryFragment = new HistoryFragment();
    }

    /**
     * Permet d'afficher la vue de connexion
     */
    private void configureAndShowConnectionFragments() {
        setTitle(getString(R.string.app_name));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(fragmentManager.findFragmentById(R.id.fragment_container) != connectionFragment) {
            fragmentManager.popBackStackImmediate();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, connectionFragment).addToBackStack(null).commit();
        }
        transaction.hide(mNetworkConnectionFragment);
        transaction.hide(mMotorFragment);
        transaction.hide(mProcessorFragment);
        transaction.hide(mPWMFragment);
        transaction.hide(mOrderTestFragment);
        transaction.hide(mTestsFragment);
        transaction.hide(mPositionTestFragment);
        transaction.hide(mOrderPilotFragment);
        transaction.hide(mCommandPilotFragment);
        transaction.hide(mPositionPilotFragment);
        transaction.hide(mCartographerFragment);
        transaction.show(connectionFragment);
        transaction.hide(mHistoryFragment);
        transaction.addToBackStack(null);
        transaction.commit();
        findViewById(R.id.frame_layout_cartographer).setBackgroundColor(0);
    }

    /**
     * Permet d'afficher la vue de test
     */
    private void configureAndShowTestFragments() {
        setTitle(getString(R.string.testLayoutTitle));
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.show(mNetworkConnectionFragment);
        transaction.show(mMotorFragment);
        transaction.show(mProcessorFragment);
        transaction.show(mPWMFragment);
        transaction.show(mOrderTestFragment);
        transaction.show(mTestsFragment);
        transaction.show(mPositionTestFragment);
        transaction.show(mOrderPilotFragment);
        transaction.show(mCommandPilotFragment);
        transaction.show(mPositionPilotFragment);
        transaction.show(mCartographerFragment);
        transaction.replace(R.id.frame_layout_connection, mNetworkConnectionFragment)
                .replace(R.id.frame_layout_motor,mMotorFragment)
                .replace(R.id.frame_layout_processor,mProcessorFragment)
                .replace(R.id.frame_layout_order,mOrderTestFragment)
                .replace(R.id.frame_layout_tests,mTestsFragment)
                .replace(R.id.frame_layout_position,mPositionTestFragment)
                .replace(R.id.frame_layout_pwm,mPWMFragment)
                .replace(R.id.frame_layout_cartographer, mCartographerFragment);
        transaction.hide(connectionFragment);
        transaction.hide(mHistoryFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Permet d'afficher la vue de l'historique
     */
    private void configureAndShowHistoryFragment() {
        setTitle(getString(R.string.historianLayoutTitle));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(fragmentManager.findFragmentById(R.id.fragment_container) != mHistoryFragment) {
            fragmentManager.popBackStackImmediate();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, mHistoryFragment).addToBackStack(null).commit();
        }
        transaction.show(mHistoryFragment);
        transaction.hide(mNetworkConnectionFragment);
        transaction.hide(mMotorFragment);
        transaction.hide(mProcessorFragment);
        transaction.hide(mPWMFragment);
        transaction.hide(mOrderTestFragment);
        transaction.hide(mTestsFragment);
        transaction.hide(mPositionTestFragment);
        transaction.hide(mOrderPilotFragment);
        transaction.hide(mCommandPilotFragment);
        transaction.hide(mPositionPilotFragment);
        transaction.hide(mCartographerFragment);
        transaction.addToBackStack(null);
        transaction.commit();
        findViewById(R.id.frame_layout_cartographer).setBackgroundColor(0);
    }

    /**
     * Permet d'afficher la vue de pilotage manuel
     */
    private void configureAndShowPilotFragments() {
        setTitle(getString(R.string.pilotLayoutTitle));
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.show(mNetworkConnectionFragment);
        transaction.show(mMotorFragment);
        transaction.show(mProcessorFragment);
        transaction.show(mPWMFragment);
        transaction.show(mOrderTestFragment);
        transaction.show(mTestsFragment);
        transaction.show(mPositionTestFragment);
        transaction.show(mOrderPilotFragment);
        transaction.show(mCommandPilotFragment);
        transaction.show(mPositionPilotFragment);
        transaction.show(mCartographerFragment);
        transaction.replace(R.id.frame_layout_connection, mNetworkConnectionFragment)
                .replace(R.id.frame_layout_motor,mMotorFragment)
                .replace(R.id.frame_layout_processor,mProcessorFragment)
                .replace(R.id.frame_layout_order,mOrderPilotFragment)
                .replace(R.id.frame_layout_tests,mCommandPilotFragment)
                .replace(R.id.frame_layout_position, mPositionPilotFragment)
                .replace(R.id.frame_layout_pwm,mPWMFragment)
                .replace(R.id.frame_layout_cartographer, mCartographerFragment);
        transaction.hide(connectionFragment);
        transaction.hide(mHistoryFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Méthode permettant de vérifier si on est connecté en Wi-Fi
     *
     * @return L'état de la connexion au Wi-Fi
     */
    public boolean checkWifi() {
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfoWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return networkInfoWifi != null && networkInfoWifi.isConnected();
    }

    /**
     * Méthode permettant
     * de gérer la visibilité du cercle de chargemetn lors de la connexion à RobSoft
     *
     * @param visibility La visibilité de la barre de progression
     * @param endOfDelay Le booléen indiquant si c'est la fin du délai du Timeout de connexion
     */
    public void showProgressBar(final boolean visibility, final boolean endOfDelay) {
        runOnUiThread(new Runnable(){
            public void run() {
                if(visibility) {
                    (findViewById(R.id.progressBar_connexion)).setVisibility(View.VISIBLE);
                    connectionFragment.setButtonConnect(false);
                } else if(endOfDelay) {
                    (findViewById(R.id.progressBar_connexion)).setVisibility(View.GONE);
                    connectionFragment.setButtonConnect(true);
                    if(myConnectionDelay >= 1) showMessageDialog(getString(R.string.delayExpiredTitle), getString(R.string.delayExpiredMessage));
                } else {
                    (findViewById(R.id.progressBar_connexion)).setVisibility(View.GONE);
                    connectionFragment.setButtonConnect(true);
                }
            }
        });
    }

    /**
     * Affiche une popup, méthode
     * ajoutée par simplicité et non présente dans la conception générale
     *
     * @param title Le titre de la popup
     * @param Message Le message de la popup
     */
    public void showMessageDialog(String title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(title);
        builder.setPositiveButton(getString(R.string.validate), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setMessage(Message);
        builder.show();
    }

    /**
     * Affiche une popup
     * avec deux choix pour se déconnecter, méthode ajoutée par simplicité et non présente
     * dans la conception générale
     *
     * @param title Le titre de la popup
     * @param message Le message de la popup
     */
    public void showConfirmDialogConnection(String title, String message){
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.validate), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        disconnect();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .show();
    }

    /**
     * Affiche une popup avec deux choix, un
     * pour valider et enregistrer la donnée de test et l’autre pour annuler, méthode ajoutée
     * par simplicité et non présente dans la conception générale
     *
     * @param title Le titre de la popup
     */
    public void showConfirmDialogEndTest(final String title){
        LayoutInflater factory = LayoutInflater.from(this);
        final View alertDialogView = factory.inflate(R.layout.alert_dialog_end_test, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                builder.setTitle(title)
                        .setView(alertDialogView)
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.validate), new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                endTestPosX = alertDialogView.findViewById(R.id.endTestPosX);
                                endTestPosY = alertDialogView.findViewById(R.id.endTestPosY);
                                validateTestData();
                                if(mOrderTestFragment != null) {
                                    mOrderTestFragment.buttonStart.setEnabled(true);
                                    mOrderTestFragment.buttonStop.setEnabled(false);
                                    mOrderTestFragment.buttonCancel.setEnabled(false);
                                } else Log.d(TAG, "mOrderTestFragment is null !");
                            }
                        })
                        .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(mOrderTestFragment != null) {
                                    mOrderTestFragment.buttonStart.setEnabled(true);
                                    mOrderTestFragment.buttonStop.setEnabled(false);
                                    mOrderTestFragment.buttonCancel.setEnabled(false);
                                } else Log.d(TAG, "mOrderTestFragment is null !");
                            }
                        })
                        .show();
            }
        });
    }

    /**
     * Permet d'afficher la position calculée par RobSoft
     *
     * @param position le position calculée
     */
    public void setCurrentPosition(final Point position) {
        final Cartographer cartographer = ModelSingleton.getInstance().getCartographer();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(position.x != 10 && position.y != 10) {
                    if(idScreen == IdScreen.PILOT && mPositionPilotFragment !=null) {
                        mPositionPilotFragment.setPosition(position);
                        mCartographerFragment.setCurrentPosition(position);
                        cartographer.addToMapData(position);
                        mCartographerFragment.setMapData(cartographer.getMapData());
                    }
                    else Log.e(TAG, "mPositionPilotFragment is null !\n");
                    if(idScreen == IdScreen.TEST && mPositionTestFragment != null) {
                        mPositionTestFragment.setPosition(position);
                        cartographer.addToMapData(position);
                        mCartographerFragment.setCurrentPosition(position);
                        mCartographerFragment.setMapData(cartographer.getMapData());
                    }
                    else Log.e(TAG, "mPositionTestFragment is null !\n");
                    Log.d(TAG, "setCurrentPosition");
                } else {
                    if(idScreen == IdScreen.PILOT && mPositionPilotFragment !=null) {
                        mPositionPilotFragment.setError(getString(R.string.errorPosition));
                    }
                    else Log.e(TAG, "mPositionPilotFragment is null !\n");
                    if(idScreen == IdScreen.TEST && mPositionTestFragment != null) {
                        mPositionTestFragment.setError(getString(R.string.errorPosition));
                    }
                    else Log.e(TAG, "mPositionTestFragment is null !\n");
                    Log.d(TAG, "setCurrentPosition");
                }
            }
        });
        cartographer.setCurrentPosition(position);
    }

    /**
     * Permet d'afficher la position souhaitée par RobSoft
     *
     * @param position La position calculée
     */
    public void setWishedPosition(final Point position) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(idScreen == IdScreen.TEST && mPositionTestFragment != null) mPositionTestFragment.setPositionWished(position);
                else Log.e(TAG, "mPositionTestFragment is null !\n");
                if(mCartographerFragment != null) mCartographerFragment.setWishedPositionFromSpinner(position);
                else Log.e(TAG, "mCartographerFragment is null !\n");
                Log.d(TAG, "setWishedPosition");
            }
        });
        ModelSingleton.getInstance().getCartographer().setDestinationPosition(position);
    }

    /**
     * Permet d'afficher la position souhaitée par RobSoft
     *
     * @param position La position calculée
     */
    public void setWishedPositionFromCartographer(final Point position) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(idScreen == IdScreen.TEST && mPositionTestFragment != null) mPositionTestFragment.setPositionWished(position);
                else Log.e(TAG, "mPositionTestFragment is null !\n");
                Log.d(TAG, "setWishedPosition");
            }
        });
        ModelSingleton.getInstance().getCartographer().setDestinationPosition(position);
    }

    /**
     * Permet de remmettre à zéro les champs
     */
    public void resetValues() {
        try {
            setCPU(0);
            setRAM(0);
            setWheelsPower(0,0);
            setCurrentPosition(new Point(0,0));
            setWishedPosition(new Point(0,0));
            setStartButtonVisible(true);
            if(mCartographerFragment != null) {
                mCartographerFragment.calibrate();
                ModelSingleton.getInstance().getCartographer().calibrate();
                Log.d(TAG, "mCartographerFragment : calibrate() method called !");
            }
        } catch (Exception e) {
            Log.d(TAG, "Exception resetValues");
        }
    }

} // End of class