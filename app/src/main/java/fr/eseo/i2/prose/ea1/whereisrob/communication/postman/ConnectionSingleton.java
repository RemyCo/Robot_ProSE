/**
 * @file ConnectionSingleton.java
 *
 * @brief La classe ConnectionSingleton permet de communiquer avec un serveur via le protocol
 * TCP/IP. Elle contient deux classes nichées étant des AsyncTasks permettant de connecter
 * la socket et d’écrire dessus.
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
package fr.eseo.i2.prose.ea1.whereisrob.communication.postman;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Timer;
import java.util.TimerTask;
import fr.eseo.i2.prose.ea1.whereisrob.communication.DispatcherAndroid;
import fr.eseo.i2.prose.ea1.whereisrob.communication.Protocol;
import fr.eseo.i2.prose.ea1.whereisrob.gui.ConnectionFragment;

/**
 * La classe ConnectionSingleton permet de communiquer avec un serveur via le protocol
 * TCP/IP. Elle contient deux classes nichées étant des AsyncTasks permettant de connecter
 * la socket et d’écrire dessus.
 */
public class ConnectionSingleton {

    private static final String TAG = "Debug";

    public interface ICallbackConnection {
        void onConnectSuccess();
        void onConnectTimeoutException();
        void onConnectIOException();
        void onConnectionLost();
    }

    private ICallbackConnection iCallbackConnection;
    public enum ConnectionState {CONNECTED, DISCONNECTED, CONNECTING};
    private ConnectionState connectionState = ConnectionState.DISCONNECTED;
    private Socket socket;
    private InetSocketAddress inetSocketAddress;
    private BufferedWriter output;
    private BufferedReader input;
    private int connectionDelay;
    private Timer timerReachability;
    private int timerReachabilityDelay;
    private DataReceiver dataReceiver;
    private DispatcherAndroid dispatcher;

    /**
     * Constructeur de la classe
     */
    private ConnectionSingleton() {

    }

    /**
     * Initialise le singleton
     *
     * @param activity Activity
     */
    public void intitSingletonConnection(Activity activity, int connectionDelay, int timerReachabilityDelay) {
        this.iCallbackConnection = (ICallbackConnection) activity;
        this.connectionDelay = connectionDelay;
        this.timerReachabilityDelay = timerReachabilityDelay;
        this.dispatcher = new DispatcherAndroid(activity);
        this.dataReceiver = new DataReceiver(dispatcher);

        this.socket = new Socket();
        this.timerReachability = new Timer();
    }

    /**
     * Instance du singleton
     */
    private static ConnectionSingleton connectionSingletonInstance = new ConnectionSingleton();

    /**
     * Accesseur du singleton
     *
     * @return L'instance du singleton
     */
    public static ConnectionSingleton getInstance()
    {
        return connectionSingletonInstance;
    }

    /**
     * Accesseur de l'état de la connexion
     *
     * @return Connection state
     */
    public ConnectionState getConnectionState() {
        return connectionState;
    }

    /**
     * Modificateur de l'état de la connexion
     *
     * @param connectionState L'état de la connexion
     */
    public void setConnectionState(ConnectionState connectionState) {
        this.connectionState = connectionState;
    }

    /**
     * Retourne l'état de la socket
     *
     * @return L'état de la socket
     */
    public boolean getSocketState() {
        return (socket.isConnected() && !socket.isClosed());
    }

    /**
     * Retourne l'accessibilité de l'hôte
     *
     * @return L'accessibilité de l'hôte
     * @throws IOException
     */
    public boolean getHostReachability() throws IOException {
        boolean response;
        InetAddress inetAddress = InetAddress.getByName(ConnectionFragment.myIp);
        response = inetAddress.isReachable(1000);
        if(response == false) {
            disconnect();
            iCallbackConnection.onConnectionLost();
            Log.d(TAG, "Server disconnected by violent shutdown !\n");
            Log.d(TAG, "Connection state : " + getConnectionState() + "\n");
            Log.d(TAG, "Host " + ConnectionFragment.myIp + " is reachable : " + response + "\n");
        }
        return response;
    }

