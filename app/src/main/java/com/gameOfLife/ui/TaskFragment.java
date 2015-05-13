package com.gameOfLife.ui;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gameOfLife.logic.MainScreenLogic;
import com.gameOfLife.utils.Constants;

public class TaskFragment extends Fragment {

    public static String TAG = "TaskFragment";
    private Activity mActivity;
    private MainScreenLogic gameOfLife;
    private ViewHolder viewHolder = new ViewHolder();
    private boolean play = false;

    public boolean isPlay() {
        return play;
    }

    public void setPlay(boolean play) {
        this.play = play;
    }

    public MainScreenLogic getGameOfLife() {
        return gameOfLife;
    }

    public ViewHolder getViewHolder() {
        if (viewHolder == null)
            viewHolder = new ViewHolder();
        return viewHolder;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return null;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        if (gameOfLife == null) {
            gameOfLife = new MainScreenLogic(mActivity);
        } else {
            gameOfLife.attachView(mActivity);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        gameOfLife.dettachView();
    }

    public class ViewHolder {
        private TextView[] textViews;

        public ViewHolder() {
            textViews = new TextView[Constants.GRID_SIZE * Constants.GRID_SIZE];
        }

        public TextView[] getTextViews() {
            return textViews;
        }

    }

}
