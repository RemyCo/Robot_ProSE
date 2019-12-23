/**
 * @file DispatcherAndroidTest.java
 *
 * @brief La classe de test qui permet de vérifier la classe DispatcherAndroid
 *
 * @author Matthias Pasquier
 *
 * @copyright 2019 ProseA1
 *
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

package fr.eseo.i2.prose.ea1.whereisrob.communication;

import org.junit.Test;
import fr.eseo.i2.prose.ea1.whereisrob.gui.GUIActivity;
import static org.mockito.Mockito.verify;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import android.graphics.Point;


/**
 * La classe permettant de tester le dispatcher Android
 */
public class DispatcherAndroidTest {

    GUIActivity guiActivity = Mockito.mock(GUIActivity.class);

    DispatcherAndroid dispatcher = new DispatcherAndroid(guiActivity);

    @Test
    public void testDispatchCpu(){
        dispatcher.dispatch("cpu:10");
        verify(guiActivity).setCPU(ArgumentMatchers.eq(10));
    }

    @Test
    public void testDispatchRam(){
        dispatcher.dispatch("ram:37");
        verify(guiActivity).setRAM(ArgumentMatchers.eq(37));
    }

    @Test
    public void testDispatchPwm(){
        dispatcher.dispatch("lW:53;rW:-17");
        verify(guiActivity).setWheelsPower(ArgumentMatchers.eq(53), ArgumentMatchers.eq(-17));
    }

    @Test
    public void testDispatchTestEnded(){
        dispatcher.dispatch("test:ended");
        verify(guiActivity).showConfirmDialogEndTest(ArgumentMatchers.eq("Le test est terminé"));
    }

    @Test
    public void testDispatchPosition(){
        dispatcher.dispatch("pX:100;pY:200");
        verify(guiActivity).setCurrentPosition(ArgumentMatchers.isA(Point.class));
    }
}
