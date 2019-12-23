/*
 * @file CartographerView.java
 *
 * @brief La classe CartographerView est la classe qui permet de gérer la Vue de Cartographer.
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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.whereisrob.whereisrob.R;

import java.util.ArrayList;

/**
 *
 */
public class CartographerView extends View {

    private Paint paint = new Paint();
    private Bitmap robot;
    private Point bluePoint = new Point(0,0);
    private Point robotPoint = new Point(0,0);
    private int meters = 8;
    private ArrayList<Point> mapData = new ArrayList<>();

    /**
     *
     */
    private void init() {
        this.robot = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.robot);
        this.robot = Bitmap.createScaledBitmap(robot, 120, 90, true);
    }

    public CartographerView(Context context) {
        super(context);
        init();
    }

    public CartographerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CartographerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     *
     * @param canvas the canvas where we are drawing
     */
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        this.paint.setColor(Color.DKGRAY);
        int pasH = height/meters;
        int pasW = width/meters;


        // For creating the grid
        for (int meter = 0; meter <= meters; meter++){
            if (meter == 4){
                this.paint.setStrokeWidth(4);
            } else {
                this.paint.setStrokeWidth(2);
            }
            canvas.drawLine(0, meter * pasH, width, meter * pasH, paint);
            canvas.drawLine(meter * pasW, 0, meter * pasW, height, paint);
        }



        //For putting dots and lines where the Robot is passing
        for (Point p : mapData){
                Point newP = calibrateToCanvas(p.x, p.y);
                canvas.drawCircle(newP.x, newP.y, 7, paint);
                this.paint.setStrokeWidth(3);
                if (mapData.indexOf(p) != 0){
                    Point otherP = calibrateToCanvas(mapData.get(mapData.indexOf(p)-1).x, mapData.get(mapData.indexOf(p)-1).y);
                    canvas.drawLine(otherP.x, otherP.y, newP.x, newP.y, paint);
                } else {
                    Point originP = calibrateToCanvas(0, 0);
                    canvas.drawLine(originP.x, originP.y, newP.x, newP.y, paint);
                }
        }

        // For making a red dot on the middle (Where Rob is starting)
        this.paint.setColor(Color.RED);
        canvas.drawCircle((float)width/2, (float)height/2, 12, paint);

        // For moving the robot
        canvas.drawBitmap(robot, robotPoint.x, robotPoint.y, paint);


        // For making a blue dot on touch
        this.paint.setColor(Color.BLUE);
        if(GUIActivity.idScreen == GUIActivity.IdScreen.TEST)canvas.drawCircle(bluePoint.x, bluePoint.y, 15, paint);

    }

    void changeView(){
        invalidate();
    }

    /**
     *
     * @param event : The event which appends in CartographerFragment
     * @return true, to make sure the BluePoint has change
     */
    boolean changeBluePoint(MotionEvent event){
        this.bluePoint.x = (int)event.getX();
        this.bluePoint.y = (int)event.getY();
        invalidate();
        return true;
    }

    /**
     * This method set Rob to the middle of the screen
     */
    void calibrate(){
        changeView();
    }

    /**
     * This method is used to convert a point of the wished scaled to the real scaled
     * @param x : integer which represent the value on the x axis
     * @param y : integer which represent the value on the x axis
     * @return a point, representing the point scaling to the canvas
     */
    Point calibrateToXAndY(int x, int y){
        int newX = x * (meters * 100) / getWidth() - 400;
        int newY = - (y * (meters * 100) / getHeight() - 400);
        return new Point(newX, newY);
    }

    /**
     * This method is used to convert a point of the real scaled to the wished scaled
     * @param x : integer which represent the value on the x axis
     * @param y : integer which represent the value on the y axis
     * @return a point, representing the point scaling to the canvas
     */
    Point calibrateToCanvas(int x,int y){
        int newX = (x + 400) * getWidth() / (meters * 100);
        int newY = ((-y) + 400) * getHeight() / (meters * 100);
        return new Point(newX, newY);
    }

    /**
     *
     * @param x : integer which represent the value on the x axis
     * @param y : integer which represent the value on the y axis
     * @return a point, representing the point of the middle of Rob scaling to the canvas
     */
    Point calibrateRobToCanvas(int x,int y){
        Point point = calibrateToCanvas(x,y);
        int newX = point.x - robot.getWidth()/2;
        int newY = point.y - robot.getHeight()/2;
        return new Point(newX, newY);
    }

    /**
     * This method is used to set the position of Rob to robotPoint
     * @param robotPoint : the position of the robot
     */
    void setCurrentPosition(Point robotPoint){
        robotPoint = calibrateRobToCanvas(robotPoint.x, robotPoint.y);
        this.robotPoint = robotPoint;
        invalidate();
    }

    /**
     * This method is used to change the position of the blue dot when a scenario is chosen.
     * @param position : position of the chosen scenario
     */
    public void setWishedPositionFromSpinner(Point position) {
        this.bluePoint.x = position.x;
        this.bluePoint.y = position.y;
        invalidate();
    }

    public Point setBluePointToIntersections(MotionEvent event) {
        this.bluePoint.x = Math.round(event.getX()/((float)getWidth()/meters)) * (getWidth()/meters);
        this.bluePoint.y = Math.round(event.getY()/((float)getHeight()/meters)) * (getHeight()/meters);
        invalidate();
        return new Point(this.bluePoint.x, this.bluePoint.y);
    }

    public void setMapData(ArrayList<Point> mapData) {
        this.mapData = mapData;
        invalidate();
    }
}
