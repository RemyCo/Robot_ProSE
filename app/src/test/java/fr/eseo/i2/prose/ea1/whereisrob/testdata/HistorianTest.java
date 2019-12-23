package fr.eseo.i2.prose.ea1.whereisrob.testdata;


import android.content.Context;
import android.graphics.Point;

import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.eseo.i2.prose.ea1.whereisrob.testdata.Historian;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.junit.Assert.assertTrue;



/**
 * La classe permettant le test de la classe Historian
 */

public class HistorianTest {

    public Context mContext;
    public Historian mHistorian;
    Point mRealPosition;
    Point mCalculatedPosition;
    public long idTestResult;
    public Float scopeError;
    public Date date;

    public TestResults mTestResults = new TestResults(idTestResult,mRealPosition,mCalculatedPosition,scopeError,date);


    @Before
    public void setUp() {
        mContext = mock(Context.class);
        mHistorian = new Historian(mContext);
        idTestResult = 0;
        scopeError = 0.0f;
        date = new Date();
        mRealPosition = new Point(3,3);
        mCalculatedPosition = new Point(3,3);
    }

    /**
     * Test pour savoir si le test Results est bien ajouté à la base de données
     */
    @Test
    public void recordTestResultsTest() throws ParseException {
        mHistorian.recordTestResults(mTestResults);
        List<TestResults> testResultsList = mHistorian.getAllTestResults();
        assertTrue(testResultsList.contains(mTestResults));
    }

    /**
     * Test pour savoir si le testResults est bien supprimé de la base de données
     */
    @Test
    public void deleteTestResultsTest() throws ParseException {
        mHistorian.deleteTestResults(mTestResults);
        List<TestResults> testResultsList = mHistorian.getAllTestResults();
        assertFalse(testResultsList.contains(mTestResults));
    }

}