    /**
     * Permet d'exécuter l'AsyncTask pour connecter la socket
     *
     * @param ip L'address IPv4
     * @param port Le numéro de port
     */
    public void connect(String ip, int port) {
        inetSocketAddress = new InetSocketAddress(ip, port);
        new ConnectAsyncTask().execute(inetSocketAddress);
    }

    /**
     * Permet de fermer la socket pour se déconnecter
     *
     * @throws IOException
     */
    public void disconnect() throws IOException {
        setConnectionState(ConnectionState.DISCONNECTED);
        if(socket != null) socket.close();
        timerReachability.cancel();
        Log.d(TAG, "Timer canceled !\n");
    }

    /**
     * Permet d'exécuter l'AsyncTask pour écrire sur la socket
     *
     * @param data Message à envoyer
     */
    public void sendData(String data) {
        new SendDataAsyncTask().execute(data);
        Log.d(TAG, "Message sent (in SendDataAsyncTask class): " + data + "\n");
    }

    /**
     * Permet de lire sur la socket
     *
     * @return Message read
     */
    public String readData() {
        String message = "";
        try {
            if(socket != null && getSocketState() && input != null) {
                message = input.readLine();
                Log.d(TAG, "Message received (in Connection class, readData()): " + message + "\n");
                if (message == null) {
                    try {
                        throw new Exception();
                    } catch (Exception e1) {
                        disconnect();
                        iCallbackConnection.onConnectionLost(/*new Exception()*/);
                        Log.d(TAG, "Server disconnected !\n");
                        Log.d(TAG, "Connection state : " + connectionState + "\n");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }

    /**
     * Lance le thread pour lire les données envoyées par RobSoft en permanence
     */
    public void startReading() {
        dataReceiver.start();
    }

    /**
     * AsyncTask qui connecte la socket
     */
    private class ConnectAsyncTask extends AsyncTask<InetSocketAddress, Void, Void> {

        private Exception exception = null;
        private boolean ioException;

        @Override
        protected Void doInBackground(InetSocketAddress... InetSocketAddresses) {
            try {
                socket.connect(InetSocketAddresses[0], connectionDelay * 1000);
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                Log.d(TAG, "Success connecting to server with AsyncTask !\n");
                setConnectionState(ConnectionState.CONNECTED);
                startReading();
                ioException = false;
            }  catch (SocketTimeoutException e1) {
                e1.printStackTrace();
                this.exception = e1;
                Log.d(TAG, "Timeout exception, delay expired !\n" + e1.getMessage());
                setConnectionState(ConnectionState.DISCONNECTED);
                ioException = false;
            } catch (IOException e) {
                e.printStackTrace();
                this.exception = e;
                Log.d(TAG, "Fail connecting with AsyncTask !\n" + e.getMessage());
                setConnectionState(ConnectionState.DISCONNECTED);
                ioException = true;
            }

            Log.d(TAG, "Connection state : " + connectionState + "\n");
            return null;
        }

        class TimerNetwork extends TimerTask {

            @Override
            public void run() {
                try {
                    getHostReachability();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } // End of class

        @Override
        protected void onPostExecute(Void aVoid) {
            if (this.exception == null) {
                iCallbackConnection.onConnectSuccess();    // callback connection ok
                timerReachability.schedule(new ConnectAsyncTask.TimerNetwork(), 0, timerReachabilityDelay * 1000); // launch timer
                Log.d(TAG, "Timer launched !\n");
            } else {
                if(ioException == false) iCallbackConnection.onConnectTimeoutException();
                else iCallbackConnection.onConnectIOException();
            }
        }

    } // End of class

    /**
     * AsyncTask permettant d'écrire sur la socket pour envoyer des données
     */
    private class SendDataAsyncTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... data) {
            if (socket != null) {
                try {
                    output.write(data[0]);
                    output.newLine();
                    output.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

    } // End of class

} // End of class
// http://thecodersbreakfast.net/index.php?post/2008/02/25/26-de-la-bonne-implementation-du-singleton-en-java