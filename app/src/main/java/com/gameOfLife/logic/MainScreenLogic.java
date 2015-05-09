package com.gameOfLife.logic;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import com.gameOfLife.R;
import com.gameOfLife.algo.Driver;
import com.gameOfLife.ui.TaskFragment;
import com.gameOfLife.utils.Constants;

public class MainScreenLogic extends GameOfLifeBase {
    public static String TAG = "TaskFragment";
    private Thread startAlgo;
    private boolean play = false;
    private Driver driver;
    private CustomHandler handler;
    private int aliveClr;
    private int deadClr;
    private TextView[] textViews;
    private View.OnClickListener onClickListener;

    public MainScreenLogic(Activity activity) {
        super.attachView(activity);
        aliveClr = activity.getResources().getColor(R.color.alive_clr);
        deadClr = activity.getResources().getColor(R.color.dead_clr);
    }

    private void updateUI(int[] a) {
        long t = 0;
        GridLayout lay = ((GridLayout) ((Activity) super.getView()).findViewById(R.id.grid_layout));
        for (int i = 0; i < lay.getChildCount(); i++) {
            TextView tv = ((TextView) lay.getChildAt(i));
            if (a[i] == 0) {
                tv.setBackgroundColor(deadClr);
            } else {
                tv.setBackgroundColor(aliveClr);
            }
        }
        Log.i(TAG, "*************UI UPDATED**************" + (System.currentTimeMillis() - t));
        t = System.currentTimeMillis();
    }

    private void initTextView(TextView valueTV, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int color = context.getResources().getColor(R.color.dead_clr);
        float padding = 16;
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int ht = (int) (dpHeight - padding);
        int width = (int) (dpWidth - padding);
        valueTV.setOnClickListener(getOnClickListener());
        valueTV.setHeight(ht / Constants.GRID_SIZE);
        valueTV.setWidth(width / Constants.GRID_SIZE);
        ColorDrawable cd = (ColorDrawable) ((TextView) valueTV).getBackground();
        if (cd != null)//this will be null when textViews in textviews array are initialized for the first time.
            color = cd.getColor();

        valueTV.setBackgroundColor(color);
        valueTV.setGravity(Gravity.BOTTOM);
    }

    public int getAliveClr() {
        return aliveClr;
    }

    public int getDeadClr() {
        return deadClr;
    }

    public boolean isPlay() {
        return play;
    }

    public void setPlay(boolean play) {
        this.play = play;
    }

    public CustomHandler getHandler() {
        return handler;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriverNull() {
        driver = null;
    }

    public View.OnClickListener getOnClickListener() {
        if (onClickListener == null) {
            onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "****OnClickListener****" + driver);
                    if (driver != null) {
                        if (driver.isStartStopFlag()) {
                        } else {
                            setTextView(v);
                        }
                    } else {
                        setTextView(v);
                    }
                }

                private void setTextView(View v) {
                    if (v instanceof TextView) {
                        ColorDrawable cd = (ColorDrawable) ((TextView) v).getBackground();
                        int colorCode = cd.getColor();
                        if (colorCode == aliveClr /*Color.parseColor(Constants.aliveClr)*/) {
                            ((TextView) v).setBackgroundColor(deadClr);
                        } else {
                            ((TextView) v).setBackgroundColor(aliveClr);
                        }
                    }
                }
            };
        }
        return onClickListener;
    }

    public void startAlgo(final int patterType) {
        final Activity activity = ((Activity) super.getView());
        if (handler == null)
            handler = new CustomHandler();
        startAlgo = new Thread() {
            public void run() {
                if (driver == null) {
                    driver = new Driver();
                }
                GridLayout gridLayout = (GridLayout) activity.findViewById(R.id.grid_layout);
                int[][] values = new int[Constants.GRID_SIZE][Constants.GRID_SIZE];
                int j = 0;
                for (int i = 0; i < gridLayout.getChildCount(); i++) {
                    TextView tv = (TextView) gridLayout.getChildAt(i);
                    ColorDrawable cd = (ColorDrawable) ((TextView) tv).getBackground();
                    int colorCode = cd.getColor();
                    if (colorCode == aliveClr) {
                        values[j][i % Constants.GRID_SIZE] = 1;
                    } else {
                        values[j][i % Constants.GRID_SIZE] = 0;
                    }
                    if (i >= Constants.GRID_SIZE && i % Constants.GRID_SIZE == 0) {
                        ++j;
                    }
                }
                driver.setHandler(handler);
                try {
                    driver.main(values, patterType);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        startAlgo.start();
    }

    @Override
    public void dettachView() {
        Activity context;
        if (super.isViewPresent()) {
            context = ((Activity) super.getView());
            GridLayout gridLayout = (GridLayout) context.findViewById(R.id.grid_layout);
            gridLayout.removeAllViews();
        }
        super.dettachView();
    }

    public void addGridCells(TaskFragment fragment, Activity context) {
        GridLayout gridLayout = (GridLayout) context.findViewById(R.id.grid_layout);
        if (textViews == null) {
            textViews = new TextView[Constants.GRID_SIZE * Constants.GRID_SIZE];
            for (int i = 0; i < textViews.length; i++) {
                TextView valueTV = new TextView(context);
                textViews[i] = valueTV;
                initTextView(valueTV, context);
                gridLayout.addView(valueTV);
            }
        } else {
            for (int i = 0; i < textViews.length; i++) {
                initTextView(textViews[i], context);
                gridLayout.addView(textViews[i]);
            }
        }
    }

    public void reset() {
        for (int i = 0; i < textViews.length; i++) {
            textViews[i].setBackgroundColor(deadClr);
        }
        if (driver != null) {
            driver.setStartStopFlag(false);
            driver = null;
            play = false;
        }

    }

    private class CustomHandler extends Handler {
        public void handleMessage(Message msg) {
            final int[] a = msg.getData().getIntArray(Constants.intArray);
            updateUI(a);
            super.handleMessage(msg);
        }
    }


}
