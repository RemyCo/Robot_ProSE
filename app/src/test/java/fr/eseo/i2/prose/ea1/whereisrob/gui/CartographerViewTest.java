package fr.eseo.i2.prose.ea1.whereisrob.gui;

import android.content.Context;
import android.graphics.Point;

import static org.mockito.Mockito.mock;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class CartographerViewTest {

    private Context context = mock(Context.class);
    private CartographerView cartographerView = new CartographerView(context);

    @Test
    public void testCalibrateToCanvas(){
        CartographerView cartographerView2 = Mockito.spy(cartographerView);
        Mockito.when(cartographerView2.getWidth()).thenReturn(900);
        Mockito.when(cartographerView2.getHeight()).thenReturn(900);
        Point myPoint = cartographerView2.calibrateToCanvas(100, 100);
        Assert.assertEquals(562, myPoint.x);
    }
}
