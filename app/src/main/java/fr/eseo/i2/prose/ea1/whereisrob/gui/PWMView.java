/**
 * @file PWMView.java
 *
 * @brief La classe PWMView permet d'afficher les flèches des PWM sur les moteurs de Rob
 *
 * @author Timothée GIRARD, Charly JONCHERAY
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
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * La classe PWMView permet d'afficher les flèches des PWM sur les moteurs de Rob
 */
public class PWMView extends View {

    private Paint paint = new Paint();
    private int rightOffset = 125;
    private int leftOffset = -125;
    private int middleOffset = 18;
    private int arrowLeft = 0;
    private int arrowRight = 0;
    private boolean pwmLeftPos = true;
    private boolean isPwmLeftNull = true;
    private boolean pwmRightPos = true;
    private boolean isPwmRightNull = true;

    private void init() {
        paint.setColor(Color.DKGRAY);
        paint.setStrokeWidth(5);
    }

    public PWMView(Context context) {
        super(context);
        init();
    }

    public PWMView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PWMView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight() + middleOffset;
        paint.setColor(Color.DKGRAY);
        int meters = 8;

        canvas.drawLine(width/2 + rightOffset,height/2 ,width/2 + rightOffset,height/2 + arrowRight, paint);
        canvas.drawLine(width/2 + leftOffset,height/2 ,width/2 + leftOffset,height/2 + arrowLeft, paint);

        if(!isPwmRightNull) {
            if(pwmRightPos == false) {
                canvas.drawLine(width/2 + rightOffset,height/2 + arrowRight ,width/2 + 10 + rightOffset,height/2 + arrowRight + 10, paint);
                canvas.drawLine(width/2 + rightOffset,height/2 + arrowRight ,width/2 -10 + rightOffset,height/2 + arrowRight + 10, paint);
            }
            if(pwmRightPos == true) {
                canvas.drawLine(width/2 + rightOffset,height/2 + arrowRight ,width/2 + 10 + rightOffset,height/2 + arrowRight - 10, paint);
                canvas.drawLine(width/2 + rightOffset,height/2 + arrowRight ,width/2 -10 + rightOffset,height/2 + arrowRight - 10, paint);
            }
        }

        if(!isPwmLeftNull) {
            if(pwmLeftPos == false) {
                canvas.drawLine(width/2 + leftOffset,height/2 + arrowLeft ,width/2 + 10 + leftOffset,height/2 + arrowLeft + 10, paint);
                canvas.drawLine(width/2 + leftOffset,height/2 + arrowLeft ,width/2 -10 + leftOffset,height/2 + arrowLeft + 10, paint);
            }
            if(pwmLeftPos == true) {
                canvas.drawLine(width/2 + leftOffset,height/2 + arrowLeft ,width/2 + 10 + leftOffset,height/2 + arrowLeft - 10, paint);
                canvas.drawLine(width/2 + leftOffset,height/2 + arrowLeft ,width/2 -10 + leftOffset,height/2 + arrowLeft - 10, paint);
            }
        }
    }

    /**
     * Méthode appelant le dessin des lignes sur
     * le fragment
     *
     * @param lW La puissance PWM sur le moteur gauche de Rob
     * @param rW La puissance PWM sur le moteur droit de Rob
     */
    public void drawlines(int lW, int rW) {
        arrowLeft = lW;
        arrowRight = rW;
        if(lW>0) {
            pwmLeftPos = true;
            isPwmLeftNull = false;
        } else {
            pwmLeftPos = false;
            isPwmLeftNull = false;
        }
        if(rW>0) {
            pwmRightPos = true;
            isPwmRightNull = false;
        } else {
            pwmRightPos = false;
            isPwmRightNull = false;
        }
        if(lW==0) isPwmLeftNull = true; else isPwmLeftNull = false;
        if(rW==0) isPwmRightNull = true; else isPwmRightNull = false;
        invalidate();
    }

}   // End of class
