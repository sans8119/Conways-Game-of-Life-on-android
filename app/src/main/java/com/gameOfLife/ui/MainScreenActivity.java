package com.gameOfLife.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.TextView;

import com.gameOfLife.R;
import com.gameOfLife.utils.Constants;

public class MainScreenActivity extends Activity implements View.OnClickListener {

    private TaskFragment fragment;
    private Menu menu;
    private int aliveClr;
    private int deadClr;
    private CustomHandler handler;
    private String TAG = "MainScreenActivity";

    public CustomHandler getHandler() {
        return handler;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_of_life);

        ActionBar actionbar = getActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.show();
        aliveClr = getResources().getColor(R.color.alive_clr);
        deadClr = getResources().getColor(R.color.dead_clr);
        GridLayout gridLayout = (GridLayout) findViewById(R.id.grid_layout);
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridLayout.setOrientation(GridLayout.HORIZONTAL);
        } else {
            gridLayout.setOrientation(GridLayout.VERTICAL);
        }
        gridLayout.setRowCount(Constants.GRID_SIZE);
        FragmentManager fragmentManager = getFragmentManager();
        fragment = (TaskFragment) fragmentManager
                .findFragmentByTag(TaskFragment.TAG);
        if (fragment == null) {
            fragment = new TaskFragment();
            fragmentManager.beginTransaction().add(fragment,
                    TaskFragment.TAG).commit();
        }
        addGridCells();
        handler = new CustomHandler();
    }

    @Override
    protected void onStop() {
        GridLayout gridLayout = (GridLayout) findViewById(R.id.grid_layout);
        for (int i = 0; i < gridLayout.getChildCount(); i++)
            gridLayout.removeAllViews();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.menu_game_of_life, menu);
        if (fragment.isPlay()) {
            menu.findItem(R.id.start_stop).setIcon(R.drawable.stop);
        } else {
            menu.findItem(R.id.start_stop).setIcon(R.drawable.play_icon);
        }
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        GridLayout gridLayout = (GridLayout) findViewById(R.id.grid_layout);
        int color = getResources().getColor(R.color.dead_clr);
        switch (item.getItemId()) {
            case R.id.start_stop:
                if (fragment.isPlay()) {
                    fragment.setPlay(false);
                    item.setIcon(R.drawable.play_icon);
                    fragment.getGameOfLife().setStartStopFlag(false);
                } else {
                    fragment.setPlay(true);
                    item.setIcon(R.drawable.stop);
                    fragment.getGameOfLife().setStartStopFlag(true);
                    fragment.getGameOfLife().startAlgo(Constants.USER_DEFINED_PATTERN, getValuesFromUI());
                }
                break;

            case R.id.default_pattern:
                reset();
                fragment.getGameOfLife().setStartStopFlag(true);
                menu.findItem(R.id.start_stop).setIcon(R.drawable.play_icon);
                fragment.setPlay(true);
                menu.findItem(R.id.start_stop).setIcon(R.drawable.stop);
                fragment.getGameOfLife().startAlgo(Constants.DEFAULT_PATTERN, null);
                break;

            case R.id.reset:
                reset();
                menu.findItem(R.id.start_stop).setIcon(R.drawable.play_icon);
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (fragment.getGameOfLife() != null)
            fragment.getGameOfLife().setStartStopFlag(false);
    }

    public Menu getMenu() {
        return menu;
    }

    @Override
    public void onClick(View v) {
        if (fragment.getGameOfLife().isStartStopFlag()) {
        } else {
            setTextView(v);
        }
    }

    private int[][] getValuesFromUI() {
        TextView[] textViews = fragment.getViewHolder().getTextViews();
        int[][] values = new int[Constants.GRID_SIZE][Constants.GRID_SIZE];
        int j = 0;
        for (int i = 0; i < textViews.length; i++) {
            ColorDrawable cd = (ColorDrawable) (textViews[i].getBackground());
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
        return values;
    }

    private void setTextView(View v) {
        if (v instanceof TextView) {
            ColorDrawable cd = (ColorDrawable) ((TextView) v).getBackground();
            int colorCode = cd.getColor();
            if (colorCode == aliveClr) {
                ((TextView) v).setBackgroundColor(deadClr);
            } else {
                ((TextView) v).setBackgroundColor(aliveClr);
            }
        }
    }

    public void reset() {
        TextView[] textViews = fragment.getViewHolder().getTextViews();
        if (textViews[0] == null) return;
        int color = getResources().getColor(R.color.dead_clr);
        for (TextView textView : textViews) {
            textView.setBackgroundColor(color);
        }
        fragment.setPlay(false);
        fragment.getGameOfLife().setStartStopFlag(false);
    }

    private void initTextView(TextView valueTV, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int color = context.getResources().getColor(R.color.dead_clr);
        float padding = 16;
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int ht = (int) (dpHeight - padding);
        int width = (int) (dpWidth - padding);
        valueTV.setOnClickListener(this);
        valueTV.setHeight(ht / Constants.GRID_SIZE);
        valueTV.setWidth(width / Constants.GRID_SIZE);
        ColorDrawable cd = (ColorDrawable) ((TextView) valueTV).getBackground();
        if (cd != null)//this will be null when textViews in textviews array are initialized for the first time.
            color = cd.getColor();
        valueTV.setBackgroundColor(color);
        valueTV.setGravity(Gravity.BOTTOM);
    }

    public void addGridCells() {
        GridLayout gridLayout = (GridLayout) findViewById(R.id.grid_layout);
        TextView[] textViews = fragment.getViewHolder().getTextViews();
        if (textViews[0] == null) {
            for (int i = 0; i < textViews.length; i++) {
                TextView valueTV = new TextView(this);
                textViews[i] = valueTV;
                initTextView(valueTV, this);
                gridLayout.addView(valueTV);
            }
        } else {
            for (int i = 0; i < textViews.length; i++) {
                initTextView(textViews[i], this);
                gridLayout.addView(textViews[i]);
            }
        }
    }

    private class CustomHandler extends Handler {

        public void handleMessage(Message msg) {
            final int[] a = msg.getData().getIntArray(Constants.intArray);
            updateUI(a);
            super.handleMessage(msg);
        }

        private void updateUI(int[] a) {
            TextView[] textViews = fragment.getViewHolder().getTextViews();
            if (textViews[0] != null) {
                for (int i = 0; i < textViews.length; i++) {
                    TextView tv = textViews[i];
                    if (a[i] == 0) {
                        tv.setBackgroundColor(deadClr);
                    } else {
                        tv.setBackgroundColor(aliveClr);
                    }
                }
                Log.i(TAG, "*************UI UPDATED**************");
            }
        }

    }


}