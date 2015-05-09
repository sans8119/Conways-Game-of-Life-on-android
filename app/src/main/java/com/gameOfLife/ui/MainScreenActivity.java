package com.gameOfLife.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridLayout;

import com.gameOfLife.R;
import com.gameOfLife.utils.Constants;

public class MainScreenActivity extends Activity {

    private TaskFragment fragment;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_of_life);
        ActionBar actionbar = getActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.show();
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

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.menu_game_of_life, menu);
        if (fragment.getGameOfLife().isPlay()) {
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
                if (fragment.getGameOfLife().getDriver() != null) {
                    if (fragment.getGameOfLife().isPlay()) {
                        fragment.getGameOfLife().setPlay(false);
                        item.setIcon(R.drawable.play_icon);
                        fragment.getGameOfLife().getDriver().setStartStopFlag(false);
                    } else {
                        fragment.getGameOfLife().setPlay(true);
                        item.setIcon(R.drawable.stop);
                        fragment.getGameOfLife().getDriver().setStartStopFlag(true);
                        fragment.getGameOfLife().startAlgo(Constants.USER_DEFINED_PATTERN);
                    }
                } else {
                    fragment.getGameOfLife().setPlay(true);
                    item.setIcon(R.drawable.stop);
                    fragment.getGameOfLife().startAlgo(Constants.USER_DEFINED_PATTERN);
                }
                break;

            case R.id.default_pattern:
                fragment.getGameOfLife().reset();
                menu.findItem(R.id.start_stop).setIcon(R.drawable.play_icon);
                fragment.getGameOfLife().setPlay(true);
                menu.findItem(R.id.start_stop).setIcon(R.drawable.stop);
                fragment.getGameOfLife().startAlgo(Constants.DEFAULT_PATTERN);
                break;

            case R.id.reset:
                fragment.getGameOfLife().reset();
                menu.findItem(R.id.start_stop).setIcon(R.drawable.play_icon);
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (fragment.getGameOfLife().getDriver() != null)
            fragment.getGameOfLife().getDriver().setStartStopFlag(false);
    }

    public Menu getMenu() {
        return menu;
    }

}