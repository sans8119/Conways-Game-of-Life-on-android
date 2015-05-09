package com.gameOfLife.logic;


import java.lang.ref.WeakReference;

public class GameOfLifeBase implements GameOfLife {
    private Object object;

    @Override
    public void attachView(Object uiObject) {
        object = uiObject;
        WeakReference<Object> weakReference = new WeakReference<Object>(object);
    }

    @Override
    public void dettachView() {
        object = null;
    }

    @Override
    public boolean isViewPresent() {
        if (object != null)
            return true;
        else
            return false;
    }

    @Override
    public Object getView() {
        return object;
    }

}
