package com.gameOfLife.ui;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gameOfLife.logic.MainScreenLogic;

public class TaskFragment extends Fragment {

    public static String TAG = "TaskFragment";
    private Activity mActivity;
    private MainScreenLogic gameOfLife;

    public MainScreenLogic getGameOfLife() {
        return gameOfLife;
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
        gameOfLife.addGridCells(this, mActivity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        gameOfLife.dettachView();
    }

}
