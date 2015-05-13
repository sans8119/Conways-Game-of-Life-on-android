package com.gameOfLife.tests;


import android.app.Instrumentation;
import android.graphics.drawable.ColorDrawable;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.Menu;
import android.widget.GridLayout;
import android.widget.TextView;

import com.gameOfLife.R;
import com.gameOfLife.ui.MainScreenActivity;
import com.gameOfLife.utils.Constants;

import junit.framework.TestResult;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class GameOfLifeActivityTest extends ActivityInstrumentationTestCase2<MainScreenActivity> {

    private MainScreenActivity gameOfLifeActivity;
    private TextView[] textViews;

    public GameOfLifeActivityTest() {
        super(MainScreenActivity.class);
        textViews=new TextView[Constants.GRID_SIZE*Constants.GRID_SIZE];
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        gameOfLifeActivity = getActivity();
    }

    public void testPreconditions() {
        assertNotNull("gameOfLifeActivity is null", gameOfLifeActivity);
    }

    public void testCaseCheckTotalCellsInGrid(){
        testPreconditions();
        assertEquals("Number of cells in grid:", 20*20,((GridLayout)gameOfLifeActivity.findViewById(R.id.grid_layout)).getChildCount());
    }

    public void testCaseCheckIfGridCellsChangeColorOnClick(){
        testPreconditions();
       int childCount= ((GridLayout)gameOfLifeActivity.findViewById(R.id.grid_layout)).getChildCount();
        for(int i=0;i<childCount;i++){
            textViews[i]=(TextView)((GridLayout)gameOfLifeActivity.findViewById(R.id.grid_layout)).getChildAt(i);
        }
        int colorAlive = gameOfLifeActivity.getResources().getColor(R.color.alive_clr);
        int colorDead = gameOfLifeActivity.getResources().getColor(R.color.dead_clr);
        for(int i=0;i<childCount;i++){
            ColorDrawable cd = (ColorDrawable)(textViews[i]).getBackground();
            int currentColor = cd.getColor();
            TouchUtils.clickView(this, textViews[i]);
            cd = (ColorDrawable)(textViews[i]).getBackground();
            int actual=cd.getColor();
            if(currentColor==colorAlive)
            assertEquals("Color of the grid cell should change on click", colorDead,actual);
            else
                assertEquals("Color of the grid cell should change on click", colorAlive,actual);
        }

        for(int i=0;i<childCount;i++){
            ColorDrawable cd = (ColorDrawable)(textViews[i]).getBackground();
            int currentColor = cd.getColor();
            int expectedColor=colorDead;
            assertEquals("Reseting the grid ", colorDead,expectedColor);
        }
    }
    public void testCaseClickOnDefoultActionButton(){
        testPreconditions();
        actionBarItemClick(R.id.default_pattern, this.getInstrumentation(), gameOfLifeActivity.getMenu());
    }

    private void actionBarItemClick(int menuItemId, Instrumentation instruments, Menu menuInstance) {
        final Integer itemId = menuItemId;
        final Menu menu = menuInstance;
        instruments.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                menu.performIdentifierAction(itemId, 0);
            }
        });
    }

    public GameOfLifeActivityTest(Class<MainScreenActivity> activityClass) {
        super(activityClass);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }


    @Override
    public void runTestOnUiThread(Runnable r) throws Throwable {
        super.runTestOnUiThread(r);
    }

    @Override
    public int countTestCases() {
        return super.countTestCases();
    }

    @Override
    protected TestResult createResult() {
        return super.createResult();
    }

    @Override
    public TestResult run() {
        return super.run();
    }

    @Override
    public void run(TestResult result) {
        super.run(result);
    }

    @Override
    protected void runTest() throws Throwable {
        super.runTest();
    }

    @Override
    public void sendKeys(String keysSequence) {
        super.sendKeys(keysSequence);
    }

    @Override
    public void sendKeys(int... keys) {
        super.sendKeys(keys);
    }

    @Override
    public void sendRepeatedKeys(int... keys) {
        super.sendRepeatedKeys(keys);
    }
}
